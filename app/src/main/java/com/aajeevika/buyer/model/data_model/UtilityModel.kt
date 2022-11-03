package com.aajeevika.buyer.model.data_model

import android.os.Parcel
import android.os.Parcelable

data class HomeModel(
    var totalpage: Int? = null,
    var data: ArrayList<ProductListData>
)

data class ArtisanListModel(
    var pagination: PaginationModel,
    var data: ArrayList<ProductListData>,
)

data class DashboardArtisanListModel(
    var pagination: DashboardPaginationModel,
    var data: ArrayList<ProductListData>,
)

data class PopularProductsModel(
    var pagination: PaginationModel,
    var popularproduct: ArrayList<ProductBasicData>
)

data class RecentlyAddedModel(
    var pagination: PaginationModel,
    var recentlyproduct: ArrayList<ProductBasicData>
)

data class CategoryWiseProductModel(
    var pagination: PaginationModel,
    var products: ArrayList<CategoryWiseProductDataList>
)

data class SubCategoryWiseProductModel(
    var pagination: PaginationModel,
    var products: ArrayList<ProductBasicData>
)

data class CategoryWiseProductDataList(
    var categoryname: String? = null,
    var subcategory: String? = null,
    var subcategoryId: Int? = null,
    var product: ArrayList<ProductBasicData>? = null,
)

data class ProductListData(
    var id: Int? = null,
    var type: String? = null,
    var name: String? = null,
    var title: String? = null,
    var data: ArrayList<ProductBasicData>? = null,
)

data class TemplateDataModel(
    var id: Int? = null,
    var name: String? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TemplateDataModel> {
        override fun createFromParcel(parcel: Parcel): TemplateDataModel {
            return TemplateDataModel(parcel)
        }

        override fun newArray(size: Int): Array<TemplateDataModel?> {
            return arrayOfNulls(size)
        }
    }
}

