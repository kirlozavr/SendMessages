package com.example.sendmessages.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sendmessages.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MessagesSendActivity extends AppCompatActivity{

    private DatabaseReference databaseReference;
    private static final String NAME_DB = "User";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_send_layout);

        initialization();

    }

    private void initialization(){
        try {
            databaseReference = FirebaseDatabase.getInstance().getReference();
        } catch (Exception e){
            Log.i("Ошибка", "Ошибка initializationMessagesSend: " + e.getMessage());
        }
    }

    private void getDataMessages(){
        databaseReference.child(NAME_DB).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("Ошибка", "Ошибка получения переписки с пользователями: " + error.toException());
            }
        });
    }
}
