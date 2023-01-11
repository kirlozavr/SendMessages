package com.example.sendmessages.Mapping;

import androidx.annotation.NonNull;

import com.example.sendmessages.DTO.MessageDto;
import com.example.sendmessages.Entity.MessageEntity;
import com.example.sendmessages.Common.DateFormat;
import com.example.sendmessages.Interface.Mapping;

import java.time.ZonedDateTime;

public class MessageMapper implements Mapping<MessageEntity, MessageDto> {

    private boolean isToday;

    @Override
    public MessageDto getEntityToDto(@NonNull MessageEntity entity) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(
                entity.getDateTimeToDataBase(),
                DateFormat.getFormatFromDataBase()
        );

        if (isToday) {
            return new MessageDto(
                    entity.getMessage(),
                    DateFormat.getFormatToDateAndTime().format(zonedDateTime),
                    entity.getUsernameFrom(),
                    entity.getUriImage()
            );
        } else {
            return new MessageDto(
                    entity.getMessage(),
                    DateFormat.getFormatToTime().format(zonedDateTime),
                    entity.getUsernameFrom(),
                    entity.getUriImage()
            );
        }
    }

    @Override
    public MessageEntity getDtoToEntity(MessageDto dto) {
        return null;
    }

    public void setIsToday(boolean isToday) {
        this.isToday = isToday;
    }
}
