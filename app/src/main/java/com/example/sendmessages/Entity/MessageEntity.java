package com.example.sendmessages.Entity;

public class MessageEntity {

    private String message;
    private String dateTimeToDataBase;
    private String usernameFrom;
    private String uriImage;

    public MessageEntity(){

    }

    public MessageEntity(String dateTimeToDataBase, String usernameFrom, String message) {
        this.message = message;
        this.usernameFrom = usernameFrom;
        this.dateTimeToDataBase = dateTimeToDataBase;
    }

    public String getUriImage() {
        return uriImage;
    }

    public void setUriImage(String uriImage) {
        this.uriImage = uriImage;
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
