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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sendmessages.Adapters.RecyclerViewAdapterMessages;
import com.example.sendmessages.DTO.MessageDto;
import com.example.sendmessages.Entity.ChatsEntity;
import com.example.sendmessages.Entity.MessageEntity;
import com.example.sendmessages.General.DataBase;
import com.example.sendmessages.General.DateFormat;
import com.example.sendmessages.Mapping.MessageMapper;
import com.example.sendmessages.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessagesSendActivity extends AppCompatActivity {

    private String username;
    private Toolbar toolbar;
    private FirebaseFirestore db;
    private MessageMapper mapper = new MessageMapper();
    private EditText editText;
    private Button button;
    private SharedPreferences settings;
    private MessageEntity messageEntity;
    private ChatsEntity chatsEntityFrom;
    private ChatsEntity chatsEntityToWhom;
    private RecyclerViewAdapterMessages adapterMessages;
    private RecyclerView recyclerView;
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
            initView();
            username = getIntent().getStringExtra("username");
            getSupportActionBar().setTitle("Чат с " + username);
            settings = getSharedPreferences(DataBase.SettingsTag.SETTINGS_TAG, MODE_PRIVATE);
            initRecyclerView();
            getChats();
        } catch (Exception e) {
            Log.i("Ошибка", "Ошибка initializationMessagesSend: " + e.getMessage());
        }
    }

    private void initView() {
        editText = findViewById(R.id.editTextSendMessages);
        button = findViewById(R.id.buttonSendMessages);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycleViewSendMessages);
        adapterMessages = new RecyclerViewAdapterMessages(
                MessagesSendActivity.this,
                settings.getString(DataBase.SettingsTag.USER_NAME_TAG, "")
        );
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterMessages);
    }

    private void setMessagesEntity() {
        messageEntity = new MessageEntity(
                editText.getText().toString(),
                DateFormat.getFormatToDataBase().format(ZonedDateTime.now()),
                settings.getString(DataBase.SettingsTag.USER_NAME_TAG, "")
        );
    }

    private void setChatsEntity() {
        chatsEntityFrom = new ChatsEntity(
                username
        );
        chatsEntityToWhom = new ChatsEntity(
                chatsEntityFrom.getIdChats(),
                settings.getString(DataBase.SettingsTag.USER_NAME_TAG, "")
        );
    }

    private void addChatsToDataBase() {
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
                        if (documentSnapshot.exists()) {
                            chatsEntityFrom = documentSnapshot
                                    .toObject(ChatsEntity.class);
                            boolChatExist = true;
                        } else {
                            setChatsEntity();
                        }
                        getMessagesFromDataBase();
                    }
                });
    }

    public void updateChats() {
        db
                .collection(DataBase.CHATS_DB)
                .document(settings.getString(DataBase.SettingsTag.USER_NAME_TAG, ""))
                .collection(DataBase.SettingsTag.COLLECTIONS_CHATS_TAG)
                .document(username)
                .update("lastMessage", messageEntity.getMessage());
        db
                .collection(DataBase.CHATS_DB)
                .document(username)
                .collection(DataBase.SettingsTag.COLLECTIONS_CHATS_TAG)
                .document(settings.getString(DataBase.SettingsTag.USER_NAME_TAG, ""))
                .update("lastMessage", messageEntity.getMessage());
        boolChatExist = true;
    }

    private void addMessagesToDataBase() {
        if (
                editText.getText().toString() != null
                        || editText.getText().toString().length() != 0
        ) {
            setMessagesEntity();
            if (!boolChatExist) {
                addChatsToDataBase();
            }
            db
                    .collection(DataBase.MESSAGES_DB)
                    .document(chatsEntityFrom.getIdChats().toString())
                    .collection(DataBase.SettingsTag.COLLECTIONS_MESSAGES_TAG)
                    .document(messageEntity.getDateTimeToDataBase())
                    .set(messageEntity);
            editText
                    .getText()
                    .clear();
            updateChats();
        }
    }

    private void getMessagesFromDataBase() {
        List<MessageDto> messageList = new ArrayList<MessageDto>();
        try {
            db
                    .collection(DataBase.MESSAGES_DB)
                    .document(chatsEntityFrom.getIdChats().toString())
                    .collection(DataBase.SettingsTag.COLLECTIONS_MESSAGES_TAG)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(
                                @Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException error
                        ) {
                            if (!value.isEmpty()) {
                                adapterMessages.deleteList();
                                for (DocumentSnapshot ds : value.getDocuments()) {
                                    MessageDto message = mapper
                                            .getEntityToDto(ds.toObject(MessageEntity.class));
                                    messageList.add(message);
                                }
                                adapterMessages.setList(messageList);
                            }
                        }
                    });

        } catch (Exception e) {

        }

    }
}


