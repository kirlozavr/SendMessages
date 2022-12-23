package com.example.sendmessages.Mapping;

import com.example.sendmessages.DTO.ChatsDto;
import com.example.sendmessages.Entity.ChatsEntity;
import com.example.sendmessages.Interface.Mapping;

public class ChatsMapper implements Mapping<ChatsEntity, ChatsDto> {

    @Override
    public ChatsDto getEntityToDto(ChatsEntity entity) {
        return new ChatsDto(
                entity.getUsernameToWhom(),
                entity.getLastMessage()
        );
    }

    @Override
    public ChatsEntity getDtoToEntity(ChatsDto dto) {
        return null;
    }
}
