package ru.h562.smallsite.dao;

import ru.h562.smallsite.model.Message;
import ru.h562.smallsite.model.MessageType;

import java.util.Collection;

public interface MessageDao {
    Collection<Message> findAll();
    Collection<Message> findByType(MessageType messageType);
    Message findById(String id);
    Message save(Message message);
    boolean delete(Message message);
}
