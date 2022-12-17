package com.example.sendmessages.Entity;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class MessageEntity {

    private String message;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm VV");
    private String dateTime;

    public MessageEntity(String message, ZonedDateTime date) {
        this.message = message;
        this.dateTime = dateTimeFormatter.format(date);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
