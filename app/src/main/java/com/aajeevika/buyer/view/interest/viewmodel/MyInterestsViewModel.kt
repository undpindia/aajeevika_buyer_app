package com.aajeevika.buyer.view.interest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.model.data_model.CategoryModel
import com.aajeevika.buyer.model.data_model.UserInterestData
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction
import com.google.gson.JsonObject

class MyInterestsViewModel : BaseViewModel() {
    private val _interest = MutableLiveData<ArrayList<UserInterestData>>()
    val interest : LiveData<ArrayList<UserInterestData>> = _interest


    fun addInterest(jsonObject: JsonObject, success: (msg: String) -> Unit) {
        requestData(
            { apiService.expressInterest(jsonObject) },
            { success(it.message ?: "") },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG
        )
    }

    fun userInterest() {
        requestData(
            { apiService.userInterest() },
            { it.data?.user_interest?.let { result->_interest.postValue(result) }},
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE
        )
    }

    fun interestDetail(id:Int) {
        requestData(
            { apiService.interestDetail(id) },
            { it.data?.seller_interest?.let { result->_interest.postValue(result) }},
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG
        )
    }
}