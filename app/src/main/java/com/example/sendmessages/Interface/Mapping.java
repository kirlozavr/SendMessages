package com.example.sendmessages.Interface;

public interface Mapping <E, D>{

    D getEntityToDto(E entity);

    E getDtoToEntity(D dto);
}
