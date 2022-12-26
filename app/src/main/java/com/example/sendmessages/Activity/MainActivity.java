package com.example.sendmessages.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sendmessages.Adapters.RecyclerViewAdapterChats;
import com.example.sendmessages.DTO.ChatsDto;
import com.example.sendmessages.Entity.ChatsEntity;
import com.example.sendmessages.General.DataBase;
import com.example.sendmessages.General.NetworkIsConnected;
import com.example.sendmessages.Interface.OnClickListener;
import com.example.sendmessages.Mapping.ChatsMapper;
import com.example.sendmessages.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ConstraintLayout constraintLayout;
    private String username;
    private RecyclerView recycleView;
    private RecyclerViewAdapterChats adapterChats;
    private FirebaseFirestore db;
    private ChatsMapper mapper = new ChatsMapper();
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
        getChats();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_user: {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.exit: {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialization() {
        db = FirebaseFirestore.getInstance();
        constraintLayout = findViewById(R.id.constraintLayoutMainActivity);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        settings = getSharedPreferences(DataBase.SettingsTag.SETTINGS_TAG, MODE_PRIVATE);
        if (settings.contains(DataBase.SettingsTag.USER_NAME_TAG)) {
            username = settings.getString(DataBase.SettingsTag.USER_NAME_TAG, "");
        }
        getSupportActionBar().setTitle(
                "Добро пожаловать " +
                        username.substring(0, 1).toUpperCase() +
                        username.substring(1)
        );
        initRecycler();
        isConnected();
    }

    public void initRecycler(){
        OnClickListener<ChatsDto> onClickListener = new OnClickListener<com.example.sendmessages.DTO.ChatsDto>() {
            @Override
            public void onClick(ChatsDto entity, int position) {
                runStartActivity(entity.getUsernameToWhom());
            }
        };
        recycleView = findViewById(R.id.recycleViewMain);
        adapterChats = new RecyclerViewAdapterChats(MainActivity.this, onClickListener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(adapterChats);
    }

    public void isConnected(){
        NetworkIsConnected networkIsConnected =
                new ViewModelProvider(MainActivity.this)
                        .get(NetworkIsConnected.class);
        networkIsConnected
                .getConnected()
                .observe(MainActivity.this, connected -> {
                    networkIsConnected.setSnackbar(
                            constraintLayout,
                            NetworkIsConnected.NO_CONNECTED_TO_NETWORK,
                            NetworkIsConnected.VISIBLE_LONG
                    );
                });
    }

    private void getChats() {
        List<ChatsDto> chatsList = new ArrayList<ChatsDto>();

        try {
            db
                    .collection(DataBase.CHATS_DB)
                    .document(username)
                    .collection(DataBase.SettingsTag.COLLECTIONS_CHATS_TAG)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(
                                @Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException error
                        ) {
                            adapterChats.deleteList();
                            for (DocumentSnapshot ds : value.getDocuments()) {
                                ChatsDto chatsDto = mapper
                                        .getEntityToDto(ds.toObject(ChatsEntity.class));
                                chatsList.add(chatsDto);
                            }
                            adapterChats.setList(chatsList);
                        }
                    });
        } catch (Exception e){

        }
    }

    private void runStartActivity(String username){
        Intent intent = new Intent(MainActivity.this, MessagesSendActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

}
