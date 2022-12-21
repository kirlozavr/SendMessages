package com.example.sendmessages.DTO;

public class MessageDto {

    private String message;
    private String dateTimeToMessages;
    private String usernameFrom;

    public MessageDto(String message, String dateTimeToMessages, String usernameFrom) {
        this.message = message;
        this.dateTimeToMessages = dateTimeToMessages;
        this.usernameFrom = usernameFrom;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
