package com.example.sendmessages.Mapping;

import androidx.annotation.NonNull;

import com.example.sendmessages.Common.DateFormat;
import com.example.sendmessages.DTO.MessageDto;
import com.example.sendmessages.Entity.MessageEntity;
import com.example.sendmessages.Interface.Mapping;
import com.example.sendmessages.Security.PrivateKeys;
import com.example.sendmessages.Security.PublicKeys;

import java.time.ZonedDateTime;

import kotlin.Pair;

public class MessageMapper implements Mapping<MessageEntity, MessageDto> {

    private boolean isToday;

    @Override
    public MessageDto getEntityToDto(@NonNull MessageEntity entity, Pair<PublicKeys, PrivateKeys> pairKeys) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(
                keyGenerator.decrypt(
                        keyGenerator.stringToListBigInteger(entity.getDateTimeToDataBase()),
                        pairKeys.getSecond()
                ),
                DateFormat.getFormatFromDataBase()
        );

        if (isToday) {
            return new MessageDto(
                    keyGenerator.decrypt(
                            keyGenerator.stringToListBigInteger(entity.getMessage()), pairKeys.getSecond()
                    ),
                    DateFormat.getFormatToDateAndTime().format(zonedDateTime),
                    keyGenerator.decrypt(
                            keyGenerator.stringToListBigInteger(entity.getUsernameFrom()), pairKeys.getSecond()
                    ),
                    (entity.getUriImage() != null) ?
                            keyGenerator.decrypt(
                                    keyGenerator.stringToListBigInteger(entity.getUriImage()), pairKeys.getSecond()
                            ) : null
            );
        } else {
            return new MessageDto(
                    keyGenerator.decrypt(
                            keyGenerator.stringToListBigInteger(entity.getMessage()), pairKeys.getSecond()
                    ),
                    DateFormat.getFormatToTime().format(zonedDateTime),
                    keyGenerator.decrypt(
                            keyGenerator.stringToListBigInteger(entity.getUsernameFrom()), pairKeys.getSecond()
                    ),
                    (entity.getUriImage() != null) ?
                            keyGenerator.decrypt(
                                    keyGenerator.stringToListBigInteger(entity.getUriImage()), pairKeys.getSecond()
                            ) : null
            );
        }
    }

    @Override
    public MessageEntity getDtoToEntity(MessageDto dto, Pair<PublicKeys, PrivateKeys> pairKeys) {
        return new MessageEntity(
                keyGenerator.listBigIntegerToString(
                        keyGenerator.encrypt(dto.getMessage(), pairKeys.getFirst())
                ),
                keyGenerator.listBigIntegerToString(
                        keyGenerator.encrypt(dto.getDateTimeToDataBase(), pairKeys.getFirst())
                ),
                keyGenerator.listBigIntegerToString(
                        keyGenerator.encrypt(dto.getUsernameFrom(), pairKeys.getFirst())
                ),
                (dto.getUriImage() != null) ?
                        keyGenerator.listBigIntegerToString(
                                keyGenerator.encrypt(dto.getUriImage(), pairKeys.getFirst())
                        ) : null
        );
    }

    public void setIsToday(boolean isToday) {
        this.isToday = isToday;
    }
}
