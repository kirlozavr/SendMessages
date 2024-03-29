package com.example.sendmessages.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sendmessages.Adapters.RecyclerViewAdapterSearch;
import com.example.sendmessages.Common.DataBase;
import com.example.sendmessages.DTO.SearchDto;
import com.example.sendmessages.Entity.UserEntity;
import com.example.sendmessages.Interface.OnClickListener;
import com.example.sendmessages.Mapping.SearchMapper;
import com.example.sendmessages.R;
import com.example.sendmessages.Service.NetworkIsConnectedService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс отвечает за поиск пользователей, чтобы начать с ними чат.
 **/

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private ConstraintLayout constraintLayout;
    private FirebaseFirestore firestore;
    private SearchMapper mapper = new SearchMapper();
    private RecyclerView recyclerView;
    private RecyclerViewAdapterSearch adapterSearch;
    /**
     * Ключ для сохранения поля запроса при закрытии или свертывании активити
     **/
    private static final String QUERY = "query";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        initialization();
    }

    /**
     * Сохранение состояния поля поиска пользователей
     **/
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(
                QUERY,
                searchView.getQuery().toString()
        );
    }

    /**
     * Запись в поле поиска сохраненных значений ввода
     **/
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        searchView.setQuery(
                savedInstanceState.getCharSequence(QUERY),
                false
        );
    }

    private void initialization() {
        constraintLayout = findViewById(R.id.constraintLayoutSearchLayout);
        firestore = FirebaseFirestore.getInstance();
        searchView = findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        search(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if (newText.length() == 0 || newText.isEmpty()) {
                            adapterSearch.deleteList();
                        } else {
                            search(newText);
                        }
                        return false;
                    }
                });

        initRecycler();

        /**
         * Проверка на подключение к сети интернет
         **/

        NetworkIsConnectedService networkIsConnectedService =
                new ViewModelProvider(SearchActivity.this)
                        .get(NetworkIsConnectedService.class);

        networkIsConnectedService.isConnected(
                networkIsConnectedService,
                SearchActivity.this,
                constraintLayout
        );
    }

    private void initRecycler() {
        OnClickListener<SearchDto> onClickListener =
                new OnClickListener<SearchDto>() {
                    @Override
                    public void onClick(SearchDto entity, int position) {
                        runStartActivity(entity.getUsername());
                    }
                };
        recyclerView = findViewById(R.id.recycleViewSearchView);
        adapterSearch = new RecyclerViewAdapterSearch(SearchActivity.this, onClickListener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterSearch);
    }

    /**
     * Метод поиска пользователя в БД по введенному логину
     *
     * @param username Параметр принимает имя в качестве поискового запроса
     **/
    private void search(String username) {

        List<SearchDto> list = new ArrayList<SearchDto>();

        firestore
                .collection(DataBase.NAME_DB)
                .whereGreaterThanOrEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot ds : task.getResult()) {
                                SearchDto searchDto = mapper
                                        .getEntityToDto(ds.toObject(UserEntity.class));
                                list.add(searchDto);
                            }
                            adapterSearch.setList(list);
                        }
                    }
                });
    }

    /**
     * Метода запускает активити.
     *
     * @param username Принимает в качестве параметра логин авторизованного аккаунта
     **/
    private void runStartActivity(String username) {
        Intent intent = new Intent(SearchActivity.this, MessagesSendActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }

}
