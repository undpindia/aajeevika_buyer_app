package com.aajeevika.buyer.view.application.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.model.data_model.NotificationModel
import com.aajeevika.buyer.utility.KEY_PAGE
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction

class NotificationsViewModel : BaseViewModel() {
    private val _notification = MutableLiveData<NotificationModel>()
    val notification : LiveData<NotificationModel> = _notification

    fun requestNotifications(page: Int = 1) {
        val map = HashMap<String, Any>()
        map[KEY_PAGE] = page

        requestData(
            {apiService.getNotification(map)},
            {it.data?.let { result->_notification.postValue(result) }  },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE)
    }

}