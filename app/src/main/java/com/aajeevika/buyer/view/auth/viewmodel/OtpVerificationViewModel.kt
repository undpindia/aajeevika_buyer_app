package com.aajeevika.buyer.view.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.model.data_model.OtpModel
import com.aajeevika.buyer.model.data_model.UserProfileModel
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction

class OtpVerificationViewModel : BaseViewModel() {
    private val _userProfileLiveData = MutableLiveData<UserProfileModel>()
    val userProfileLiveData: LiveData<UserProfileModel> = _userProfileLiveData

    private val _userProfile = MutableLiveData<UserProfileModel>()
    val userProfile: LiveData<UserProfileModel> = _userProfile

    private val _otpLiveData = MutableLiveData<OtpModel>()
    val otpLiveData: LiveData<OtpModel> = _otpLiveData

    fun requestOtpVerification(mobileNo: String?, otp: String?, isSignUp: Boolean,userId:String?=null) {
        if (mobileNo != null && otp != null) {
            val map = HashMap<String, Any>()
            map["mobile"] = mobileNo
            map["otp"] = otp
            if (isSignUp){
                map["type"] = "signup"
            }
            if(userId != null){
                map["type"] = "updatemobile"
                map["user_id"] = userId
            }

            requestData(
                { apiService.verifyOtp(map) },
                { it.data.run { _userProfileLiveData.postValue(this) } },
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

    fun requestUserProfile() {
        requestData({ apiService.getProfile() }, {
            it.data?.let { result ->
                preferencesHelper.name =  result.user?.name ?: ""
                preferencesHelper.title =  result.user?.title ?: ""
                preferencesHelper.email =  result.user?.email ?: ""
                preferencesHelper.mobile =  result.user?.mobile ?: ""
                preferencesHelper.profileImage =  result.user?.profileImage ?: ""
                _userProfile.postValue(result)
            }
        },
        progressAction = ProgressAction.PROGRESS_DIALOG,
        errorType = ErrorType.DIALOG)
    }

}