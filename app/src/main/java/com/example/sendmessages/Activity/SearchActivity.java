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
import com.example.sendmessages.DTO.SearchDto;
import com.example.sendmessages.Entity.UserEntity;
import com.example.sendmessages.General.DataBase;
import com.example.sendmessages.General.NetworkIsConnected;
import com.example.sendmessages.Interface.OnClickListener;
import com.example.sendmessages.Mapping.SearchMapper;
import com.example.sendmessages.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private ConstraintLayout constraintLayout;
    private FirebaseFirestore firestore;
    private SearchMapper mapper = new SearchMapper();
    private RecyclerView recyclerView;
    private RecyclerViewAdapterSearch adapterSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        initialization();
    }

    private void initialization() {
        constraintLayout = findViewById(R.id.constraintLayoutSearchLayout);
        firestore = FirebaseFirestore.getInstance();
        searchView = findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        isConnected();
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

    public void isConnected(){
        NetworkIsConnected networkIsConnected =
                new ViewModelProvider(SearchActivity.this)
                        .get(NetworkIsConnected.class);
        networkIsConnected
                .getConnected()
                .observe(SearchActivity.this, connected -> {
                    networkIsConnected.setSnackbar(
                            constraintLayout,
                            NetworkIsConnected.NO_CONNECTED_TO_NETWORK,
                            NetworkIsConnected.VISIBLE_LONG
                    );
                });
    }

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

    private void runStartActivity(String username) {
        Intent intent = new Intent(SearchActivity.this, MessagesSendActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }

}
