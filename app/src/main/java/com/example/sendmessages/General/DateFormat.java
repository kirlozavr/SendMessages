package com.example.sendmessages.General;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateFormat {

    public static DateTimeFormatter getFormatToDataBase(){
        return DateTimeFormatter
                .ofPattern("dd-MM-yyyy HH:mm:ss.SSS OOOO", Locale.ENGLISH);
    }

    public static DateTimeFormatter getFormatFromDataBase(){
        return DateTimeFormatter
                .ofPattern("dd-MM-yyyy HH:mm:ss.SSS zzzz", Locale.ENGLISH);
    }

    public static DateTimeFormatter getFormatToDateAndTime(){
        return DateTimeFormatter
                .ofPattern("dd-MM-yyyy HH:mm", Locale.ENGLISH);
    }

    public static DateTimeFormatter getFormatToTime(){
        return DateTimeFormatter
                .ofPattern("HH:mm", Locale.ENGLISH);
    }
}
