package com.example.sendmessages.Entity

data class ChatsEntity(
    var idChats: Int = 0,
    var usernameToWhom: String = "",
    var lastMessage: String = "",
    var timeMessageToDataBase: String = "",
    var lineKeys: String = ""
)
