package com.example.sendmessages.Interface;

import com.example.sendmessages.Security.KeyGenerator;
import com.example.sendmessages.Security.PrivateKeys;
import com.example.sendmessages.Security.PublicKeys;

import kotlin.Pair;

/**
 * Интерфейс описывает основные методы для мапперов
 *
 * @param <E> Entity
 * @param <D> DTO
 **/

public interface Mapping<E, D> {

    KeyGenerator keyGenerator = new KeyGenerator();
    /**
     * Метод отвечает за преобразование Entity в DTO
     **/
    D getEntityToDto(E entity, Pair<PublicKeys, PrivateKeys> pairKeys);

    /**
     * Метод отвечает за преобразование DTO в Entity
     **/
    E getDtoToEntity(D dto, Pair<PublicKeys, PrivateKeys> pairKeys);
}
