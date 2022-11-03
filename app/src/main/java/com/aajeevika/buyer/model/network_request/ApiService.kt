package com.aajeevika.buyer.model.network_request

import com.aajeevika.buyer.model.data_model.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("api/get_countries")
    suspend fun getCountries(): Response<BaseDataModel<CountryModel>>

    @FormUrlEncoded
    @POST("api/get_state")
    suspend fun getState(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<StateModel>>

    @FormUrlEncoded
    @POST("api/get_city")
    suspend fun getCity(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<CityModel>>

    @FormUrlEncoded
    @POST("api/registration")
    suspend fun registerUser(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<OtpModel>>

    @FormUrlEncoded
    @POST("api/login")
    suspend fun loginUser(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<UserProfileModel>>

    @FormUrlEncoded
    @POST("api/verifyotp")
    suspend fun verifyOtp(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<UserProfileModel>>

    @FormUrlEncoded
    @POST("api/resendotp")
    suspend fun resendOtp(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<OtpModel>>

    @FormUrlEncoded
    @POST("api/forgetpassword")
    suspend fun forgotPassword(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<OtpModel>>

    @FormUrlEncoded
    @POST("api/changepassword")
    suspend fun changePassword(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @GET("api/getprofile")
    suspend fun getProfile(): Response<BaseDataModel<UserProfileModel>>

    @FormUrlEncoded
    @POST("api/updateuserprofile")
    suspend  fun updateUserProfile(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/updateaddress")
    suspend fun updateAddress(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/addaddress")
    suspend fun addAddress(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/updatemobile")
    suspend fun updateMobile(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<OtpModel>>

    @GET("api/getcategory")
    suspend fun getCategories(): Response<BaseDataModel<CategoryModel>>

    @GET("api/getuserhome")
    suspend fun getUserHome(): Response<BaseDataModel<HomeModel>>

    @FormUrlEncoded
    @POST("api/getallproduct")
    suspend fun loadMoreProducts(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<DashboardArtisanListModel>>

    @GET("api/getbanner")
    suspend fun getBanner(): Response<BaseDataModel<BannerModel>>

    @FormUrlEncoded
    @POST("api/viewallpopularproduct")
    suspend fun getPopularProduct(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<PopularProductsModel>>

    @FormUrlEncoded
    @POST("api/viewallrecentlyproduct")
    suspend fun getRecentlyProduct(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<RecentlyAddedModel>>

    @FormUrlEncoded
    @POST("api/viewcategoryproduct")
    suspend fun getCategoryProducts(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<CategoryWiseProductModel>>

    @FormUrlEncoded
    @POST("api/viewsubcategoryproduct")
    suspend fun getSubCategoryProducts(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<SubCategoryWiseProductModel>>

    @FormUrlEncoded
    @POST("api/viewsartisancategoryproduct")
    suspend  fun getArtisanSubCategoryProducts(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<SubCategoryWiseProductModel>>

    @FormUrlEncoded
    @POST("api/viewproduct")
    suspend fun getProductDetails(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<ProductDetailsModel>>

    @FormUrlEncoded
    @POST("api/viewartisanshgprofile")
    suspend fun getArtisanProfile(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<ArtisanProfileModel>>

    @FormUrlEncoded
    @POST("api/viewartisanshgproduct")
    suspend fun getArtisanProducts(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<ArtisanProductListModel>>

    @FormUrlEncoded
    @POST("api/get_seller_product")
    suspend fun getSellerProducts(@Field("sellerId") id: Int, @Field("page") page: Int): Response<BaseDataModel<SellerProductListModel>>

    @FormUrlEncoded
    @POST("api/getnotification")
    suspend fun getNotification(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<NotificationModel>>

    @GET("api/getpopup")
    suspend fun getPopup(): Response<BaseDataModel<PopupModel>>

    @FormUrlEncoded
    @POST("api/search")
    suspend fun searchProduct(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<SearchResultModel>>

    @Multipart
    @POST("api/updateprofileimage")
    suspend fun uploadProfileImage(@Part file: MultipartBody.Part): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/enquiry")
    suspend fun submitInquiry(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/updatepassword")
    suspend fun updatePassword(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @POST("api/express_interest")
    suspend fun expressInterest(@Body body: JsonObject): Response<BaseDataModel<Any>>

    @POST("api/user_interest")
    suspend fun userInterest(): Response<BaseDataModel<UserInterestModel>>

    @FormUrlEncoded
    @POST("api/get_interest_byid")
    suspend fun interestDetail(@Field("interestId")interestId:Int): Response<BaseDataModel<InterestDetailModel>>

    @FormUrlEncoded
    @POST("api/order_list")
    suspend fun orderList(@Field("order_type") type: String): Response<BaseDataModel<OrderDataModel>>

    @FormUrlEncoded
    @POST("api/get_order_byid")
    suspend fun orderDetail(@Field("orderId")orderId:Int): Response<BaseDataModel<OrderDetailModel>>

    @POST("api/get_reviews")
    suspend fun getReviews(): Response<BaseDataModel<ReviewsModel>>

    @FormUrlEncoded
    @POST("api/get-seller-reviews")
    suspend fun getSellerReviews(@Field("seller_id")seller_id:Int): Response<BaseDataModel<ReviewsModel>>

    @FormUrlEncoded
    @POST("api/add-favorite")
    suspend fun addFavorite(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/get-grievance-type-list")
    suspend fun getGrievanceTypeList(@FieldMap body: HashMap<String, Any> = HashMap()): Response<BaseDataModel<GrievanceTypeDataModel>>

    @FormUrlEncoded
    @POST("api/add-grievance-ticket")
    suspend fun addGrievanceTicket(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/get-ticket-list")
    suspend fun getTicketList(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<TicketsDataModel>>

    @FormUrlEncoded
    @POST("api/get-ticket-chat-list")
    suspend fun getTicketChatList(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<GrievanceChatDataModel>>

    @FormUrlEncoded
    @POST("api/add-grievance-message")
    suspend fun sendMessage(@FieldMap body: HashMap<String, Any>): Response<BaseDataModel<Any>>

    @FormUrlEncoded
    @POST("api/addrating")
    suspend fun addRating(
        @Field("type") type:String="Buyer",
        @Field("user_id") user_id:Int,
        @Field("order_id") order_id: String,
        @Field("review_msg") review_msg:String="",
        @Field("rating") rating:Float,
    ): Response<BaseDataModel<Any>>

}