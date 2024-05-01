package com.example.sendmessages.Entity

data class ChatsEntity(
    var chatId: Int = 0,
    var usernameToWhom: String = "",
    var lastMessage: String = "",
    var timeMessage: String = "",
    var lineKeys: String = ""
)
