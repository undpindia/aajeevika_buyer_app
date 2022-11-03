package com.aajeevika.buyer.view.order.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.model.data_model.OrderData
import com.aajeevika.buyer.model.data_model.UserInterestData
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.MyOrderType
import com.aajeevika.buyer.utility.app_enum.ProgressAction

class MyOrderViewModel : BaseViewModel() {

    private val _orderList = MutableLiveData<ArrayList<OrderData>>()
    val orderList : LiveData<ArrayList<OrderData>> = _orderList

    fun orderList(myOrderType: MyOrderType) {
        requestData(
            { apiService.orderList(myOrderType.type) },
            { it.data?.order_list?.let { result->_orderList.postValue(result) }},
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE
        )
    }
}

