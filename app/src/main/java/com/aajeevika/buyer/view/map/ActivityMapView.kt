package com.aajeevika.buyer.view.map

import android.location.Geocoder
import android.os.Bundle
import com.aajeevika.buyer.R
import com.aajeevika.buyer.baseclasses.BaseActivityVM
import com.aajeevika.buyer.databinding.ActivitySelectLocationBinding
import com.aajeevika.buyer.utility.extension.gotoNewTask
import com.aajeevika.buyer.view.home.ActivityDashboard
import com.aajeevika.buyer.view.map.viewmodel.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import java.util.*

class ActivityMapView: BaseActivityVM<ActivitySelectLocationBinding, MapViewModel>(R.layout.activity_select_location, MapViewModel::class), OnMapReadyCallback {
    private lateinit var geocoder: Geocoder
    private lateinit var latLng: LatLng

    private var googleMap: GoogleMap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        geocoder = Geocoder(this, Locale.getDefault())
        latLng = LatLng(preferencesHelper.latitude, preferencesHelper.longitude)

        (supportFragmentManager.findFragmentById(R.id.google_map_container) as? SupportMapFragment)?.getMapAsync(this)

        viewDataBinding.run {
            confirmAddressBtn.setOnClickListener {
                preferencesHelper.latitude = latLng.latitude
                preferencesHelper.longitude = latLng.longitude
                preferencesHelper.location = viewDataBinding.locationTxt.text.toString()
                gotoNewTask(ActivityDashboard::class.java)
            }
        }
    }

    /**
     * Once the map is ready move camera to the [latLng] with animation and display the formatted address on the screen.
     */
    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        this.googleMap?.setOnCameraIdleListener(CameraMoveListener())

        viewDataBinding.locationTxt.text = getAddressFromLatLong(latLng)

        this.googleMap?.run {
            animateCamera(CameraUpdateFactory.newLatLngZoom(latLng , 16F))
        }
    }

    /**
     * Get the address from google map api using [latLng]. If address is not found returns an empty [String].
     */
    private fun getAddressFromLatLong(latLng: LatLng) : String = run {
        val addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

        if(addressList.isNotEmpty()) {
            val addressFromGoogleMap = addressList[0]
            addressFromGoogleMap.getAddressLine(0)
        } else {
            ""
        }
    }

    inner class CameraMoveListener : GoogleMap.OnCameraIdleListener {

        /**
         *  Get current position of the camera once camera stops moving and become idle.
         *  Get address of the current location [getAddressFromLatLong] and display to the screen.
         * */
        override fun onCameraIdle() {
            googleMap?.cameraPosition?.target?.let {
                latLng = it
                viewDataBinding.locationTxt.text = getAddressFromLatLong(it)
            }
        }
    }
}