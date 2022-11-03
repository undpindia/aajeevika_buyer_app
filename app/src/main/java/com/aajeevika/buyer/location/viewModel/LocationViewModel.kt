package com.aajeevika.buyer.location.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.location.model.PlaceAddressResult
import com.aajeevika.buyer.location.model.PlaceDetailsResponse
import com.aajeevika.buyer.location.model.SearchLocationResponse

class LocationViewModel : BaseViewModel() {

    private val _address = MutableLiveData<ArrayList<PlaceAddressResult>>()
    val address: LiveData<ArrayList<PlaceAddressResult>> = _address

    private val _resPredictionsList =
        MutableLiveData<ArrayList<SearchLocationResponse.Predictions>>()
    val resPredictionsList: LiveData<ArrayList<SearchLocationResponse.Predictions>> =
        _resPredictionsList

    private val _resPlaceDetails = MutableLiveData<PlaceDetailsResponse>()
    val resPlaceDetails: LiveData<PlaceDetailsResponse> = _resPlaceDetails


    fun hitGetPlaceApi(map: HashMap<String, String>) {
        requestPlaceData({ mMapApiService.getLocation(map) }, {
            _resPredictionsList.value = it.predictions
        })
    }

    fun hitGetPlaceDetailsApi(map: HashMap<String, String>) {
        requestPlaceData({ mMapApiService.getPlaceDetails(map) }, {
            _resPlaceDetails.value = it
        })
    }

    fun getAddressFromLatLng(map: HashMap<String, String>) {
        requestPlaceData({ mMapApiService.getAddressFromLatLng(map) }, {
            _address.value = it.results
        })
    }
}