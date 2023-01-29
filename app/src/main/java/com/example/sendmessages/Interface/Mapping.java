package com.example.sendmessages.Interface;

/**
 * Интерфейс описывает основные методы для мапперов
 *
 * @param <E> Entity
 * @param <D> DTO
 **/

public interface Mapping<E, D> {

    /**
     * Метод отвечает за преобразование Entity в DTO
     **/
    D getEntityToDto(E entity);

    /**
     * Метод отвечает за преобразование DTO в Entity
     **/
    E getDtoToEntity(D dto);
}
