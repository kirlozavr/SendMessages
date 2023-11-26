package com.example.sendmessages.Mapping;

import com.example.sendmessages.DTO.SearchDto;
import com.example.sendmessages.Entity.UserEntity;

public class SearchMapper {

    public SearchDto getEntityToDto(UserEntity entity) {
        return new SearchDto(entity.getUsername());
    }

    public UserEntity getDtoToEntity(SearchDto dto) {
        return null;
    }
}
