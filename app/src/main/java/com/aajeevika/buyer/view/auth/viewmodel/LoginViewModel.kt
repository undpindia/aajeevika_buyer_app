package com.aajeevika.buyer.view.auth.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.model.data_model.UserProfileModel
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction

class LoginViewModel : BaseViewModel() {

    private val _userProfileLiveData = MutableLiveData<UserProfileModel>()
    val userProfileLiveData: LiveData<UserProfileModel> = _userProfileLiveData

    fun requestUserLogin(phoneOrEmail: String, password: String) {
        val map = HashMap<String, Any>()

        if (Patterns.EMAIL_ADDRESS.matcher(phoneOrEmail).matches()) map["email"] = phoneOrEmail
        else map["mobile"] = phoneOrEmail
        map["role_id"] = 1
        map["password"] = password

        requestData(
            { apiService.loginUser(map) },
            { it.data.let { result -> _userProfileLiveData.postValue(result) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG,
        )
    }
}