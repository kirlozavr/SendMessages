package com.example.sendmessages.Sevice;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.sendmessages.DTO.ChatsDto;
import com.example.sendmessages.Entity.ChatsEntity;
import com.example.sendmessages.General.Data;
import com.example.sendmessages.General.DataBase;
import com.example.sendmessages.General.DateFormat;
import com.example.sendmessages.Mapping.ChatsMapper;
import com.example.sendmessages.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class NotificationService extends FirebaseMessagingService {

    private FirebaseFirestore db;
    private ChatsMapper mapper = new ChatsMapper();
    private String usernameFrom;

    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseFirestore.getInstance();
        usernameFrom = Data.getStringPreferences(this, Data.SAVE_USERNAME);
        Log.i("папа", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

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

                                NotificationCompat.Builder builder =
                                        new NotificationCompat.Builder(NotificationService.this, "process")
                                                .setSmallIcon(R.drawable.ic_launcher_background)
                                                .setContentTitle(chatsDto.getUsernameToWhom())
                                                .setContentText(chatsDto.getLastMessage())
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                                NotificationManagerCompat managerCompat =
                                        NotificationManagerCompat.from(NotificationService.this);
                                NotificationChannel notificationChannel = new NotificationChannel("1", "1", NotificationManager.IMPORTANCE_DEFAULT);
                                managerCompat.createNotificationChannel(notificationChannel);
                                managerCompat.notify(1, builder.build());
                                Log.i("папа", chatsDto.getLastMessage());
                            }
                        }
                    }
                });
        Log.i("папа", "onStart");
        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("папа", "onDestroy");
    }
}
