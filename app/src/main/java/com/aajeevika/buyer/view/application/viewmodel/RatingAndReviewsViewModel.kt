package com.aajeevika.buyer.view.application.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.model.data_model.OrderData
import com.aajeevika.buyer.model.data_model.ReviewData
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction

class RatingAndReviewsViewModel : BaseViewModel() {

    private val _reviews = MutableLiveData<ArrayList<ReviewData>>()
    val reviews : LiveData<ArrayList<ReviewData>> = _reviews

    fun getReviews(id:Int=0) {
        requestData(
            { if(id == 0)apiService.getReviews() else apiService.getSellerReviews(id) },
            { it.data?.ratings?.let { result->_reviews.postValue(result) }},
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE
        )
    }
}