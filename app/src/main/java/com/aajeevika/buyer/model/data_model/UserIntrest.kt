package com.aajeevika.buyer.model.data_model

data class UserInterestModel(
    val user_interest: ArrayList<UserInterestData>? = arrayListOf()
)


data class InterestDetailModel(
    val seller_interest: ArrayList<UserInterestData>? = arrayListOf()
)

data class UserInterestData(
    val id: Int = 0,
    val seller_id: Int = 0,
    val interest_Id: String = "",
    val user_id: String = "",
    val otp: String = "",
    val message: String = "",
    val created_at: String = "",
    var updated_at: String = "",
    var image: String = "",
    val seller: UserData? = UserData(),
    val buyer: UserData? = UserData(),
    val items: ArrayList<InterestProduct>? = arrayListOf()
)

data class InterestProduct(
    val id: Int = 0,
    val express_id: Int = 0,
    val quantity: Int = 0,
    val product_price: Int = 0,
    val product_id: String = "",
    val created_at: String = "",
    val updated_at: String = "",
    val product:ProductDetails? = ProductDetails()
)