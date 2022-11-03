package com.aajeevika.buyer.baseclasses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aajeevika.buyer.location.MapApiService
import com.aajeevika.buyer.model.data_model.BaseDataModel
import com.aajeevika.buyer.model.data_model.ErrorDataModel
import com.aajeevika.buyer.model.network_request.ApiService
import com.aajeevika.buyer.utility.AppPreferencesHelper
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.core.context.GlobalContext
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Response

abstract class BaseViewModel : ViewModel() {
    protected val apiService: ApiService by inject(clazz = ApiService::class.java)
    protected val mMapApiService: MapApiService by inject(clazz = MapApiService::class.java)
    protected val preferencesHelper: AppPreferencesHelper = GlobalContext.get().get()

    private val _errorMessage = MutableLiveData<ErrorDataModel>()
    val errorMessage: LiveData<ErrorDataModel> get() = _errorMessage

    private val _progressHandler = MutableLiveData<ProgressAction>()
    val progressHandler: LiveData<ProgressAction> get() = _progressHandler

    fun <T> requestData(
        action: suspend () -> Response<BaseDataModel<T>>,
        success: (m: BaseDataModel<T>) -> Unit,
        progressAction: ProgressAction = ProgressAction.PROGRESS_BAR,
        errorType: ErrorType = ErrorType.NONE,
    ) {
        if (progressAction != ProgressAction.NONE)
            _progressHandler.value = progressAction

        viewModelScope.launch {
            try {
                val requestResponse = withContext(Dispatchers.IO) { action() }

                if(requestResponse.isSuccessful) {
                    requestResponse.body()?.let {
                        if(it.status) success(it)
                        else handleError(it.message, errorType)
                    }
                } else {
                    handleError(requestResponse.message(), errorType)
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                handleError(exception.message, errorType)
            } finally {
                if (progressAction != ProgressAction.NONE)
                    _progressHandler.value = ProgressAction.DISMISS
            }
        }
    }

    fun <T> requestPlaceData(
        action: suspend () -> T,
        success: (m: T) -> Unit,
        progressPriority: ProgressAction = ProgressAction.PROGRESS_BAR,
        errorType: ErrorType = ErrorType.TOAST,
    ) {
        if (progressPriority != ProgressAction.NONE)
            _progressHandler.value = progressPriority

        viewModelScope.launch {
            try {
                val requestResponse = withContext(Dispatchers.IO) { action() }
                success(requestResponse)
            } catch (exception: Exception) {
                handleError(exception.message, errorType)
            } finally {
                if (progressPriority != ProgressAction.NONE)
                    _progressHandler.value = ProgressAction.DISMISS
            }
        }
    }

    private fun handleError(message: String?, errorType: ErrorType) {
        _errorMessage.postValue(ErrorDataModel(message ?: "", errorType))
    }
}