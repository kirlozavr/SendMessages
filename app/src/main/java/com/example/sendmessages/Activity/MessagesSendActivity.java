package com.example.sendmessages.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sendmessages.Adapters.RecyclerViewAdapterMessages;
import com.example.sendmessages.Common.Data;
import com.example.sendmessages.R;
import com.example.sendmessages.Sevice.ImageService;
import com.example.sendmessages.Sevice.MessageService;
import com.example.sendmessages.Sevice.NetworkIsConnectedService;

/**
 * Класс отвечает за отображение переписки конкретных пользователей
 * и ввод сообщений этому пользователю.
 * При запуске этого класса, отправляется запрос в БД,
 * на наличие уже существующего чата между пользователями,
 * если чата еще нет, то создается новый, но не записывается в БД.
 * Запись происходит тогда, когда пользователь отправляет сообщение.
 * Все операции с БД выполняет класс MessageService
 **/

public class MessagesSendActivity extends AppCompatActivity {

    private MessageService messageService;
    private String usernameFrom, usernameToWhom;
    private Toolbar toolbar;
    private ImageService imageService;
    private EditText editText;
    private ProgressBar progressBar;
    private ImageView imageView;
    private ImageButton getGallery, clearImageButton, sendMess;
    private FrameLayout frameLayout;
    private ConstraintLayout constraintLayout;
    private RecyclerViewAdapterMessages adapterMessages;
    private RecyclerView recyclerView;
    /**
     * Ключ для сохранения введенного сообщения
     **/
    private static final String TEXT_VIEW = "textMessage";
    /**
     * Ключ для сохранения логина пользователя с которым ведется переписка
     **/
    private static final String USERNAME_TO_WHOM = "usernameToWhom";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_send_layout);

        initialization();
        onClick();
    }

    /**
     * Сохранение состояния полей при закрытии или свертывании активити
     **/
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(
                TEXT_VIEW,
                editText.getText().toString()
        );
        outState.putString(
                USERNAME_TO_WHOM,
                usernameToWhom
        );
    }

    /**
     * Запись сохраненных значений в поля
     **/
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        usernameToWhom = savedInstanceState.getString(USERNAME_TO_WHOM);
        editText.setText(
                savedInstanceState
                        .getString(TEXT_VIEW)
        );
        editText.setSelection(
                editText.getText().length()
        );
    }

    /**
     * Метод отвечающий за обработку нажатий
     **/
    private void onClick() {

        sendMess.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (imageView.getDrawable() == null) {
                            messageService.addMessagesToDataBase();
                        } else {
                            imageService.setImageToDataBase(
                                    messageService,
                                    frameLayout,
                                    imageView,
                                    progressBar
                            );
                        }
                    }
                });

        getGallery.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imageService = new ImageService(
                                getActivityResultRegistry(),
                                imageView,
                                frameLayout
                        );
                        imageService.launch();
                    }
                });

        clearImageButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imageService.clearImageView(imageView, frameLayout);
                    }
                }
        );
    }

    private void initialization() {
        try {
            initView();
            usernameToWhom = getIntent().getStringExtra("username");
            usernameFrom = Data
                    .getStringPreferences(this, Data.USERNAME);
            getSupportActionBar().setTitle("Чат с " + usernameToWhom);
            initRecyclerView();
            messageService = new MessageService();
            messageService
                    .setAdapterMessages(adapterMessages)
                    .setEditText(editText)
                    .setImageView(imageView)
                    .setUsernameFrom(usernameFrom)
                    .setUsernameToWhom(usernameToWhom);
            messageService.getChat();
        } catch (Exception e) {
            Log.i("Ошибка", "Ошибка initializationMessagesSend: " + e.getMessage());
        }
    }

    private void initView() {
        constraintLayout = findViewById(R.id.constraintLayoutMessagesActivity);
        progressBar = findViewById(R.id.progressBarImage);
        frameLayout = findViewById(R.id.frameLayout2);
        editText = findViewById(R.id.editTextSendMessages);
        imageView = findViewById(R.id.imageViewMessage);
        clearImageButton = findViewById(R.id.clearImageMessage);
        getGallery = findViewById(R.id.imageButtonMessage);
        sendMess = findViewById(R.id.buttonSendMessages);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * Проверка на подключение к сети интернет
         **/

        NetworkIsConnectedService networkIsConnectedService =
                new ViewModelProvider(MessagesSendActivity.this)
                        .get(NetworkIsConnectedService.class);

        networkIsConnectedService.isConnected(
                networkIsConnectedService,
                MessagesSendActivity.this,
                constraintLayout
        );
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycleViewSendMessages);
        adapterMessages = new RecyclerViewAdapterMessages(
                MessagesSendActivity.this,
                Data.getSharedPreferences(this).getString(Data.USERNAME, "")
        );
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterMessages);
    }

}


