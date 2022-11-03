package com.aajeevika.buyer.view.interest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.model.data_model.ArtisanProductListModel
import com.aajeevika.buyer.model.data_model.SellerProductListModel
import com.aajeevika.buyer.utility.KEY_ARTISAN_ID
import com.aajeevika.buyer.utility.KEY_PAGE
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction

class AddInterestProductsViewModel : BaseViewModel() {
    private val _artisanProduct = MutableLiveData<SellerProductListModel>()
    val artisanProduct : LiveData<SellerProductListModel> = _artisanProduct


    fun requestArtisanProducts(id:Int, page: Int=1) {
        requestData(
            { apiService.getSellerProducts(id, page) },
            { it.data?.let { result -> _artisanProduct.postValue(result) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE
        )
    }

}