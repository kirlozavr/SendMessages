package com.example.sendmessages.Entity

data class MessageEntity(
    var message: String = "",
    var timeMessage: String = "",
    var usernameFrom: String = "",
    var urlImage: String? = null
)
