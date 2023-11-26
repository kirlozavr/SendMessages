package com.example.sendmessages.Entity

data class MessageEntity(
    var message: String = "",
    var dateTimeToDataBase: String = "",
    var usernameFrom: String = "",
    var uriImage: String? = null
)
