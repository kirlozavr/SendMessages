package com.example.sendmessages.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sendmessages.Adapters.RecyclerViewAdapterMessages;
import com.example.sendmessages.DTO.MessageDto;
import com.example.sendmessages.Entity.ChatsEntity;
import com.example.sendmessages.Entity.MessageEntity;
import com.example.sendmessages.General.Data;
import com.example.sendmessages.General.DataBase;
import com.example.sendmessages.General.DateFormat;
import com.example.sendmessages.General.NetworkIsConnected;
import com.example.sendmessages.Mapping.MessageMapper;
import com.example.sendmessages.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessagesSendActivity extends AppCompatActivity {

    private String usernameFrom, usernameToWhom;
    private Toolbar toolbar;
    private FirebaseFirestore db;
    private MessageMapper mapper = new MessageMapper();
    private EditText editText;
    private Button button;
    private ConstraintLayout constraintLayout;
    private MessageEntity messageEntity;
    private ChatsEntity chatsEntityFrom, chatsEntityToWhom;
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
            usernameToWhom = getIntent().getStringExtra("username");
            usernameFrom = Data
                    .getStringPreferences(this, Data.USERNAME);
            getSupportActionBar().setTitle("Чат с " + usernameToWhom);
            initRecyclerView();
            getChat();
        } catch (Exception e) {
            Log.i("Ошибка", "Ошибка initializationMessagesSend: " + e.getMessage());
        }
    }

    private void initView() {
        constraintLayout = findViewById(R.id.constraintLayoutMessagesActivity);
        editText = findViewById(R.id.editTextSendMessages);
        button = findViewById(R.id.buttonSendMessages);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        isConnected();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycleViewSendMessages);
        adapterMessages = new RecyclerViewAdapterMessages(
                MessagesSendActivity.this,
                Data.getSharedPreferences(this).getString(Data.USERNAME, "")
        );
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterMessages);
    }

    public void isConnected() {
        NetworkIsConnected networkIsConnected =
                new ViewModelProvider(MessagesSendActivity.this)
                        .get(NetworkIsConnected.class);
        networkIsConnected
                .getConnected()
                .observe(MessagesSendActivity.this, connected -> {
                    networkIsConnected.setSnackbar(
                            constraintLayout,
                            NetworkIsConnected.NO_CONNECTED_TO_NETWORK,
                            NetworkIsConnected.VISIBLE_LONG
                    );
                });
    }

    private void setMessagesEntity() {
        messageEntity = new MessageEntity(
                editText.getText().toString(),
                DateFormat.getFormatToDataBase().format(ZonedDateTime.now()),
                usernameFrom
        );
    }

    private void setChatsEntity() {
        chatsEntityFrom = new ChatsEntity(
                usernameToWhom
        );
        chatsEntityToWhom = new ChatsEntity(
                chatsEntityFrom.getIdChats(),
                usernameFrom
        );
    }

    private void addChatsToDataBase() {

        chatsEntityFrom = new ChatsEntity(
                chatsEntityFrom.getIdChats(),
                chatsEntityFrom.getUsernameToWhom(),
                messageEntity.getMessage(),
                messageEntity.getDateTimeToDataBase()
        );
        chatsEntityToWhom = new ChatsEntity(
                chatsEntityToWhom.getIdChats(),
                chatsEntityToWhom.getUsernameToWhom(),
                messageEntity.getMessage(),
                messageEntity.getDateTimeToDataBase()
        );

        db
                .collection(DataBase.CHATS_DB)
                .document(usernameFrom)
                .collection(DataBase.ListTag.COLLECTIONS_CHATS_TAG)
                .document(usernameToWhom)
                .set(chatsEntityFrom);
        db
                .collection(DataBase.CHATS_DB)
                .document(usernameToWhom)
                .collection(DataBase.ListTag.COLLECTIONS_CHATS_TAG)
                .document(usernameFrom)
                .set(chatsEntityToWhom);
        boolChatExist = false;
    }

    private void getChat() {
        db
                .collection(DataBase.CHATS_DB)
                .document(usernameFrom)
                .collection(DataBase.ListTag.COLLECTIONS_CHATS_TAG)
                .document(usernameToWhom)
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
                .document(usernameFrom)
                .collection(DataBase.ListTag.COLLECTIONS_CHATS_TAG)
                .document(usernameToWhom)
                .update(
                        "lastMessage", messageEntity.getMessage(),
                        "timeMessageToDataBase", messageEntity.getDateTimeToDataBase()
                );
        db
                .collection(DataBase.CHATS_DB)
                .document(usernameToWhom)
                .collection(DataBase.ListTag.COLLECTIONS_CHATS_TAG)
                .document(usernameFrom)
                .update(
                        "lastMessage", messageEntity.getMessage(),
                        "timeMessageToDataBase", messageEntity.getDateTimeToDataBase()
                );
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
                    .collection(DataBase.ListTag.COLLECTIONS_MESSAGES_TAG)
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
        LocalDate localDate = LocalDate.now();
        try {
            db
                    .collection(DataBase.MESSAGES_DB)
                    .document(chatsEntityFrom.getIdChats().toString())
                    .collection(DataBase.ListTag.COLLECTIONS_MESSAGES_TAG)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(
                                @Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException error
                        ) {
                            adapterMessages.deleteList();
                            for (DocumentSnapshot ds : value.getDocuments()) {
                                if (ds.toObject(MessageEntity.class).getDateTimeToDataBase() != null) {

                                    ZonedDateTime zonedDateTime =
                                            ZonedDateTime.parse(
                                                    ds.toObject(MessageEntity.class).getDateTimeToDataBase(),
                                                    DateFormat.getFormatFromDataBase()
                                            );

                                    if (zonedDateTime.toLocalDate().isEqual(localDate)) {
                                        mapper.setIsToday(false);
                                    } else {
                                        mapper.setIsToday(true);
                                    }

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


