package com.aajeevika.buyer.view.product.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.model.data_model.CategoryModel
import com.aajeevika.buyer.model.data_model.CategoryWiseProductModel
import com.aajeevika.buyer.utility.KEY_CATEGORY_ID
import com.aajeevika.buyer.utility.KEY_PAGE
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction

class CategoryViewModel : BaseViewModel() {

    private val _categories = MutableLiveData<CategoryModel>()
    val categories: LiveData<CategoryModel> = _categories

    private val _categoryWiseProducts = MutableLiveData<CategoryWiseProductModel>()
    val categoryWiseProducts: LiveData<CategoryWiseProductModel> = _categoryWiseProducts

    fun requestCategories() {
        requestData(
            { apiService.getCategories() },
            { it.data?.let { result -> _categories.postValue(result) } },
        )
    }

    fun requestCategoryProductList(categoryId: Int?, page: Int = 1) {
        categoryId?.let { id ->
            val map = HashMap<String, Any>()
            map[KEY_CATEGORY_ID] = id
            map[KEY_PAGE] = page

            requestData(
                { apiService.getCategoryProducts(map) },
                { it.data?.let { result -> _categoryWiseProducts.postValue(result) } },
                progressAction = ProgressAction.PROGRESS_BAR,
                errorType = ErrorType.MESSAGE
            )
        }
    }


}