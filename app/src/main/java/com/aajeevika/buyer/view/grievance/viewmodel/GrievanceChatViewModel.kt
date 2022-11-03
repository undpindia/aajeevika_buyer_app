package com.aajeevika.buyer.view.grievance.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.buyer.model.data_model.ChatData
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction

class GrievanceChatViewModel : BaseViewModel() {
    private val _grievanceLiveData = MutableLiveData<ChatData>()
    val grievanceLiveData: LiveData<ChatData> = _grievanceLiveData

    private val _messageLiveData = MutableLiveData<String>()
    val messageLiveData: LiveData<String> = _messageLiveData

    fun getGrievanceTypeList(userId: Int, grievanceId: Int) {
        val requestMap = HashMap<String, Any>()
        requestMap["user_id"] = userId
        requestMap["grievance_id"] = grievanceId

        requestData(
            { apiService.getTicketChatList(requestMap) },
            { it.data?.ticket_chat_list?.get(0)?.run { _grievanceLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }

    fun sendMessage(grievanceId: Int, message: String) {
        val requestMap = HashMap<String, Any>()
        requestMap["message"] = message
        requestMap["grievance_id"] = grievanceId

        requestData(
            { apiService.sendMessage(requestMap) },
            { it.message?.run { _messageLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.TOAST,
        )
    }
}