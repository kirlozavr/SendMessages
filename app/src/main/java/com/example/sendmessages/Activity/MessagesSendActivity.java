package com.example.sendmessages.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sendmessages.Entity.ChatsEntity;
import com.example.sendmessages.Entity.MessageEntity;
import com.example.sendmessages.General.DataBase;
import com.example.sendmessages.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.ZonedDateTime;

public class MessagesSendActivity extends AppCompatActivity {

    private String username;
    private Toolbar toolbar;
    private FirebaseFirestore db;
    private EditText editText;
    private Button button;
    private SharedPreferences settings;
    private MessageEntity messageEntity;
    private ChatsEntity chatsEntityFrom;
    private ChatsEntity chatsEntityToWhom;
    private boolean boolChatExist = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_send_layout);

        initialization();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMessagesToDataBase();
            }
        });
    }

    private void initialization() {
        try {
            db = FirebaseFirestore.getInstance();
            editText = findViewById(R.id.editTextSendMessages);
            button = findViewById(R.id.buttonSendMessages);
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            username = getIntent().getStringExtra("username");
            getSupportActionBar().setTitle("Чат с " + username);
            settings = getSharedPreferences(DataBase.SettingsTag.SETTINGS_TAG, MODE_PRIVATE);
            getChats();
        } catch (Exception e) {
            Log.i("Ошибка", "Ошибка initializationMessagesSend: " + e.getMessage());
        }
    }

    private void setMessagesEntity() {
            ZonedDateTime date = ZonedDateTime.now();
            messageEntity = new MessageEntity(
                    editText.getText().toString(),
                    date
            );
    }

    private void setChatsEntity() {
            chatsEntityFrom = new ChatsEntity(
                    settings.getString(DataBase.SettingsTag.USER_NAME_TAG, ""),
                    username
            );
            chatsEntityToWhom = new ChatsEntity(
                    chatsEntityFrom.getIdChats(),
                    username,
                    settings.getString(DataBase.SettingsTag.USER_NAME_TAG, "")
            );
    }

    private void addChatsToDataBase(){
        db
                .collection(DataBase.CHATS_DB)
                .document(settings.getString(DataBase.SettingsTag.USER_NAME_TAG, ""))
                .collection(DataBase.SettingsTag.COLLECTIONS_CHATS_TAG)
                .document(username)
                .set(chatsEntityFrom);
        db
                .collection(DataBase.CHATS_DB)
                .document(username)
                .collection(DataBase.SettingsTag.COLLECTIONS_CHATS_TAG)
                .document(settings.getString(DataBase.SettingsTag.USER_NAME_TAG, ""))
                .set(chatsEntityToWhom);
        boolChatExist = false;
    }

    private void getChats() {
        db
                .collection(DataBase.CHATS_DB)
                .document(settings.getString(DataBase.SettingsTag.USER_NAME_TAG, ""))
                .collection(DataBase.SettingsTag.COLLECTIONS_CHATS_TAG)
                .document(username)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            chatsEntityFrom = documentSnapshot
                                    .toObject(ChatsEntity.class);
                            boolChatExist = true;
                        } else {
                            setChatsEntity();
                        }
                    }
                });
    }

    private void addMessagesToDataBase() {
        if (
                editText.getText().toString() != null
                        || editText.getText().toString().length() != 0
        ) {
            if (!boolChatExist) {
                addChatsToDataBase();
            }
            setMessagesEntity();
            db
                    .collection(DataBase.MESSAGES_DB)
                    .document(chatsEntityFrom.getIdChats().toString())
                    .collection(DataBase.SettingsTag.COLLECTIONS_MESSAGES_TAG)
                    .add(messageEntity);
            editText
                    .getText()
                    .clear();
        }
    }

    private void getMessagesFromDataBase() {

    }

}
