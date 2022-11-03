package com.aajeevika.buyer.model.data_model

data class ReviewsModel(
    val ratings: ArrayList<ReviewData>? = arrayListOf()
)

data class ReviewData(
    val id: Int? = 0,
    val user_id: Int? = 0,
    val order_id: Int? = 0,
    val status: Int? = 0,
    val rating: Double? = 0.0,
    val review_by_user: Int? = 0,
    val review_msg: String? = "",
    val created_at: String? = "",
    val updated_at: String? = "",
    val getreviews: UserData? = UserData()
)