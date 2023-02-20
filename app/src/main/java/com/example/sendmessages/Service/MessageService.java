package com.example.sendmessages.Service;

import android.net.Uri;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.sendmessages.Adapters.RecyclerViewAdapterMessages;
import com.example.sendmessages.DTO.MessageDto;
import com.example.sendmessages.Entity.ChatsEntity;
import com.example.sendmessages.Entity.MessageEntity;
import com.example.sendmessages.Common.DataBase;
import com.example.sendmessages.Common.DateFormat;
import com.example.sendmessages.Mapping.MessageMapper;
import com.google.android.gms.tasks.OnSuccessListener;
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
 * Класс отвечает за операции с БД,
 * в частности прием/отправка сообщений пользователю с которым ведется переписка.
 **/
public class MessageService {

    private FirebaseFirestore db;
    private RecyclerViewAdapterMessages adapterMessages;
    private Uri uriImage;
    private ImageView imageView;
    private EditText editText;
    private String usernameFrom, usernameToWhom;
    private MessageMapper mapper;
    private MessageEntity messageEntity;
    private ChatsEntity chatsEntityFrom, chatsEntityToWhom;
    private boolean boolChatExist = false;

    public MessageService() {
        mapper = new MessageMapper();
        db = FirebaseFirestore.getInstance();
    }

    public RecyclerViewAdapterMessages getAdapterMessages() {
        return adapterMessages;
    }

