package com.aajeevika.buyer.view.order.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.model.data_model.OrderData
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction

class OrderDetailsViewModel : BaseViewModel() {
    private val _orderDetail = MutableLiveData<ArrayList<OrderData>>()
    val orderDetail: LiveData<ArrayList<OrderData>> = _orderDetail

    fun orderDetail(id: Int) {
        requestData(
            { apiService.orderDetail(id) },
            { it.data?.all_order?.let { result -> _orderDetail.postValue(result) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG
        )
    }

    fun addRating(
        order_id: String,
        id: Int,
        rating: Float,
        reviewMsg: String,
        success: (msg: String) -> Unit
    ) {
        requestData(
            {
                apiService.addRating(
                    user_id = id,
                    rating = rating,
                    review_msg = reviewMsg,
                    order_id = order_id
                )
            },
            { success(it.message ?: "") },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG
        )
    }
}