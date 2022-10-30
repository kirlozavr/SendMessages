package com.example.sendmessages.Activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sendmessages.Adapters.RecycleViewAdapterSearch;
import com.example.sendmessages.Items.RecycleViewItemSearch;
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
    private RecyclerView recyclerView;
    private FirebaseFirestore firestore;
    private RecycleViewAdapterSearch adapterSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        initialization();
    }

    private void initialization() {
        firestore = FirebaseFirestore.getInstance();
        searchView = findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        recyclerView = findViewById(R.id.recycleViewSearchView);
        adapterSearch = new RecycleViewAdapterSearch(SearchActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterSearch);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() == 0 || newText.isEmpty()){
                    adapterSearch.deleteList();
                } else {
                    search(newText);
                }
                return false;
            }
        });
    }

    private void search(String username) {

        List<RecycleViewItemSearch> list = new ArrayList<RecycleViewItemSearch>();

        firestore.collection(MainActivity.NAME_DB)
                .whereGreaterThanOrEqualTo("username", username)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot ds : task.getResult()) {
                            RecycleViewItemSearch recycleViewItemSearch = new RecycleViewItemSearch(
                                    ds.toObject(RecycleViewItemSearch.class).getUsername()
                                    , ds.toObject(RecycleViewItemSearch.class).getUserId());
                            list.add(recycleViewItemSearch);
                        }
                        adapterSearch.setSearchList(list);

                    }
                });
    }
}
