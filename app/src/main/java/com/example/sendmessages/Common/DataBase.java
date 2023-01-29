package com.example.sendmessages.Common;

/**
 * Класс отвечает за хранение констант - ключей для обращения к БД
 **/
public class DataBase {

    /**
     * Название таблицы с пользователями
     **/
    public static final String NAME_DB = "User";

    /**
     * Название таблицы с сообщениями
     **/
    public static final String MESSAGES_DB = "Messages";

    /**
     * Название таблицы с чатами
     **/
    public static final String CHATS_DB = "Chats";

    /**
     * Класс отвечает за хранение констант - ключей для обращения к спискам значений в таблице
     **/
    public class ListTag {

        /**
         * Название списка с чатами
         **/
        public static final String COLLECTIONS_CHATS_TAG = "ListChats";

        /**
         * Название списка с сообщениями
         **/
        public static final String COLLECTIONS_MESSAGES_TAG = "ListMessages";
    }
}
