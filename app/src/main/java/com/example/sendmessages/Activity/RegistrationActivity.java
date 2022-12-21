package com.example.sendmessages.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sendmessages.Entity.UserEntity;
import com.example.sendmessages.General.DataBase;
import com.example.sendmessages.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Данный класс отвечает за activity R.layout.registration, в нем происходит регистрация или вход пользователей.
 * Пользователь вводит значения в  editTextName и editTextNumberPassword,
 * textViewRegistration отвечает за смену целевого дествия которое выполняет кнопка buttonRegistration
 * при нажатии на buttonRegistration происходит проверка полей имени и пароля со значениями в БД
 * при выполнении этих условий происходит либо регистрация и вход нового пользователя либо вход уже существующего
 **/

public class RegistrationActivity extends AppCompatActivity {

    private String no_username = "Пользователь с таким именем не найден";
    private String no_password = "";
    private boolean registration_bool = false;
    private TextView textViewRegistration;
    private EditText editTextName;
    private EditText editTextNumberPassword;
    private Button buttonRegistration;
    private FirebaseFirestore db;
    private SharedPreferences settings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        initialization();

        buttonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                /** Проверяем поля editTextName и editTextPassword на введенные значения **/
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
                    /** Пользователь нажал на textViewRegistration, начинается проверка: **/

                    buttonStatus();
                } catch (Exception e) {
                    Log.i("Ошибка", "Ошибка textViewRegistration: " + e.getMessage());
                }
            }
        });
    }


    private void initialization() {
        /** Инициализируем все переменные **/
        try {
            textViewRegistration = findViewById(R.id.textViewRegistration);
            editTextName = findViewById(R.id.editTextName);
            editTextNumberPassword = findViewById(R.id.editTextNumberPassword);
            buttonRegistration = findViewById(R.id.buttonRegistration);
            buttonRegistration.setText(R.string.buttonRegistration_false);
            textViewRegistration.setText(R.string.textViewRegistration_false);
            db = FirebaseFirestore.getInstance();
            settings = getSharedPreferences(DataBase.SettingsTag.SETTINGS_TAG, MODE_PRIVATE);
        } catch (Exception e) {
            Log.i("Ошибка", "Ошибка initialization Registration: " + e.getMessage());
        }
    }

    private void buttonStatus(){
        if (registration_bool) {
            /** если пользователь зарегистрирован, поменять текст buttonRegistration и textViewRegistration **/
            registration_bool = false;
            buttonRegistration.setText(R.string.buttonRegistration_false);
            textViewRegistration.setText(R.string.textViewRegistration_false);
            no_username = "Пользователь с таким именем не найден";
            no_password = "Пароль неверный";
        } else {
            /** если пользователь не зарегистрирован, поменять текст buttonRegistration и textViewRegistration **/
            registration_bool = true;
            buttonRegistration.setText(R.string.buttonRegistration_true);
            textViewRegistration.setText(R.string.textViewRegistration_true);
            no_username = "Пользователь с таким именем уже существует";
        }
    }

    private void runSearchUserForRegistrationOrInput(){
        /** Обращаемся к нашей бд, к папке NAME_DB, ко всем полям username на наличие по имени введенным пользователем **/
        db.collection(DataBase.NAME_DB)
                .whereEqualTo("username", editTextName.getText().toString().trim())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        /** Проверяем, существуют ли пользователь в папке NAME_DB**/
                        Log.i("папа", String.valueOf(task.getResult().isEmpty()));
                        if (!task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot ds : task.getResult()) {
                                UserEntity userEntity = ds.toObject(UserEntity.class);
                                /** Если пользователь хочет войти **/
                                if (!registration_bool) {
                                    /** Проверяем соответсвие пароля и в случае успеха осуществляем вход **/
                                    if (editTextNumberPassword.getText().toString().trim()
                                            .equals(userEntity.getPassword())) {
                                        runStartActivity();
                                        break;
                                    }
                                    Toast.makeText(RegistrationActivity.this, no_password, Toast.LENGTH_LONG).show();
                                    break;
                                }
                                /** Если пользователь хочет зарегистрироваться,
                                 *  но такой пользователь есть, выводит предупреждение **/
                                if (registration_bool) {
                                    Toast.makeText(RegistrationActivity.this, no_username, Toast.LENGTH_LONG).show();
                                    break;
                                }
                            }
                        } else {
                            /** Иначе (В случае если искомого пользователя нет в БД) если пользователь хочет войти,
                             *  выводит предупреждение **/
                            if (!registration_bool) {
                                Toast.makeText(RegistrationActivity.this, no_username, Toast.LENGTH_LONG).show();
                                return;
                            }
                            /** (В случае если искомого пользователя нет в БД)
                             * Если пользователь хочет зарегистрироваться, то регистрация осуществляется **/
                            if (registration_bool) {
                                sendRegistration();
                                runStartActivity();
                                return;
                            }
                        }
                    }
                });
    }

    private void runStartActivity() {
        /** Запуск MainActivity и закрытие нынещнего активити **/
        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(DataBase.SettingsTag.USER_NAME_TAG, editTextName.getText().toString().trim());
        editor.apply();
        startActivity(intent);
        finish();
    }

    private void sendRegistration() {
        /** Запись всех значений в переменные и регистрация нового пользователя **/
        try {
            int id = (int) (Math.random() * 10000000);
            String username = editTextName.getText().toString().trim();
            String password = editTextNumberPassword.getText().toString().trim();
            UserEntity userEntity = new UserEntity(id, username, password);
            db.collection(DataBase.NAME_DB).add(userEntity)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.i("Отправка", "Данные успешно отправленны " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("Ошибка", "Registration/sendRegistration " + e.getMessage());
                        }
                    });
        }catch (Exception e){

        }
    }
}
