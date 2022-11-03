package com.aajeevika.buyer.view.application

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivitySplashScreenBinding
import com.aajeevika.buyer.utility.extension.checkUserPermissions
import com.aajeevika.buyer.view.application.viewmodel.SplashScreenViewModel
import com.aajeevika.buyer.view.auth.ActivitySelectLanguage
import com.aajeevika.buyer.view.home.ActivityDashboard
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import java.text.DateFormat
import java.util.*

class ActivitySplashScreen : BaseActivityVM<ActivitySplashScreenBinding, SplashScreenViewModel>(
    R.layout.activity_splash_screen,
    SplashScreenViewModel::class
) {

    private val REQUEST_CHECK_SETTINGS = 1010
    private val mTAG = "FusedLocation"
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mSettingsClient: SettingsClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationCallback: LocationCallback? = null
    private var mCurrentLocation: Location? = null
    private var mRequestingLocationUpdates = false
    private var mLastUpdateTime: String? = null

    private val locationPermissionCallback = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if(it[Manifest.permission.ACCESS_FINE_LOCATION] == true && it[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            startLocation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                mCurrentLocation = locationResult.lastLocation
                mLastUpdateTime = DateFormat.getTimeInstance().format(Date())

                preferencesHelper.latitude = locationResult.lastLocation.latitude
                preferencesHelper.longitude = locationResult.lastLocation.longitude
                moveNext()
            }
        }

        locationPermissionCallback.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    private fun moveNext() {
        if(!preferencesHelper.languageSelected) {
            val intent = Intent(this@ActivitySplashScreen, ActivitySelectLanguage::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this@ActivitySplashScreen, ActivityDashboard::class.java)
            startActivity(intent)
            finish()
        }
    }

    public override fun onResume() {
        super.onResume()
        if (mRequestingLocationUpdates) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mRequestingLocationUpdates) {
            stopLocationUpdates()
        }
    }

    private fun startLocation() {
        Log.d(mTAG, "initLocation")
        mLocationRequest = LocationRequest.create().apply {
            interval = 0
            fastestInterval = 50
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest!!)
        mSettingsClient = LocationServices.getSettingsClient(this)

        val task = mSettingsClient?.checkLocationSettings(builder.build())
        task?.addOnSuccessListener { locationSettingsResponse ->
            startLocationUpdates()
        }

        task?.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(
                        this@ActivitySplashScreen,
                        12345
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    private fun startLocationUpdates() {
        Log.d(mTAG, "startLocationUpdates")
        mRequestingLocationUpdates = true
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        mFusedLocationClient?.requestLocationUpdates(
            mLocationRequest!!,
            mLocationCallback!!,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        Log.d(mTAG, "stopLocationUpdates")
        mFusedLocationClient
            ?.removeLocationUpdates(mLocationCallback)
            ?.addOnCompleteListener(this) {}
    }
}