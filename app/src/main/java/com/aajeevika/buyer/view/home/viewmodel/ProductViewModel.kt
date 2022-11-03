package com.aajeevika.buyer.view.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.model.data_model.*
import com.aajeevika.buyer.model.network_request.ApiService
import com.aajeevika.buyer.utility.*
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction

class ProductViewModel() : BaseViewModel() {
    private val _categories = MutableLiveData<CategoryModel>()
    val categories: LiveData<CategoryModel> = _categories

    private val _homeData = MutableLiveData<HomeModel>()
    val homeData: LiveData<HomeModel> = _homeData

    private val _recentProducts = MutableLiveData<RecentlyAddedModel>()
    val recentProducts: LiveData<RecentlyAddedModel> = _recentProducts

    private val _popularProducts = MutableLiveData<PopularProductsModel>()
    val popularProducts: LiveData<PopularProductsModel> = _popularProducts

    private val _categoryWiseProducts = MutableLiveData<CategoryWiseProductModel>()
    val categoryWiseProducts: LiveData<CategoryWiseProductModel> = _categoryWiseProducts

    private val _subCategoryWiseProducts = MutableLiveData<SubCategoryWiseProductModel>()
    val subCategoryWiseProducts: LiveData<SubCategoryWiseProductModel> = _subCategoryWiseProducts

    private val _productDetails = MutableLiveData<ProductDetailsModel>()
    val productDetails: LiveData<ProductDetailsModel> = _productDetails

    private val _artisanProfile = MutableLiveData<ArtisanProfileModel>()
    val artisanProfile: LiveData<ArtisanProfileModel> = _artisanProfile

    private val _artisanProduct = MutableLiveData<ArtisanProductListModel>()
    val artisanProduct: LiveData<ArtisanProductListModel> = _artisanProduct

    private val _artisanHomeProductList = MutableLiveData<ArtisanListModel>()
    val artisanHomeProductList: LiveData<ArtisanListModel> = _artisanHomeProductList

    private val _bannerList = MutableLiveData<BannerModel>()
    val bannerList: LiveData<BannerModel> = _bannerList

    private val _searchProduct = MutableLiveData<SearchResultModel>()
    val searchResult: LiveData<ArrayList<SearchModel>> = Transformations.map(_searchProduct) {
        val dataList: ArrayList<SearchModel> = ArrayList()
        it.searchresult?.forEach { model ->
            model.data?.let { data ->
                if (data.isNotEmpty()) dataList.add(model)
            }
        }

        dataList
    }

    fun requestCategories() {
        requestData(
            { apiService.getCategories() },
            { it.data?.let { result -> _categories.postValue(result) } },
        )
    }

    fun requestHomeData() {
        requestData(
            { apiService.getUserHome() },
            { it.data?.let { result -> _homeData.postValue(result) } },
            errorType = ErrorType.NONE
        )
    }

    fun requestBannerList() {
        requestData(
            { apiService.getBanner() },
            { it.data?.let { result -> _bannerList.postValue(result) } },
            progressAction = ProgressAction.NONE,
            errorType = ErrorType.NONE
        )
    }

    fun requestPopularProductList(page: Int) {
        val map = HashMap<String, Any>()
        map[KEY_PAGE] = page

        requestData(
            { apiService.getPopularProduct(map) },
            { it.data?.let { result -> _popularProducts.postValue(result) } },
            progressAction = ProgressAction.NONE,
            errorType = ErrorType.NONE
        )
    }

    fun requestRecentProductList(page: Int) {
        val map = HashMap<String, Any>()
        map[KEY_PAGE] = page

        requestData(
            { apiService.getRecentlyProduct(map) },
            { it.data?.let { result -> _recentProducts.postValue(result) } },
            progressAction = ProgressAction.NONE,
            errorType = ErrorType.NONE
        )
    }


    fun requestSubCategoryProductList(subCategoryId: Int?, page: Int) {
        subCategoryId?.let { id ->
            val map = HashMap<String, Any>()
            map[KEY_SUB_CATEGORY_ID] = id
            map[KEY_PAGE] = page

            requestData(
                { apiService.getSubCategoryProducts(map) },
                { it.data?.let { result -> _subCategoryWiseProducts.postValue(result) } },
                progressAction = ProgressAction.NONE,
                errorType = ErrorType.NONE
            )
        }
    }

    fun requestArtisanSubCategoryProductList(artisanId: Int?, subCategoryId: Int?, page: Int) {
        subCategoryId?.let { id ->
            val map = HashMap<String, Any>()
            map[KEY_ARTISAN_ID] = artisanId ?: 0
            map[KEY_SUB_CATEGORY_ID] = id
            map[KEY_PAGE] = page

            requestData(
                { apiService.getArtisanSubCategoryProducts(map) },
                { it.data?.let { result -> _subCategoryWiseProducts.postValue(result) } },
                progressAction = ProgressAction.NONE,
                errorType = ErrorType.NONE
            )
        }
    }

    fun requestProductDetails(productId: Int?) {
        productId?.let { id ->
            val map = HashMap<String, Any>()
            map[KEY_PRODUCT_ID] = id

            requestData(
                { apiService.getProductDetails(map) },
                { it.data?.let { result -> _productDetails.postValue(result) } })
        }
    }

    fun searchProduct(key: String) {
        val map = HashMap<String, Any>()
        map[KEY_KEYWORD] = key
        map[KEY_PAGE] = 1

        requestData({ apiService.searchProduct(map) },
            { it.data?.let { result -> _searchProduct.postValue(result) } }
        )
    }

    /*fun requestLoadMoreProducts(page: Int, artisanIds: List<Int?>?) {
        val map = HashMap<String, Any>()
        map[KEY_PAGE] = page

        requestData(
            { apiService.loadMoreProducts(map) },
            {
                artisanIds?.let { ids ->
                    it.data?.data?.filter { data -> !ids.contains(data.id) }?.let { list ->
                        val arrayList = ArrayList<ProductListData>()
                        arrayList.addAll(list)
                        it.data?.data = arrayList
                    }
                }

                it.data?.let { result -> _artisanHomeProductList.postValue(result) }
            },
            progressAction = ProgressAction.NONE,
            errorType = ErrorType.NONE
        )
    }*/

    fun requestArtisanProfile(id: Int) {
        val map = HashMap<String, Any>()
        map[KEY_ARTISAN_ID] = id

        requestData(
            { apiService.getArtisanProfile(map) },
            { it.data?.let { result -> _artisanProfile.postValue(result) } },
            progressAction = ProgressAction.NONE,
            errorType = ErrorType.NONE
        )
    }

    fun requestArtisanProducts(id: Int, page: Int) {
        val map = HashMap<String, Any>()
        map[KEY_ARTISAN_ID] = id
        map[KEY_PAGE] = page

        requestData(
            { apiService.getArtisanProducts(map) },
            { it.data?.let { result -> _artisanProduct.postValue(result) } },
            progressAction = ProgressAction.NONE,
            errorType = ErrorType.NONE
        )
    }
}