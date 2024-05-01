package com.example.sendmessages.Mapping;

import androidx.annotation.NonNull;

import com.example.sendmessages.Common.DateFormat;
import com.example.sendmessages.DTO.ChatsDto;
import com.example.sendmessages.Entity.ChatsEntity;
import com.example.sendmessages.Interface.Mapping;
import com.example.sendmessages.Security.PrivateKeys;
import com.example.sendmessages.Security.PublicKeys;

import java.time.ZonedDateTime;

import kotlin.Pair;

public class ChatsMapper implements Mapping<ChatsEntity, ChatsDto> {

    private boolean isToday;

    @Override
    public ChatsDto getEntityToDto(@NonNull ChatsEntity entity, Pair<PublicKeys, PrivateKeys> pairKeys) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(
                keyGenerator.decrypt(
                        keyGenerator.stringToListBigInteger(entity.getTimeMessage()), pairKeys.getSecond()
                ),
                DateFormat.getFormatFromDataBase()
        );
        if (isToday) {
            return new ChatsDto(
                    entity.getChatId(),
                    entity.getUsernameToWhom(),
                    (entity.getLastMessage().length() != 0) ?
                            keyGenerator.decrypt(
                                    keyGenerator.stringToListBigInteger(entity.getLastMessage()), pairKeys.getSecond()
                            ) : "",
                    DateFormat.getFormatToDateAndTime().format(zonedDateTime),
                    entity.getLineKeys()
            );
        } else {
            return new ChatsDto(
                    entity.getChatId(),
                    entity.getUsernameToWhom(),
                    (entity.getLastMessage().length() != 0) ?
                            keyGenerator.decrypt(
                                    keyGenerator.stringToListBigInteger(entity.getLastMessage()), pairKeys.getSecond()
                            ) : "",
                    DateFormat.getFormatToTime().format(zonedDateTime),
                    entity.getLineKeys()
            );
        }
    }

    @Override
    public ChatsEntity getDtoToEntity(ChatsDto dto, Pair<PublicKeys, PrivateKeys> pairKeys) {
        return new ChatsEntity(
                dto.getIdChats(),
                dto.getUsernameToWhom(),
                keyGenerator.listBigIntegerToString(
                        keyGenerator.encrypt(dto.getLastMessage(), pairKeys.getFirst())
                ),
                keyGenerator.listBigIntegerToString(
                        keyGenerator.encrypt(dto.getTimeMessageToDataBase(), pairKeys.getFirst())
                ),
                dto.getLineKeys()
        );
    }

    public void setIsToday(boolean isToday) {
        this.isToday = isToday;
    }
}
