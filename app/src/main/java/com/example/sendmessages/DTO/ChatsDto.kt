package com.example.sendmessages.DTO

data class ChatsDto(
    val idChats: Int = (Math.random() * 10000000).toInt(),
    val usernameToWhom: String,
    var lastMessage: String? = null,
    var timeMessageToDataBase: String? = null,
    var lineKeys: String? = null
)