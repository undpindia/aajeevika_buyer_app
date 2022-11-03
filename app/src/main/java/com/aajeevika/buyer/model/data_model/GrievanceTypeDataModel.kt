package com.aajeevika.buyer.model.data_model

data class GrievanceTypeDataModel(
    val grievance_list: ArrayList<GrievanceData>
)

data class GrievanceData(
    val id:Int,
    val name: String,
)