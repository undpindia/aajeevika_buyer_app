package com.aajeevika.buyer.location.helper

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aajeevika.buyer.R
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

class FusedLocationFetcher(
    private val activity: Activity,
    private var mLocationChangeListener: LocationChangeListener? = null,
    private var mIsPermissionMandatory: Boolean = false,
    private var mIsContinuousLocationUpdates: Boolean = false,
    private var mLocationInterval: Long = 200
) {
    private val mFusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(activity)
    }
    private val mLocationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        interval = mLocationInterval
        fastestInterval = 100
    }
    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            if (locationResult != null) {
                val latitude = locationResult.lastLocation.latitude
                val longitude = locationResult.lastLocation.longitude
                mLocationChangeListener?.onLocationChange(latitude, longitude)
                if (!mIsContinuousLocationUpdates) {
                    stopLocationUpdates()
                }
            }
        }
    }

    fun setLocationListener(locationChangeListener: LocationChangeListener) {
        mLocationChangeListener = locationChangeListener
    }

    fun setIsPermissionMandatory(isPermissionMandatory: Boolean) {
        mIsPermissionMandatory = isPermissionMandatory
    }

    fun setIsContinuesLocationUpdates(isContinuousLocationUpdates: Boolean) {
        mIsContinuousLocationUpdates = isContinuousLocationUpdates
    }

    fun setLocationUpdatesInterval(interval: Long) {
        mLocationInterval = interval
    }

    fun getCurrentLocation() {
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (isLocationPermissionGranted(activity)) {
            displayLocationSettingsRequest()
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun displayLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(activity)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            startLocationUpdates()
        }.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(
                        activity,
                        GPS_SETTINGS_REQUEST_CODE
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                    sendEx.printStackTrace()
                }
            }
        }
    }

    /**
     * Call this method to start location updates
     */
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        stopLocationUpdates()
        mFusedLocationProviderClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.getMainLooper()
        )

    }

    /**
     * Call this method to stop location updates
     */
    fun stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            GPS_SETTINGS_REQUEST_CODE, REQ_LOCATION_PERMISSION_SETTINGS -> {
                if (resultCode == Activity.RESULT_OK) {
                    startLocationUpdates()
                } else if (mIsPermissionMandatory) {
                    displayLocationSettingsRequest()
                }
            }
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    displayLocationSettingsRequest()
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    val shouldShowRequestPermissionRationale =
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            activity, Manifest.permission.ACCESS_FINE_LOCATION
                        )

                    Log.e("check", "$shouldShowRequestPermissionRationale")
                    if (shouldShowRequestPermissionRationale) {
                        if (mIsPermissionMandatory) {
                            checkLocationPermission()
                        }
                    } else {
                        showSettingDialog()
                    }
                }
            }
        }
    }

    private fun showSettingDialog() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle(activity.getString(R.string.permission_required))
        alertDialogBuilder.setMessage(activity.getString(R.string.permission_required_message))
        alertDialogBuilder.setPositiveButton(activity.getString(R.string.go_to_setting)) { _, _ ->
            activity.startActivityForResult(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", activity.packageName, null)
            }, REQ_LOCATION_PERMISSION_SETTINGS)
        }
        alertDialogBuilder.setNegativeButton(activity.getString(R.string.cancel)) { dialogInterface, i ->
            dialogInterface.dismiss()
            if (mIsPermissionMandatory) {
                activity.finishAffinity()
            }
        }
        val dialog: AlertDialog = alertDialogBuilder.create()
        dialog.show()
    }

    companion object {
        const val GPS_SETTINGS_REQUEST_CODE = 11011
        const val REQ_LOCATION_PERMISSION_SETTINGS = 11012
        const val LOCATION_PERMISSION_REQUEST_CODE = 11013
    }

    interface LocationChangeListener {
        fun onLocationChange(latitude: Double, longitude: Double)
    }

    private fun isLocationPermissionGranted(activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}