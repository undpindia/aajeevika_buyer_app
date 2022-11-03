package com.aajeevika.buyer.view.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.model.data_model.OtpModel
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction

class ForgotPasswordViewModel : BaseViewModel() {
    private val _otpLiveData = MutableLiveData<OtpModel>()
    val otpLiveData: LiveData<OtpModel> = _otpLiveData

    fun requestForgotPassword(mobileNo: String?) {
        mobileNo?.let {
            val map = HashMap<String, Any>()
            map["mobile"] = mobileNo

            requestData(
                { apiService.forgotPassword(map) },
                { it.data?.let { result -> _otpLiveData.postValue(result) } },
                progressAction = ProgressAction.PROGRESS_DIALOG,
                errorType = ErrorType.DIALOG,
            )
        }
    }

    fun requestOtpResend(mobileNo: String?, isChangeMobile: Boolean = false) {
        mobileNo?.let {
            val map = HashMap<String, Any>()

            map["mobile"] = mobileNo
            if (isChangeMobile) map["type"] = "updatemobile"

            requestData(
                { apiService.resendOtp(map) },
                { it.data?.run { _otpLiveData.postValue(this) } },
                progressAction = ProgressAction.PROGRESS_DIALOG,
                errorType = ErrorType.DIALOG,
            )
        }
    }

}