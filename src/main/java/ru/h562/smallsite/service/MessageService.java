package ru.h562.smallsite.service;

import ru.h562.smallsite.model.Message;
import ru.h562.smallsite.model.MessageType;

import java.util.Collection;

/**
 * Работа с сообщениями
 */
public interface MessageService {
    Collection<Message> findAll();
    Collection<Message> findByType(MessageType messageType);
    Message findById(String id);
    Message save(Message message);
    boolean delete(Message message);
}
