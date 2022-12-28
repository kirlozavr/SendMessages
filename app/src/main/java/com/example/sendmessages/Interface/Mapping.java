package com.example.sendmessages.Interface;

/**
 *  Интерфейс описывает основные методы для мапперов
 * **/

public interface Mapping<E, D> {

    D getEntityToDto(E entity);

    E getDtoToEntity(D dto);
}
