package com.aajeevika.buyer.view.grievance.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.model.data_model.GrievanceData
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction

class CreateGrievanceViewModel : BaseViewModel() {
    private val _grievanceListLiveData = MutableLiveData<ArrayList<GrievanceData>>()
    val grievanceListLiveData: LiveData<ArrayList<GrievanceData>> = _grievanceListLiveData

    private val _newGrievanceLiveData = MutableLiveData<String>()
    val newGrievanceLiveData: LiveData<String> = _newGrievanceLiveData

    fun getGrievanceTypeList() {
        requestData(
            { apiService.getGrievanceTypeList() },
            { it.data?.grievance_list?.run { _grievanceListLiveData.postValue(this) } },
            progressAction = ProgressAction.NONE,
            errorType = ErrorType.TOAST,
        )
    }

    fun addGrievanceTicket(userId: Int, issueId: Int, message: String) {
        val requestMap = HashMap<String, Any>()
        requestMap["user_id"] = userId
        requestMap["issue_type_id"] = issueId
        requestMap["concern"] = message

        requestData(
            { apiService.addGrievanceTicket(requestMap) },
            { it.message?.run { _newGrievanceLiveData.postValue(this) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG,
        )
    }
}