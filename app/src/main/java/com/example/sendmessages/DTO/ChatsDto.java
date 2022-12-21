package com.example.sendmessages.DTO;

public class ChatsDto {

    private String usernameToWhom, lastMessage;

    public ChatsDto(String usernameToWhom, String lastMessage) {
        this.usernameToWhom = usernameToWhom;
        this.lastMessage = lastMessage;
    }

    public String getUsernameToWhom() {
        return usernameToWhom;
    }

    public void setUsernameToWhom(String usernameToWhom) {
        this.usernameToWhom = usernameToWhom;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
