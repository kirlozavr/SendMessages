package com.example.sendmessages.Entity;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class MessageEntity {

    private String message;
    private DateTimeFormatter dateTimeFormatterToDataBase = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm:ss:SSSS OOOO");
    private DateTimeFormatter dateTimeFormatterToMessages = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm");
    private String dateTimeToDataBase;
    private String dateTimeToMessages;
    private String usernameFrom;

    public MessageEntity(){

    }

    public MessageEntity(String message, ZonedDateTime date, String usernameFrom) {
        this.message = message;
        this.usernameFrom = usernameFrom;
        this.dateTimeToDataBase = dateTimeFormatterToDataBase.format(date);
        this.dateTimeToMessages = dateTimeFormatterToMessages.format(date);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTimeToDataBase() {
        return dateTimeToDataBase;
    }

    public void setDateTimeToDataBase(String dateTimeToDataBase) {
        this.dateTimeToDataBase = dateTimeToDataBase;
    }

    public String getDateTimeToMessages() {
        return dateTimeToMessages;
    }

    public void setDateTimeToMessages(String dateTimeToMessages) {
        this.dateTimeToMessages = dateTimeToMessages;
    }

    public String getUsernameFrom() {
        return usernameFrom;
    }

    public void setUsernameFrom(String usernameFrom) {
        this.usernameFrom = usernameFrom;
    }
}
