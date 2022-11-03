package com.aajeevika.buyer.model.data_model

data class BaseDataModel<T>(
    var status: Boolean = false,
    var statusCode: Int? = null,
    var message: String? = null,
    var data: T? = null
)