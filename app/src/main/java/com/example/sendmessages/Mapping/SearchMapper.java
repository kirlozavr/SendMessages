package com.example.sendmessages.Mapping;

import com.example.sendmessages.DTO.SearchDto;
import com.example.sendmessages.Entity.UserEntity;
import com.example.sendmessages.Interface.Mapping;

public class SearchMapper implements Mapping<UserEntity, SearchDto> {
    @Override
    public SearchDto getEntityToDto(UserEntity entity) {
        return new SearchDto(entity.getUsername());
    }

    @Override
    public UserEntity getDtoToEntity(SearchDto dto) {
        return null;
    }
}
