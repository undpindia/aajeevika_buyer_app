package com.aajeevika.buyer.view.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.model.data_model.AddressData
import com.aajeevika.buyer.model.data_model.CityData
import com.aajeevika.buyer.model.data_model.CountryData
import com.aajeevika.buyer.model.data_model.StateData
import com.aajeevika.buyer.utility.*
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction

class AddressViewModel : BaseViewModel() {
    private val _requestStatus = MutableLiveData<String>()
    val requestStatus: LiveData<String> = _requestStatus

    private val _cityList = MutableLiveData<ArrayList<CityData>>()
    val cityList: LiveData<ArrayList<CityData>> = _cityList
    val cityNameList: LiveData<ArrayList<String>> = Transformations.map(_cityList) { list ->
        ArrayList(list.map { it.name ?: "" }.toList())
    }

    private val _stateList = MutableLiveData<ArrayList<StateData>>()
    val stateList: LiveData<ArrayList<StateData>> = _stateList

    private val _countryList = MutableLiveData<ArrayList<CountryData>>()
    val countryList: LiveData<ArrayList<CountryData>> = _countryList

    init {
        requestCountryList()
        requestStateList()
        requestCityList()
    }

    private fun requestCountryList() {
        requestData(
            { apiService.getCountries() },
            { it.data?.country?.let { country -> _countryList.postValue(country) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG
        )
    }

    private fun requestStateList() {
        val map = HashMap<String, Any>()
        map[KEY_COUNTRY_ID] = Constant.DEFAULT_COUNTRY_ID

        requestData(
            { apiService.getState(map) },
            { it.data?.states?.let { states -> _stateList.postValue(states) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG
        )
    }

    private fun requestCityList() {
        val map = HashMap<String, Any>()
        map[KEY_STATE_ID] = Constant.DEFAULT_STATE_ID

        requestData({ apiService.getCity(map) }, {
            it.data?.district?.let { dis ->
                _cityList.postValue(dis)
            }
        },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG
        )
    }

    fun requestUpdateAddress(address: AddressData) {
        val map = HashMap<String, Any>()
        map[KEY_ADDRESS_LINE_ONE] = address.address_line_one ?: ""
        map[KEY_ADDRESS_LINE_TWO] = address.address_line_two ?: ""
        map[KEY_VILLAGE] =  "NA"
        map["block"] =  "NA"
        map[KEY_PIN_CODE] = address.pincode ?: ""
        map[KEY_DISTRICT] = address.districtId ?: 0
        map[KEY_COUNTRY] = address.countryId ?: 0
        map[KEY_STATE] = address.stateId ?: 0
        address.id?.let { map[KEY_ID] = it }

        requestData(
            {address.id?.run { apiService.updateAddress(map) } ?: apiService.addAddress(map)},
            { it.message?.run { _requestStatus.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG,
        )
    }
}