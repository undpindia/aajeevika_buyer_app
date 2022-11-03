package com.aajeevika.buyer.location.model

data class SearchLocationResponse(
    val predictions: ArrayList<Predictions>,
    val status: String
) {
    data class Predictions(
        val description: String,
        val id: String,
        val place_id: String,
        val reference: String,
        val structured_formatting: StructuredFormatting
    ) {
        data class StructuredFormatting(
            val main_text: String,
            val secondary_text: String
        )
    }
}