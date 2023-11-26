package com.example.sendmessages.DTO

data class MessageDto(
    val message: String,
    val dateTimeToDataBase: String,
    val usernameFrom: String,
    var uriImage: String? = null
)
