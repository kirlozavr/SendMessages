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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sendmessages.Adapters.RecyclerViewAdapterChats;
import com.example.sendmessages.Entity.ChatsEntity;
import com.example.sendmessages.General.DataBase;
import com.example.sendmessages.Interface.OnClickListener;
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
    private String username;
    private RecyclerView recycleView;
    private RecyclerViewAdapterChats adapterChats;
    private FirebaseFirestore db;
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
    }

    public void initRecycler(){
        OnClickListener<ChatsEntity> onClickListener = new OnClickListener<ChatsEntity>() {
            @Override
            public void onClick(ChatsEntity entity, int position) {
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

    private void getChats() {
        List<ChatsEntity> chatsEntityList = new ArrayList<ChatsEntity>();

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
                                chatsEntityList.add(ds.toObject(ChatsEntity.class));
                            }
                            adapterChats.setList(chatsEntityList);
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
