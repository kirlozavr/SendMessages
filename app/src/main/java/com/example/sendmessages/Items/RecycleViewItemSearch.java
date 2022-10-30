package com.example.sendmessages.Items;

public class RecycleViewItemSearch {

    private String username;
    private int userId;

    public RecycleViewItemSearch(String username, int userId) {
        this.username = username;
        this.userId = userId;
    }
    public RecycleViewItemSearch(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