    public MessageService setAdapterMessages(RecyclerViewAdapterMessages adapterMessages) {
        this.adapterMessages = adapterMessages;
        return this;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public MessageService setImageView(ImageView imageView) {
        this.imageView = imageView;
        return this;
    }

    public EditText getEditText() {
        return editText;
    }

    public MessageService setEditText(EditText editText) {
        this.editText = editText;
        return this;
    }

    public String getUsernameFrom() {
        return usernameFrom;
    }

    public MessageService setUsernameFrom(String usernameFrom) {
        this.usernameFrom = usernameFrom;
        return this;
    }

    public String getUsernameToWhom() {
        return usernameToWhom;
    }

    public MessageService setUsernameToWhom(String usernameToWhom) {
        this.usernameToWhom = usernameToWhom;
        return this;
    }

    public Uri getUriImage() {
        return uriImage;
    }

    public MessageService setUriImage(Uri uriImage) {
        this.uriImage = uriImage;
        return this;
    }

    public void deleteUriImage() {
        uriImage = null;
    }

    /**
     * Метод отвечает за создание сущности сообщения
     **/
    private boolean setMessagesEntity() {

        /** Проверка на наличие текста в текстовом поле **/
        boolean isExistText =
                !editText.getText().toString().trim().equals("")
                        && editText.getText().toString().trim().length() != 0;

        if (
                isExistText
                        || getUriImage() != null
        ) {
            messageEntity = new MessageEntity(
                    DateFormat.getFormatToDataBase().format(ZonedDateTime.now()),
                    usernameFrom,
                    editText.getText().toString().trim()
            );

            if (getUriImage() != null) {
                messageEntity.setUriImage(getUriImage().toString());
            }

            deleteUriImage();
            return true;
        } else {
            return false;
        }
    }


    /**
     * Метод отвечает за создание сущности чата, создается 2 сущности,
     * чтобы создать 2 одинаковые записи в бд с одинаковым id.
     * Чат А с В по id:1 и чат В с А по id:1
     **/
    private void setChatsEntity() {
        chatsEntityFrom = new ChatsEntity(
                usernameToWhom
        );
        chatsEntityToWhom = new ChatsEntity(
                chatsEntityFrom.getIdChats(),
                usernameFrom
        );
    }

    /**
     * Метод отвечает за добавление чатов в БД
     **/
    private void addChatsToDataBase() {

        chatsEntityFrom = new ChatsEntity(
                chatsEntityFrom.getIdChats(),
                chatsEntityFrom.getUsernameToWhom(),
                messageEntity.getMessage(),
                messageEntity.getDateTimeToDataBase()
        );
        chatsEntityToWhom = new ChatsEntity(
                chatsEntityToWhom.getIdChats(),
                chatsEntityToWhom.getUsernameToWhom(),
                messageEntity.getMessage(),
                messageEntity.getDateTimeToDataBase()
        );

        db
                .collection(DataBase.CHATS_DB)
                .document(usernameFrom)
                .collection(DataBase.ListTag.COLLECTIONS_CHATS_TAG)
                .document(usernameToWhom)
                .set(chatsEntityFrom);
        db
                .collection(DataBase.CHATS_DB)
                .document(usernameToWhom)
                .collection(DataBase.ListTag.COLLECTIONS_CHATS_TAG)
                .document(usernameFrom)
                .set(chatsEntityToWhom);
        boolChatExist = false;
    }

    /**
     * Метод отвечает за получение чата с конкретным пользователем из БД,
     * если его еще нет, то создается новый.
     * Так же выводит все сообщения между пользователями.
     **/
    public void getChat() {
        db
                .collection(DataBase.CHATS_DB)
                .document(usernameFrom)
                .collection(DataBase.ListTag.COLLECTIONS_CHATS_TAG)
                .document(usernameToWhom)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            chatsEntityFrom = documentSnapshot
                                    .toObject(ChatsEntity.class);
                            boolChatExist = true;
                        } else {
                            setChatsEntity();
                        }
                        getMessagesFromDataBase();
                    }
                });
    }

    /**
     * Метод отвечает за обновление чата в БД, происходит перезапись значений:
     * Последнее сообщение, время отправки сообщения.
     **/
    private void updateChats() {

        db
                .collection(DataBase.CHATS_DB)
                .document(usernameFrom)
                .collection(DataBase.ListTag.COLLECTIONS_CHATS_TAG)
                .document(usernameToWhom)
                .update(
                        "lastMessage", messageEntity.getMessage(),
                        "timeMessageToDataBase", messageEntity.getDateTimeToDataBase()
                );
        db
                .collection(DataBase.CHATS_DB)
                .document(usernameToWhom)
                .collection(DataBase.ListTag.COLLECTIONS_CHATS_TAG)
                .document(usernameFrom)
                .update(
                        "lastMessage", messageEntity.getMessage(),
                        "timeMessageToDataBase", messageEntity.getDateTimeToDataBase()
                );
        boolChatExist = true;
    }

    /**
     * Метод отвечает за запись сообщения в БД.
     **/
    public void addMessagesToDataBase() {

        boolean booleanMess = setMessagesEntity();

        if (booleanMess) {
            if (!boolChatExist) {
                addChatsToDataBase();
            }
            db
                    .collection(DataBase.MESSAGES_DB)
                    .document(chatsEntityFrom.getIdChats().toString())
                    .collection(DataBase.ListTag.COLLECTIONS_MESSAGES_TAG)
                    .document(messageEntity.getDateTimeToDataBase())
                    .set(messageEntity);
            editText
                    .getText()
                    .clear();
            updateChats();
        }
    }

    /**
     * Метод отвечает за получение списка всех сообщений с пользователем с которым ведется переписка из БД.
     **/
    private void getMessagesFromDataBase() {
        List<MessageDto> messageList = new ArrayList<MessageDto>();

        try {
            db
                    .collection(DataBase.MESSAGES_DB)
                    .document(chatsEntityFrom.getIdChats().toString())
                    .collection(DataBase.ListTag.COLLECTIONS_MESSAGES_TAG)
                    .addSnapshotListener(
                            new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(
                                        @Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException error
                                ) {
                                    LocalDate localDate = LocalDate.now();
                                    adapterMessages.deleteList();
                                    for (DocumentSnapshot ds : value.getDocuments()) {

                                        /**
                                         *  Проверка на существование даты, если ее нет,
                                         *  то конвертация через маппер не осуществляется.
                                         **/

                                        if (
                                                ds
                                                        .toObject(MessageEntity.class)
                                                        .getDateTimeToDataBase() != null
                                        ) {

                                            ZonedDateTime zonedDateTime =
                                                    ZonedDateTime.parse(
                                                            ds.toObject(MessageEntity.class).getDateTimeToDataBase(),
                                                            DateFormat.getFormatFromDataBase()
                                                    );

                                            /**
                                             *  Проверка на сегодняшнюю дату,
                                             *  если дата отправки сообщения совпадает с сегодняшней,
                                             *  то выводится только время, если нет,
                                             *  то дополнительно выводится год, месяц и день.
                                             **/

                                            if (zonedDateTime.toLocalDate().isEqual(localDate)) {
                                                mapper.setIsToday(false);
                                            } else {
                                                mapper.setIsToday(true);
                                            }

                                            MessageDto message = mapper
                                                    .getEntityToDto(ds.toObject(MessageEntity.class));
                                            messageList.add(message);
                                        }
                                        adapterMessages.setList(messageList);
                                    }
                                }
                            });

        } catch (Exception e) {

        }

    }
}
