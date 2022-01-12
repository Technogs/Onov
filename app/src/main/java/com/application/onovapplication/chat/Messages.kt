package com.application.onovapplication.chat

import com.application.onovapplication.model.EventData
import com.application.onovapplication.model.FeedsData

data class Messages(
    var fileUrl: String = "",
    var feed: FeedsData? = null,
    var event: EventData? = null,
    var message: String = "",
    val message_date: String = "",

    val message_type: String = "",
    var receiver_id: String = "",
    var receiver_name: String = "",
    val sender_id: String = "",
    val sender_name: String = "",
    var timeStamp: String = ""
)
