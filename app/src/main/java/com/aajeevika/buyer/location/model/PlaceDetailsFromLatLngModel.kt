package com.aajeevika.buyer.location.model

data class PlaceDetailsFromLatLngModel(
    var results: ArrayList<PlaceAddressResult> = ArrayList()
)

data class PlaceAddressResult(
    var formatted_address: String = "",
    var place_id: String = "",
    var placeName: String = ""
)
