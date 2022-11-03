package com.aajeevika.buyer.view.product.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.model.data_model.PopularProductsModel
import com.aajeevika.buyer.model.data_model.RecentlyAddedModel
import com.aajeevika.buyer.model.data_model.SubCategoryWiseProductModel
import com.aajeevika.buyer.utility.KEY_ARTISAN_ID
import com.aajeevika.buyer.utility.KEY_PAGE
import com.aajeevika.buyer.utility.KEY_SUB_CATEGORY_ID
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction

class SubCategoryProductsViewModel : BaseViewModel() {
    private val _recentProducts = MutableLiveData<RecentlyAddedModel>()
    val recentProducts: LiveData<RecentlyAddedModel> = _recentProducts

    private val _popularProducts = MutableLiveData<PopularProductsModel>()
    val popularProducts: LiveData<PopularProductsModel> = _popularProducts

    private val _subCategoryWiseProducts = MutableLiveData<SubCategoryWiseProductModel>()
    val subCategoryWiseProducts: LiveData<SubCategoryWiseProductModel> = _subCategoryWiseProducts


    fun requestPopularProductList(page: Int = 1) {
        val map = HashMap<String, Any>()
        map[KEY_PAGE] = page

        requestData(
            { apiService.getPopularProduct(map) },
            { it.data?.let { result -> _popularProducts.postValue(result) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE
        )
    }

    fun requestRecentProductList(page: Int = 1) {
        val map = HashMap<String, Any>()
        map[KEY_PAGE] = page

        requestData(
            { apiService.getRecentlyProduct(map) },
            { it.data?.let { result -> _recentProducts.postValue(result) } },
            progressAction = ProgressAction.PROGRESS_BAR,
            errorType = ErrorType.MESSAGE
        )
    }

    fun requestSubCategoryProductList(subCategoryId: Int?, page: Int = 1) {
        subCategoryId?.let { id ->
            val map = HashMap<String, Any>()
            map[KEY_SUB_CATEGORY_ID] = id
            map[KEY_PAGE] = page

            requestData(
                { apiService.getSubCategoryProducts(map) },
                { it.data?.let { result -> _subCategoryWiseProducts.postValue(result) } },
                progressAction = ProgressAction.PROGRESS_BAR,
                errorType = ErrorType.MESSAGE
            )
        }
    }

    fun requestArtisanSubCategoryProductList(artisanId: Int?, subCategoryId: Int?, page: Int = 1) {
        subCategoryId?.let { id ->
            val map = HashMap<String, Any>()
            map[KEY_ARTISAN_ID] = artisanId ?: 0
            map[KEY_SUB_CATEGORY_ID] = id
            map[KEY_PAGE] = page

            requestData(
                { apiService.getArtisanSubCategoryProducts(map) },
                { it.data?.let { result -> _subCategoryWiseProducts.postValue(result) } },
                progressAction = ProgressAction.PROGRESS_BAR,
                errorType = ErrorType.MESSAGE
            )
        }
    }
}