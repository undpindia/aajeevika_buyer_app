package com.aajeevika.buyer.view.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.model.data_model.*
import com.aajeevika.buyer.utility.KEY_ARTISAN_ID
import com.aajeevika.buyer.utility.KEY_EMAIL
import com.aajeevika.buyer.utility.KEY_NAME
import com.aajeevika.buyer.utility.KEY_PAGE
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProfileViewModel : BaseViewModel() {

    private val _requestStatus = MutableLiveData<String>()
    val requestStatus: LiveData<String> = _requestStatus

    private val _userProfile = MutableLiveData<UserProfileModel>()
    val userProfile: LiveData<UserProfileModel> = _userProfile

    private val _artisanProfile = MutableLiveData<ArtisanProfileModel>()
    val artisanProfile : LiveData<ArtisanProfileModel> = _artisanProfile

    private val _artisanProduct = MutableLiveData<ArtisanProductListModel>()
    val artisanProduct : LiveData<ArtisanProductListModel> = _artisanProduct

    private val _requestStatusLiveData = MutableLiveData<String>()
    val requestStatusLiveData: LiveData<String> = _requestStatusLiveData

    fun requestUserProfile() {
        requestData({ apiService.getProfile() }, {
            it.data?.let { result ->
                preferencesHelper.addressData = Gson().toJson(result.address?: AddressData())

                preferencesHelper.name = result.user?.name ?: ""
                preferencesHelper.title = result.user?.title ?: ""
                preferencesHelper.email = result.user?.email ?: ""
                preferencesHelper.mobile = result.user?.mobile ?: ""
                preferencesHelper.profileImage = result.user?.profileImage ?: ""

                _userProfile.postValue(result)
            }
        },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG
        )
    }

    fun requestProfileImageUpload(file: File) {
        val filePart = MultipartBody.Part.createFormData(
            "profileimage", file.name,
            file.asRequestBody("image/*".toMediaTypeOrNull())
        )
        requestData(
            { apiService.uploadProfileImage(filePart) },
            { it.message?.run { _requestStatus.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG
        )
    }

    fun requestUpdateUserProfile(name: String, emailId: String) {
        val map = HashMap<String, Any>()
        map[KEY_NAME] = name
        if(emailId.isNotEmpty()) map[KEY_EMAIL] = emailId

        requestData(
            { apiService.updateUserProfile(map) },
            { it.message?.run { _requestStatus.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG
        )
    }

    fun requestArtisanProfile(id: Int) {
        val map = HashMap<String, Any>()
        map[KEY_ARTISAN_ID] = id

        requestData(
            { apiService.getArtisanProfile(map) },
            { it.data?.let { result -> _artisanProfile.postValue(result) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG
        )
    }

    fun requestArtisanProducts(id: Int, page: Int = 1) {
        val map = HashMap<String, Any>()
        map[KEY_ARTISAN_ID] = id
        map[KEY_PAGE] = page

        requestData(
            { apiService.getArtisanProducts(map) },
            { it.data?.let { result -> _artisanProduct.postValue(result) } },
            progressAction = ProgressAction.NONE,
            errorType = ErrorType.NONE
        )
    }

    fun addFavorite(id: Int, status: Int) {
        val requestMap = hashMapOf<String, Any>(
            Pair("seller_id", id),
            Pair("status", status),
        )

        requestData(
            { apiService.addFavorite(requestMap) },
            { it.message?.run { _requestStatusLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG,
        )
    }
}