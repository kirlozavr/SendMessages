package com.example.sendmessages.Entity;

public class ChatsEntity {
    private Integer idChats;
    private String usernameFrom, usernameToWhom;

    public ChatsEntity() {

    }


    public ChatsEntity(Integer idChats, String usernameFrom, String usernameToWhom) {
        this.idChats = idChats;
        this.usernameFrom = usernameFrom;
        this.usernameToWhom = usernameToWhom;
    }

    public ChatsEntity(String usernameFrom, String usernameToWhom) {
        this.idChats = (int)((Math.random() * 10000000));
        this.usernameFrom = usernameFrom;
        this.usernameToWhom = usernameToWhom;
    }

    public Integer getIdChats() {
        return idChats;
    }

    public String getUsernameFrom() {
        return usernameFrom;
    }

    public void setUsernameFrom(String usernameFrom) {
        this.usernameFrom = usernameFrom;
    }

    public String getUsernameToWhom() {
        return usernameToWhom;
    }

    public void setUsernameToWhom(String usernameToWhom) {
        this.usernameToWhom = usernameToWhom;
    }
}
