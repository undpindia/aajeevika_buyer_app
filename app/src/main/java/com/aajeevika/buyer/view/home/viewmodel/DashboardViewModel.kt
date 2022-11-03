package com.aajeevika.buyer.view.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.aajeevika.buyer.baseclasses.BaseViewModel
import com.aajeevika.buyer.location.model.PlaceAddressResult
import com.aajeevika.buyer.model.data_model.*
import com.aajeevika.buyer.utility.*
import com.aajeevika.buyer.utility.app_enum.ErrorType
import com.aajeevika.buyer.utility.app_enum.ProgressAction

class DashboardViewModel : BaseViewModel() {

    private val _categories = MutableLiveData<CategoryModel>()
    val categories: LiveData<CategoryModel> = _categories

    private val _homeData = MutableLiveData<HomeModel>()
    val homeData: LiveData<HomeModel> = _homeData

    private val _recentProducts = MutableLiveData<RecentlyAddedModel>()
    val recentProducts: LiveData<RecentlyAddedModel> = _recentProducts

    private val _popularProducts = MutableLiveData<PopularProductsModel>()
    val popularProducts: LiveData<PopularProductsModel> = _popularProducts

    private val _productDetails = MutableLiveData<ProductDetailsModel>()
    val productDetails: LiveData<ProductDetailsModel> = _productDetails

    private val _artisanProfile = MutableLiveData<ArtisanProfileModel>()
    val artisanProfile: LiveData<ArtisanProfileModel> = _artisanProfile

    private val _artisanProduct = MutableLiveData<ArtisanProductListModel>()
    val artisanProduct: LiveData<ArtisanProductListModel> = _artisanProduct

    private val _artisanHomeProductList = MutableLiveData<DashboardArtisanListModel>()
    val artisanHomeProductList: LiveData<DashboardArtisanListModel> = _artisanHomeProductList

    private val _bannerList = MutableLiveData<BannerModel>()
    val bannerList: LiveData<BannerModel> = _bannerList

    private val _address = MutableLiveData<ArrayList<PlaceAddressResult>>()
    val address: LiveData<ArrayList<PlaceAddressResult>> = _address

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
            progressAction = ProgressAction.PROGRESS_DIALOG,
            errorType = ErrorType.MESSAGE
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

    fun requestProductDetails(productId: Int?) {
        productId?.let { id ->
            val map = HashMap<String, Any>()
            map[KEY_PRODUCT_ID] = id

            requestData(
                { apiService.getProductDetails(map) },
                { it.data?.let { result -> _productDetails.postValue(result) } })
        }
    }

    fun requestLoadMoreProducts(artisanIds: List<Int?>?, page: Int = 1) {
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
    }

    fun getAddressFromLatLng(map: HashMap<String, String>) {
        requestPlaceData({ mMapApiService.getAddressFromLatLng(map) }, {
            _address.value = it.results
        })
    }

}