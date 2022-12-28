package com.example.sendmessages.General;

import android.content.Context;
import android.content.SharedPreferences;

public class Data {

    private static final String SETTINGS_PREFERENCES = "settings";
    public static final String SAVE_USERNAME = "saveUsername";
    public static final String USERNAME = "username";
    public static final String IS_LAUNCHED = "launched";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(
                SETTINGS_PREFERENCES,
                Context.MODE_PRIVATE
        );
    }

    public static boolean isLaunched(Context context) {
        return getSharedPreferences(context)
                .getBoolean(Data.IS_LAUNCHED, false);
    }

    public static void putStringPreferences(Context context, String key, String value) {
        SharedPreferences.Editor editor =
                getSharedPreferences(context).edit();
        editor
                .putString(key, value)
                .apply();
    }

    public static String getStringPreferences(Context context, String key) {
        return getSharedPreferences(context)
                .getString(key, "");
    }

    public static void putBooleanPreferences(Context context, String key, boolean value) {
        SharedPreferences.Editor editor =
                getSharedPreferences(context).edit();
        editor
                .putBoolean(key, value)
                .apply();
    }

    public static boolean getBooleanPreferences(Context context, String key) {
        return getSharedPreferences(context)
                .getBoolean(key, false);
    }

    public static void removePreferences(Context context, String key) {
        getSharedPreferences(context)
                .edit()
                .remove(key)
                .apply();
    }

    public static void removeAll(Context context) {
        getSharedPreferences(context)
                .edit()
                .remove(USERNAME)
                .apply();
        getSharedPreferences(context)
                .edit()
                .remove(IS_LAUNCHED)
                .apply();
    }
}
