package com.example.sendmessages.Mapping;

import androidx.annotation.NonNull;

import com.example.sendmessages.DTO.ChatsDto;
import com.example.sendmessages.Entity.ChatsEntity;
import com.example.sendmessages.Common.DateFormat;
import com.example.sendmessages.Interface.Mapping;

import java.time.ZonedDateTime;

public class ChatsMapper implements Mapping<ChatsEntity, ChatsDto> {

    private boolean isToday;

    @Override
    public ChatsDto getEntityToDto(@NonNull ChatsEntity entity) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(
                entity.getTimeMessageToDataBase(),
                DateFormat.getFormatFromDataBase()
        );
        if (isToday) {
            return new ChatsDto(
                    entity.getUsernameToWhom(),
                    entity.getLastMessage(),
                    DateFormat.getFormatToDateAndTime().format(zonedDateTime)
            );
        } else {
            return new ChatsDto(
                    entity.getUsernameToWhom(),
                    entity.getLastMessage(),
                    DateFormat.getFormatToTime().format(zonedDateTime)
            );
        }
    }

    @Override
    public ChatsEntity getDtoToEntity(ChatsDto dto) {
        return null;
    }

    public void setIsToday(boolean isToday) {
        this.isToday = isToday;
    }
}
