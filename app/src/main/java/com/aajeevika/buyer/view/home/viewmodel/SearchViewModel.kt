package com.aajeevika.buyer.view.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.model.data_model.SearchResultModel
import com.aajeevika.buyer.utility.KEY_KEYWORD
import com.aajeevika.buyer.utility.KEY_PAGE

class SearchViewModel : BaseViewModel() {
    private val _searchProduct = MutableLiveData<SearchResultModel>()
    val searchResult: LiveData<SearchResultModel> = _searchProduct

    fun searchProduct(key: String, page: Int = 1) {
        val map = HashMap<String, Any>()
        map[KEY_KEYWORD] = key
        map[KEY_PAGE] = page

        requestData({ apiService.searchProduct(map) },
            { it.data?.let { result -> _searchProduct.postValue(result) } }
        )
    }
}