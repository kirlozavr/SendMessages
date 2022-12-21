package com.example.sendmessages.DTO;

public class SearchDto {

    private String username;
    private int userId;

    public SearchDto(String username, int userId) {
        this.username = username;
        this.userId = userId;
    }
    public SearchDto(){}

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
