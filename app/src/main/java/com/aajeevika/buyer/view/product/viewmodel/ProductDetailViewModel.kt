package com.aajeevika.buyer.view.product.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.model.data_model.ProductDetailsModel
import com.aajeevika.buyer.utility.KEY_PRODUCT_ID
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction

class ProductDetailViewModel : BaseViewModel() {
    private val _productDetails = MutableLiveData<ProductDetailsModel>()
    val productDetails: LiveData<ProductDetailsModel> = _productDetails

    fun requestProductDetails(productId: Int?) {
        productId?.let { id ->
            val map = HashMap<String, Any>()
            map[KEY_PRODUCT_ID] = id

            requestData(
                { apiService.getProductDetails(map) },
                { it.data?.let {result-> _productDetails.postValue(result) } },
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.DIALOG)
        }
    }


}