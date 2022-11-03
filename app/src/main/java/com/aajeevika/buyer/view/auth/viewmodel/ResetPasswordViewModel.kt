package com.aajeevika.buyer.view.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction

class ResetPasswordViewModel : BaseViewModel() {
    private val _requestStatus = MutableLiveData<String>()
    val requestStatus: LiveData<String> = _requestStatus

    fun requestPasswordChange(mobileNo: String?, password: String?, otp: Int?) {
        if (mobileNo != null && password != null) {
            val map = HashMap<String, Any>()
            map["mobile"] = mobileNo
            map["password"] = password
            map["otp"] = otp ?: 0

            requestData(
                { apiService.changePassword(map) },
                { it.message?.run { _requestStatus.postValue(this) } },
                progressAction = ProgressAction.PROGRESS_DIALOG,
                errorType = ErrorType.DIALOG,
            )
        }
    }

}