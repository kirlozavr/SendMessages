package com.example.sendmessages.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sendmessages.General.Data;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isSave();
    }

    private void isSave() {
        if (
                Data
                        .getSharedPreferences(this)
                        .contains(Data.SAVE_USERNAME)
        ) {
            Data.putStringPreferences(
                    this,
                    Data.USERNAME,
                    Data.getStringPreferences(this, Data.SAVE_USERNAME)
            );
            runStartActivity(ChatsActivity.class);
        } else {
            runStartActivity(RegistrationActivity.class);
        }
    }

    private void runStartActivity(Class classWhere) {
        Intent intent = new Intent(
                MainActivity.this,
                classWhere
        );
        startActivity(intent);
        finish();
    }
}
