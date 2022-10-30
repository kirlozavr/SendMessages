package com.example.sendmessages.Items;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleViewItemUsers extends RecyclerView.ViewHolder {

    private String username_this;
    private String username_send;
    private String last_text;

    public RecycleViewItemUsers(@NonNull View itemView) {
        super(itemView);
    }

    public void RecycleViewItemUsers(String this_username, String send_username, String last_text){
        this.username_this = username_this;
        this.username_send = username_send;
        this.last_text = last_text;
    }

    public String getUsernameThis() {
        return username_this;
    }

    public String getUsernameSend() {
        return username_send;
    }

    public String getLastText() {
        return last_text;
    }

    public void setUsernameThis(String username_this) {
        this.username_this = username_this;
    }

    public void setUsernameSend(String username_send) {
        this.username_send = username_send;
    }

    public void setLastText(String last_text) {
        this.last_text = last_text;
    }
}
