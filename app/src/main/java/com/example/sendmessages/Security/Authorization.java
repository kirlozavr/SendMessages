package com.example.sendmessages.Security;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Authorization extends AppCompatActivity {

    private static final String fileName = "authorization.txt";

    public void setAuthorization(String username, String password) {
        try{
            BufferedWriter file = new BufferedWriter(
                    new OutputStreamWriter(
                            openFileOutput(
                                    fileName,
                                    MODE_PRIVATE
                            )));
            file.write(username + "\n" + password);
            file.close();
        }catch (IOException e){
            Log.i("Ошибка", e.getMessage());
        }
    }

    public void getAuthorization(){
        String auto = "";
        try(BufferedReader file = new BufferedReader(new FileReader(fileName))){
            while (file.ready()){
                auto+=file.read();
            }
            Log.i("кака", auto);
        }catch (IOException e){
            Log.i("Ошибка", e.getMessage());
        }
    }
}
