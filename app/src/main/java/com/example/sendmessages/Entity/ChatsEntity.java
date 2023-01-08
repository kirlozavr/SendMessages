package com.example.sendmessages.Entity;

public class ChatsEntity {

    private Integer idChats;
    private String usernameToWhom, lastMessage;
    private String timeMessageToDataBase;

    public ChatsEntity() {

    }

    public ChatsEntity(Integer idChats, String usernameToWhom) {
        this.idChats = idChats;
        this.usernameToWhom = usernameToWhom;
    }

    public ChatsEntity(String usernameToWhom) {
        this.idChats = (int) ((Math.random() * 10000000));
        this.usernameToWhom = usernameToWhom;
    }

    public ChatsEntity(
            Integer idChats,
            String usernameToWhom,
            String lastMessage,
            String timeMessageToDataBase
    ) {
        this.idChats = idChats;
        this.usernameToWhom = usernameToWhom;
        this.lastMessage = lastMessage;
        this.timeMessageToDataBase = timeMessageToDataBase;
    }

    public Integer getIdChats() {
        return idChats;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getUsernameToWhom() {
        return usernameToWhom;
    }

    public void setUsernameToWhom(String usernameToWhom) {
        this.usernameToWhom = usernameToWhom;
    }

    public String getTimeMessageToDataBase() {
        return timeMessageToDataBase;
    }

    public void setTimeMessageToDataBase(String timeMessageToDataBase) {
        this.timeMessageToDataBase = timeMessageToDataBase;
    }
}
