package com.example.sendmessages.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sendmessages.General.Data;
import com.example.sendmessages.R;

/**
 *  Этот класс запускается первый в приложении,
 * здесь происходит проверка на сохранение данных пользователя при входе.
 * Если пользователь нажал галочку "Запомнить меня",
 * то открывается список с чатами этого пользователя,
 * если же нет, то экран регистрации.
 * **/

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);
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
