package com.example.sendmessages.Entity;

public class MessageEntity {

    private String message;
    private String dateTimeToDataBase;
    private String usernameFrom;

    public MessageEntity(){

    }

    public MessageEntity(String message, String dateTimeToDataBase, String usernameFrom) {
        this.message = message;
        this.usernameFrom = usernameFrom;
        this.dateTimeToDataBase = dateTimeToDataBase;
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

    public String getUsernameFrom() {
        return usernameFrom;
    }

    public void setUsernameFrom(String usernameFrom) {
        this.usernameFrom = usernameFrom;
    }
}
