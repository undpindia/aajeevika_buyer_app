package com.aajeevika.buyer.model.data_model

data class TicketsDataModel(
    val ticket_list: ArrayList<TicketData>,
)

data class TicketData(
    val id: Int,
    val ticket_id: String,
    val message: String,
    val status: Int,
    val created_at: String,
)