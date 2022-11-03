package com.aajeevika.buyer.model.data_model

import com.aajeevika.buyer.utility.app_enum.ErrorType

data class ErrorDataModel(
    val message: String,
    val errorType: ErrorType,
)