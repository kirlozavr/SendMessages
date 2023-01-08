package com.example.sendmessages.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.example.sendmessages.General.Data;
import com.example.sendmessages.General.DataBase;
import com.example.sendmessages.General.DateFormat;
import com.example.sendmessages.Sevice.NetworkIsConnectedService;
import com.example.sendmessages.Interface.OnClickListener;
import com.example.sendmessages.Mapping.ChatsMapper;
import com.example.sendmessages.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *  Класс отвечает за представление чатов конкретного пользователя
 *  **/

public class ChatsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ConstraintLayout constraintLayout;
    private String usernameFrom;
    private RecyclerView recycleView;
    private RecyclerViewAdapterChats adapterChats;
    private FirebaseFirestore db;
    private ChatsMapper mapper = new ChatsMapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
        getChats();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         *  Удалаются временные данные необходимые только во время работы приложения
         *  **/
        Data.removePreferences(this, Data.USERNAME);
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
                /**
                 *  Пользователь нажимает на значок поиска
                 *  **/
                Intent intent = new Intent(ChatsActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.exit: {
                /**
                 * Пользователь нажимает на значок выхода из приложения
                 * **/
                Data.removePreferences(this, Data.USERNAME);
                Data.removePreferences(this, Data.SAVE_USERNAME);
                Intent intent = new Intent(ChatsActivity.this, RegistrationActivity.class);
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
        usernameFrom = Data
                .getStringPreferences(this, Data.USERNAME);

        getSupportActionBar().setTitle(
                "Добро пожаловать " +
                        usernameFrom.substring(0, 1).toUpperCase() +
                        usernameFrom.substring(1)
        );
        initRecycler();
        isConnected();
    }

    public void initRecycler() {
        OnClickListener<ChatsDto> onClickListener =
                new OnClickListener<com.example.sendmessages.DTO.ChatsDto>() {
                    @Override
                    public void onClick(ChatsDto entity, int position) {
                        runStartActivity(entity.getUsernameToWhom());
                    }
                };
        recycleView = findViewById(R.id.recycleViewMain);
        adapterChats = new RecyclerViewAdapterChats(ChatsActivity.this, onClickListener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(adapterChats);
    }

    /**
     *  В этом методе происходит проверка на подключение к сети интернет.
     *  **/
    public void isConnected() {
        NetworkIsConnectedService networkIsConnectedService =
                new ViewModelProvider(ChatsActivity.this)
                        .get(NetworkIsConnectedService.class);
        networkIsConnectedService
                .getConnected()
                .observe(ChatsActivity.this, connected -> {
                    networkIsConnectedService.setSnackbar(
                            constraintLayout,
                            NetworkIsConnectedService.NO_CONNECTED_TO_NETWORK,
                            NetworkIsConnectedService.VISIBLE_LONG
                    );
                });
    }

    /**
     * В этом методе происходит получение списка всех чатом пользователя и вывод их на экран.
     * **/
    private void getChats() {
        List<ChatsDto> chatsList = new ArrayList<ChatsDto>();

        try {
            db
                    .collection(DataBase.CHATS_DB)
                    .document(usernameFrom)
                    .collection(DataBase.ListTag.COLLECTIONS_CHATS_TAG)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(
                                @Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException error
                        ) {
                            LocalDate localDate = LocalDate.now();

                            adapterChats.deleteList();
                            for (DocumentSnapshot ds : value.getDocuments()) {
                                /**
                                 * Проверка, если времени нет,
                                 * то не загружает эти данные в маппер.
                                 * **/
                                if (
                                        ds.toObject(ChatsEntity.class).getTimeMessageToDataBase() != null
                                ) {
                                    ZonedDateTime zonedDateTime =
                                            ZonedDateTime.parse(
                                                    ds.toObject(ChatsEntity.class).getTimeMessageToDataBase(),
                                                    DateFormat.getFormatFromDataBase()
                                            );

                                    /**
                                     * Проверка на дату,
                                     * если дата сегодняшняя,
                                     * то не показыает год, месяц и день.
                                     * **/
                                    if (zonedDateTime.toLocalDate().isEqual(localDate)) {
                                        mapper.setIsToday(false);
                                    } else {
                                        mapper.setIsToday(true);
                                    }

                                    ChatsDto chatsDto = mapper
                                            .getEntityToDto(ds.toObject(ChatsEntity.class));
                                    chatsList.add(chatsDto);
                                }
                                adapterChats.setList(chatsList);
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    private void runStartActivity(String username) {
        Intent intent = new Intent(ChatsActivity.this, MessagesSendActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

}
