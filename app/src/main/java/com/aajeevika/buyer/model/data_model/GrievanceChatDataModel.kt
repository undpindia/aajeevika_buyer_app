package com.aajeevika.buyer.model.data_model

data class GrievanceChatDataModel(
    val ticket_chat_list: ArrayList<ChatData>
)

data class ChatData(
    val message: String,
    val status: Int,
    val get_issue: IssueData,
    val get_message: ArrayList<MessageData>,
)

data class IssueData(
    val name: String,
)

data class MessageData(
    val message: String,
    val type: String,
    val created_at: String,
)