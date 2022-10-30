package com.example.sendmessages.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sendmessages.R;

public class MainActivity extends AppCompatActivity{

    private RecyclerView recycleViewMain;
    private Toolbar toolbar;
    private String name_user;
    public static final String NAME_DB = "User";
    public static final String MESSAGES = "Messages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
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
        switch (item.getItemId()){
            case R.id.search_user:{
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
            case R.id.exit:{
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_LONG).show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialization(){
        recycleViewMain = findViewById(R.id.RecycleViewMain);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name_user = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle("Добро пожаловать "
                + name_user.substring(0,1).toUpperCase()
                + name_user.substring(1));
    }

    private void getDataMessages(){

    }

    private void searchUsers(String searchName){

    }

}
