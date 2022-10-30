package com.example.sendmessages.Adapters;

public class RegistrationAdapter {

    private int id;
    private String username, password;

    public RegistrationAdapter(){}

    public RegistrationAdapter(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public RegistrationAdapter getUser(RegistrationAdapter registrationAdapter){
        return new RegistrationAdapter(
                registrationAdapter.id,
                registrationAdapter.username,
                registrationAdapter.password);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
