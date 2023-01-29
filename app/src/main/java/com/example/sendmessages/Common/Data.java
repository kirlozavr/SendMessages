package com.example.sendmessages.Common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Класс отвечает за сохранение локальных данных,
 * необходимых для работы приложения.
 **/

public class Data {

    private static final String SETTINGS_PREFERENCES = "settings";
    /**
     * Ключ сохраненного логина пользователя, используется в случае когда нажата кнопка "Запомнить меня"
     **/
    public static final String SAVE_USERNAME = "saveUsername";
    /**
     * Ключ сохраненного логина пользователя, используется при любом входе пользователя в аккаунт
     **/
    public static final String USERNAME = "username";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(
                SETTINGS_PREFERENCES,
                Context.MODE_PRIVATE
        );
    }

    /**
     * Метод отвечает за запись String значений
     *
     * @param context Контекст активити из которой хотим сохранить значения
     * @param key     Ключ параметра который мы хотим сохранить
     * @param value   Значение которое нам необходимо сохранить
     **/
    public static void putStringPreferences(Context context, String key, String value) {
        SharedPreferences.Editor editor =
                getSharedPreferences(context).edit();
        editor
                .putString(key, value)
                .apply();
    }

    /**
     * Метод отвечает за получение String значений
     *
     * @param context Контекст активити из которой хотим получить значения
     * @param key     Ключ параметра который мы хотим получить
     **/
    public static String getStringPreferences(Context context, String key) {
        return getSharedPreferences(context)
                .getString(key, "");
    }

    /**
     * Метод отвечает за запись Boolean значений
     *
     * @param context Контекст активити из которой хотим сохранить значения
     * @param key     Ключ параметра который мы хотим сохранить
     * @param value   Значение которое нам необходимо сохранить
     **/
    public static void putBooleanPreferences(Context context, String key, boolean value) {
        SharedPreferences.Editor editor =
                getSharedPreferences(context).edit();
        editor
                .putBoolean(key, value)
                .apply();
    }

    /**
     * Метод отвечает за получение Boolean значений
     *
     * @param context Контекст активити из которой хотим получить значения
     * @param key     Ключ параметра который мы хотим получить
     **/
    public static boolean getBooleanPreferences(Context context, String key) {
        return getSharedPreferences(context)
                .getBoolean(key, false);
    }

    /**
     * Метод отвечает за удаление значений по ключу
     *
     * @param context Контекст активити из которой хотим удалить значения
     * @param key     Ключ параметра который мы хотим удалить
     **/
    public static void removePreferences(Context context, String key) {
        getSharedPreferences(context)
                .edit()
                .remove(key)
                .apply();
    }
}
