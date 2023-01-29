package com.example.sendmessages.Common;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Класс отвечает за хранение формата дат для класса DateTimeFormatter
 **/
public class DateFormat {

    public static DateTimeFormatter getFormatToDataBase() {
        return DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss.SSS OOOO", Locale.ENGLISH);
    }

    public static DateTimeFormatter getFormatFromDataBase() {
        return DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss.SSS zzzz", Locale.ENGLISH);
    }

    public static DateTimeFormatter getFormatToDateAndTime() {
        return DateTimeFormatter
                .ofPattern("dd-MM-yyyy HH:mm", Locale.ENGLISH);
    }

    public static DateTimeFormatter getFormatToTime() {
        return DateTimeFormatter
                .ofPattern("HH:mm", Locale.ENGLISH);
    }
}
