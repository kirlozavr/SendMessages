package com.example.sendmessages.Interface;

/**
 * Интерфейс отвечает за обработку нажатий элементов в списке
 *
 * @param <E> Сущность с которой происходит работа в списке
 **/
public interface OnClickListener<E> {

    /**
     * @param entity   Сущность с которой происходит работа в списке
     * @param position Позиция элемента в списке
     **/
    void onClick(E entity, int position);
}
