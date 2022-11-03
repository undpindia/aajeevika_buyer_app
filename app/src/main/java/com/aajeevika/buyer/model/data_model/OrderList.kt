package com.aajeevika.buyer.model.data_model

data class OrderDataModel(
    val order_list: ArrayList<OrderData>? = arrayListOf()
)


data class OrderDetailModel(
    val all_order: ArrayList<OrderData>? = arrayListOf()
)


data class OrderData(
    val id: Int = 0,
    val seller_id: Int = 0,
    val order_status: String,
    val interest_Id: String = "",
    val total_amount: String = "0",
    val user_id: String = "",
    val order_id_d: String = "",
    val otp: String? = "",
    val message: String? = "",
    val created_at: String = "",
    var updated_at: String = "",
    var image: String = "",
    val order_id: String = "",
    val order_Id: String = "",
    val seller: UserData? = UserData(),
    val buyer: UserData? = UserData(),
    val interest: Interest? = null,
    val buyer_rating: BuyerRating? = null,
    val seller_rating: BuyerRating? = null,
    val items: ArrayList<InterestProduct>? = arrayListOf()
)


data class Interest(
    val id: String? = null,
    val interest_Id: String? = null,
)
data class BuyerRating(
    val id: Int,
    val order_id: Int? = null,
    val rating: Float? = null,
    val review_msg: String? = null,
    val type: String? = ""
)


