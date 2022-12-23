package com.example.sendmessages.Mapping;

import androidx.annotation.NonNull;

import com.example.sendmessages.DTO.MessageDto;
import com.example.sendmessages.Entity.MessageEntity;
import com.example.sendmessages.General.DateFormat;
import com.example.sendmessages.Interface.Mapping;

import java.time.ZonedDateTime;

public class MessageMapper implements Mapping<MessageEntity, MessageDto> {

    @Override
    public MessageDto getEntityToDto(@NonNull MessageEntity entity) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(
                entity.getDateTimeToDataBase(),
                DateFormat.getFormatFromDataBase()
                );

        return new MessageDto(
                entity.getMessage(),
                DateFormat.getFormatToMessages().format(zonedDateTime),
                entity.getUsernameFrom()
        );
    }

    @Override
    public MessageEntity getDtoToEntity(MessageDto dto) {
        return null;
    }
}
