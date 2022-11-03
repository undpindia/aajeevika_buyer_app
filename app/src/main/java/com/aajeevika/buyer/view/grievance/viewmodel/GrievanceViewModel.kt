package com.aajeevika.buyer.view.grievance.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.model.data_model.TicketData
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction

class GrievanceViewModel : BaseViewModel() {
    private val _newGrievanceLiveData = MutableLiveData<ArrayList<TicketData>>()
    val newGrievanceLiveData: LiveData<ArrayList<TicketData>> = _newGrievanceLiveData

    fun getGrievanceTypeList(userId: Int) {
        val requestMap = HashMap<String, Any>()
        requestMap["user_id"] = userId

        requestData(
            { apiService.getTicketList(requestMap) },
            { it.data?.ticket_list?.run { _newGrievanceLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE,
        )
    }
}