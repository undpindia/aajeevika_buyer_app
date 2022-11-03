package com.aajeevika.buyer.view.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.model.data_model.OtpModel
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction

class RegisterViewModel : BaseViewModel() {
    private val _otpLiveData = MutableLiveData<OtpModel>()
    val otpLiveData: LiveData<OtpModel> = _otpLiveData

    fun requestUserRegistration(
        name: String,
        email: String,
        mobileNumber: String,
        password: String
    ) {
        val map = HashMap<String, Any>()
        map["role_id"] = 1
        map["name"] = name
        map["password"] = password
        map["mobile"] = mobileNumber
        if(email.isNotEmpty()) map["email"] = email

        requestData(
            { apiService.registerUser(map) },
            { it.data?.run { _otpLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG
        )
    }
}