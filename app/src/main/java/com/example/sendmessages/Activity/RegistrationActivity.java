package com.example.sendmessages.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.sendmessages.Common.Data;
import com.example.sendmessages.Common.DataBase;
import com.example.sendmessages.Entity.UserEntity;
import com.example.sendmessages.R;
import com.example.sendmessages.Sevice.NetworkIsConnectedService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Данный класс отвечает за activity R.layout.registration, в нем происходит регистрация или вход пользователей.
 * Пользователь вводит значения в  editTextName и editTextNumberPassword,
 * textViewRegistration отвечает за смену целевого дествия которое выполняет кнопка buttonRegistration
 * при нажатии на buttonRegistration происходит проверка полей имени и пароля со значениями в БД
 * при выполнении этих условий происходит либо регистрация и вход нового пользователя либо вход уже существующего
 **/

public class RegistrationActivity extends AppCompatActivity {

    private boolean registration_bool = false;
    private ConstraintLayout constraintLayout;
    private TextView textViewRegistration;
    private EditText editTextName;
    private EditText editTextNumberPassword;
    private Button buttonRegistration;
    private CheckBox checkBox;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        initialization();
        onClick();
    }

    private void onClick() {

        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Проверяем поля editTextName и editTextPassword на введенные значения
                 * **/
                if (editTextName.getText().toString().trim().equals("")) {
                    Toast.makeText(RegistrationActivity.this, R.string.Toast_name, Toast.LENGTH_LONG).show();
                } else if (editTextNumberPassword.getText().toString().trim().equals("")) {
                    Toast.makeText(RegistrationActivity.this, R.string.Toast_password, Toast.LENGTH_LONG).show();
                } else {
                    runSearchUserForRegistrationOrInput();
                }
            }
        });

        textViewRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    /**
                     * Пользователь нажал на textViewRegistration, начинается проверка:
                     * **/

                    buttonStatus();
                } catch (Exception e) {
                    Log.i("Ошибка", "Ошибка textViewRegistration: " + e.getMessage());
                }
            }
        });
    }

    @SuppressLint("ResourceType")
    private void initialization() {
        /**
         * Инициализируем все переменные
         * **/
        try {
            initView();
            db = FirebaseFirestore.getInstance();

            /**
             * Проверка на подключение к сети интернет
             **/

            NetworkIsConnectedService networkIsConnectedService =
                    new ViewModelProvider(RegistrationActivity.this)
                            .get(NetworkIsConnectedService.class);

            networkIsConnectedService.isConnected(
                    networkIsConnectedService,
                    RegistrationActivity.this,
                    constraintLayout
            );

        } catch (Exception e) {
            Log.i("Ошибка", "Ошибка initialization Registration: " + e.getMessage());
        }
    }

    public void initView() {
        constraintLayout = findViewById(R.id.constraintLayoutRegistrationActivity);
        textViewRegistration = findViewById(R.id.textViewRegistration);
        editTextName = findViewById(R.id.editTextName);
        editTextNumberPassword = findViewById(R.id.editTextNumberPassword);
        buttonRegistration = findViewById(R.id.buttonRegistration);
        buttonRegistration.setText(R.string.buttonRegistration_false);
        textViewRegistration.setText(R.string.textViewRegistration_false);
        checkBox = findViewById(R.id.checkBoxRegistration);
    }

    private void buttonStatus() {
        if (registration_bool) {
            /**
             * если пользователь зарегистрирован, поменять текст buttonRegistration и textViewRegistration
             * **/
            registration_bool = false;
            buttonRegistration.setText(R.string.buttonRegistration_false);
            textViewRegistration.setText(R.string.textViewRegistration_false);
        } else {
            /**
             * если пользователь не зарегистрирован, поменять текст buttonRegistration и textViewRegistration
             * **/
            registration_bool = true;
            buttonRegistration.setText(R.string.buttonRegistration_true);
            textViewRegistration.setText(R.string.textViewRegistration_true);
        }
    }

    private void runSearchUserForRegistrationOrInput() {
        /**
         * Обращаемся к нашей бд, к папке NAME_DB, ко всем полям username на наличие по имени введенным пользователем
         * **/
        db
                .collection(DataBase.NAME_DB)
                .document(editTextName.getText().toString().trim())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot ds = task.getResult();
                        UserEntity userEntity = ds.toObject(UserEntity.class);

                        /**
                         * Пользователь хочет войти и верно ввел данные
                         * **/
                        if (
                                !registration_bool &&
                                        ds.exists() &&
                                        editTextNumberPassword
                                                .getText()
                                                .toString()
                                                .trim()
                                                .equals(userEntity.getPassword())
                        ) {
                            runStartActivity();
                        }

                        /**
                         * Пользователь хочет войти и неверно ввел данные
                         * **/
                        if (
                                !registration_bool &&
                                        (!ds.exists() ||
                                                !editTextNumberPassword
                                                        .getText()
                                                        .toString()
                                                        .trim()
                                                        .equals(userEntity.getPassword()))
                        ) {
                            Toast.makeText(
                                    RegistrationActivity.this,
                                    "Неверный логин или пароль",
                                    Toast.LENGTH_LONG
                            ).show();
                        }

                        /**
                         * Пользователь хочет зарегестрироваться и такого пользователя еще нет
                         * **/
                        if (
                                registration_bool &&
                                        !ds.exists()
                        ) {
                            sendRegistration();
                            runStartActivity();
                        }

                        /**
                         * Пользователь хочет зарегестрироваться и такой пользователь уже есть
                         * **/
                        if (
                                registration_bool &&
                                        ds.exists()
                        ) {
                            Toast.makeText(
                                    RegistrationActivity.this,
                                    "Пользователь с таким именем уже есть",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                });
    }

    /**
     * Запуск ChatsActivity и закрытие нынещнего активити
     **/

    private void runStartActivity() {
        /**
         * Проверка на галочку "Запомнить меня"
         * **/
        if (checkBox.isChecked()) {
            Data.putStringPreferences(
                    this,
                    Data.SAVE_USERNAME,
                    editTextName.getText().toString().trim()
            );
        }

        Data.putStringPreferences(
                this,
                Data.USERNAME,
                editTextName.getText().toString().trim()
        );

        Intent intent = new Intent(
                RegistrationActivity.this,
                ChatsActivity.class
        );
        startActivity(intent);
        finish();
    }

    private void sendRegistration() {
        /**
         * Запись всех значений в переменные и регистрация нового пользователя
         * **/
        try {
            int id = (int) (Math.random() * 10000000);
            String username = editTextName.getText().toString().trim();
            String password = editTextNumberPassword.getText().toString().trim();
            UserEntity userEntity = new UserEntity(id, username, password);
            db
                    .collection(DataBase.NAME_DB)
                    .document(userEntity.getUsername())
                    .set(userEntity);
        } catch (Exception e) {

        }
    }

}
