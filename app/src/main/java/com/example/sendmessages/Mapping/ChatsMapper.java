package com.example.sendmessages.Mapping;

import androidx.annotation.NonNull;

import com.example.sendmessages.DTO.ChatsDto;
import com.example.sendmessages.Entity.ChatsEntity;
import com.example.sendmessages.General.DateFormat;
import com.example.sendmessages.Interface.Mapping;

import java.time.ZonedDateTime;

public class ChatsMapper implements Mapping<ChatsEntity, ChatsDto> {

    @Override
    public ChatsDto getEntityToDto(@NonNull ChatsEntity entity) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(
                entity.getTimeMessageToDataBase(),
                DateFormat.getFormatFromDataBase()
        );

        return new ChatsDto(
                entity.getUsernameToWhom(),
                entity.getLastMessage(),
                DateFormat.getFormatToMessages().format(zonedDateTime)
        );
    }

    @Override
    public ChatsEntity getDtoToEntity(ChatsDto dto) {
        return null;
    }
}
