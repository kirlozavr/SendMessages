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
import androidx.recyclerview.widget.RecyclerView;

import com.example.sendmessages.General.DataBase;
import com.example.sendmessages.R;

public class MainActivity extends AppCompatActivity{

    private RecyclerView recycleViewMain;
    private Toolbar toolbar;
    private String username;
    private SharedPreferences settings;

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
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialization(){
        recycleViewMain = findViewById(R.id.RecycleViewMain);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        settings = getSharedPreferences(DataBase.SettingsTag.SETTINGS_TAG, MODE_PRIVATE);
        if(settings.contains(DataBase.SettingsTag.USER_NAME_TAG)){
            username = settings.getString(DataBase.SettingsTag.USER_NAME_TAG, "");
        }
        getSupportActionBar().setTitle(
                "Добро пожаловать " +
                username.substring(0,1).toUpperCase() +
                username.substring(1)
        );
    }

    private void getDataMessages(){

    }

    private void searchUsers(String searchName){

    }

}
