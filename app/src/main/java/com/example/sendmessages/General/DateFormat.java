package com.example.sendmessages.General;

import java.time.format.DateTimeFormatter;

public class DateFormat {

    public static DateTimeFormatter getFormatToDataBase(){
        return DateTimeFormatter
                .ofPattern("dd-MM-yyyy HH:mm:ss:SSSS OOOO");
    }

    public static DateTimeFormatter getFormatToMessages(){
        return DateTimeFormatter
                .ofPattern("dd-MM-yyyy HH:mm");
    }

    public static DateTimeFormatter getFormatFromDataBase(){
        return DateTimeFormatter
                .ofPattern("dd-MM-yyyy HH:mm:ss:SSSS zzzz");
    }
}