data class ProductBasicData(
    var id: Int? = null,
    var price: String? = null,
    var name: String? = null,
    var template_id: String? = null,
    var template: TemplateDataModel? = null,
    var image_1: String? = null,
    var email: String? = null,
    var mobile: String? = null,
    var profileImage: String? = null,
    var seller_name: String? = "",
    var qty: Int = 0,
    var selectedQuantity: Int = 0,
    var orderPrice: Int = 0,
    var price_unit: String? = null,
    val product_id_d: String?,
    val rating: RattingData? = RattingData(),
    val user: UserData? = UserData()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(TemplateDataModel::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(price)
        parcel.writeString(name)
        parcel.writeString(template_id)
        parcel.writeParcelable(template, flags)
        parcel.writeString(image_1)
        parcel.writeString(email)
        parcel.writeString(mobile)
        parcel.writeString(profileImage)
        parcel.writeString(seller_name)
        parcel.writeInt(qty)
        parcel.writeInt(selectedQuantity)
        parcel.writeInt(orderPrice)
        parcel.writeString(price_unit)
        parcel.writeString(product_id_d)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductBasicData> {
        override fun createFromParcel(parcel: Parcel): ProductBasicData {
            return ProductBasicData(parcel)
        }

        override fun newArray(size: Int): Array<ProductBasicData?> {
            return arrayOfNulls(size)
        }
    }
}

data class CountryModel(
    var country: ArrayList<CountryData>? = null
)

data class CountryData(
    var id: Int? = null,
    var sortname: String? = null,
    var name: String? = null,
    var phonecode: Int? = null
)

data class StateModel(
    var states: ArrayList<StateData>? = null
)

data class StateData(
    var id: Int? = null,
    var name: String? = null,
    var country_id: Int? = null
)

data class CityModel(
    var district: ArrayList<CityData>
)

data class CityData(
    var id: Int? = null,
    var name: String? = null,
    var state_id: Int? = null
)

data class CategoryModel(
    var category: ArrayList<CategoryData>? = null
)

data class CategoryData(
    var id: Int? = null,
    var name: String? = null,
    var image: String? = null
)

data class ProductDetailsModel(
    var productdetail: ProductDetails? = null
)

data class ProductDetails(
    var id: Int? = null,
    var price: String? = null,
    var user_id: String? = null,
    var material_id: String? = null,
    var SubCatName: String? = null,
    var catName: String? = null,
    var categoryId: Int? = null,
    var subcategoryId: Int? = null,
    var qty: String? = null,
    var price_unit: String? = null,
    var length: String? = null,
    var width: String? = null,
    var height: String? = null,
    var vol: String? = null,
    var weight: String? = null,
    var length_unit: String? = null,
    var width_unit: String? = null,
    var height_unit: String? = null,
    var weight_unit: String? = null,
    var product_id_d: String? = null,
    var vol_unit: String? = null,
    var image_1: String? = null,
    var image_2: String? = null,
    var image_3: String? = null,
    var image_4: String? = null,
    var image_5: String? = null,
    var video_url: String? = null,
    var name: String? = null,
    var localname_en: String? = null,
    var template_name: String? = null,
    var description: String? = null,
    var des_kn: String? = null,
    var materialname: String? = null,
    var artisanid: Int? = null,
    var artisanshgname: String? = null,
    var artisanshgtitle: String? = null,
    var certificate_data: ProductCertificateData? = null,
)

data class ProductCertificateData(
    var certificate_image_1: String?,
    var certificate_status_1: Int?,
    var certificate_type_1: Int?,
    var certificate_type_name_1: String?,
    var certificate_image_2: String?,
    var certificate_status_2: Int?,
    var certificate_type_2: Int?,
    var certificate_type_name_2: String?,
    var certificate_image_3: String?,
    var certificate_status_3: Int?,
    var certificate_type_3: Int?,
    var certificate_type_name_3: String?,
    var certificate_image_4: String?,
    var certificate_status_4: Int?,
    var certificate_type_4: Int?,
    var certificate_type_name_4: String?,
    var certificate_image_5: String?,
    var certificate_status_5: Int?,
    var certificate_type_5: Int?,
    var certificate_type_name_5: String?,
    var certificate_image_6: String?,
    var certificate_status_6: Int?,
    var certificate_type_6: Int?,
    var certificate_type_name_6: String?,
    var certificate_image_7: String?,
    var certificate_status_7: Int?,
    var certificate_type_7: Int?,
    var certificate_type_name_7: String?,
)

data class PopupModel(
    var popup: PopupData? = null
)

data class PopupData(
    var id: Int? = null,
    var background_image: String? = null,
)

data class BannerModel(
    var banner: ArrayList<BannerData>? = null
)

data class BannerData(
    var id: Int? = null,
    var image: String? = null,
    var action: String? = null
)

data class NotificationModel(
    var pagination: NotificationPaginationModel,
    var getnotification: ArrayList<NotificationData>,
)

data class NotificationData(
    var id: Int? = null,
    var title: String? = null,
    var body: String? = null,
    var image: String?,
    var created_at: String?,
)

data class ArtisanProductListModel(
    var pagination: PaginationModel? = null,
    var products: ArrayList<CategorizedProductList>? = null
)


data class SellerProductListModel(
    var pagination: PaginationModel,
    var seller_data: ArrayList<ProductBasicData>,
)


data class CategorizedProductList(
    var categoryId: Int? = null,
    var categoryName: String? = null,
    var subCategories: ArrayList<SubCategorizedProductList>? = null,
)

data class SubCategorizedProductList(
    var subCategoryId: Int? = null,
    var subCategoryName: String? = null,
    var products: ArrayList<ProductBasicData>? = null,
)

data class PaginationModel(
    var current_page: Int,
    var last_page: Int,
)

data class NotificationPaginationModel(
    var current_page: Int,
    var last_page: Int,
)

data class DashboardPaginationModel(
    var currentPage: Int,
    var last_page: Int,
)

data class SearchResultModel(
    val pagination: PaginationModel,
    var searchresult: ArrayList<SearchModel>,
)

data class SearchModel(
    var type: String,
    var data: ArrayList<SearchDataModel>,
)

data class SearchDataModel(
    var type: String,
    var productId: Int,
    var productName: String,
    var price: String,
    var image_1: String,
    val product_id_d: String,
    var template: TemplateDataModel,

    var artisanshgId: Int? = null,
    var artisanshgName: String? = null,
    var profileImage: String? = null,

    var catId: Int? = null,
    var catName: String? = null,
    var catImage: String? = null,
)

data class RattingData(
    val reviewCount: Long? = 0,
    var ratingAvgStar: Double? = 0.0)