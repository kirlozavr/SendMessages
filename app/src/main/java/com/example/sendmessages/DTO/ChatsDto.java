package com.example.sendmessages.DTO;

public class ChatsDto {

    private String usernameToWhom, lastMessage, timeMessageToChats;

    public ChatsDto(
            String usernameToWhom,
            String lastMessage,
            String timeMessageToChats
    ) {
        this.usernameToWhom = usernameToWhom;
        this.lastMessage = lastMessage;
        this.timeMessageToChats = timeMessageToChats;
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

    public String getTimeMessageToChats() {
        return timeMessageToChats;
    }

    public void setTimeMessageToChats(String timeMessageToChats) {
        this.timeMessageToChats = timeMessageToChats;
    }
}
