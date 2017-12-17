package ru.h562.smallsite.service;

import ru.h562.smallsite.model.Message;

/**
 * Передача сообщений через внутренюю очередь
 */
public interface MessageBusService {
    boolean putMessage(Message message);

    Message getMessage();
    boolean hasMessage();
}
