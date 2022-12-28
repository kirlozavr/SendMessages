package com.example.sendmessages.DTO;

public class SearchDto {

    private String username;

    public SearchDto(String username) {
        this.username = username;
    }

    public SearchDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
