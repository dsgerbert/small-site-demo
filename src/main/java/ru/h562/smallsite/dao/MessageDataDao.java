package ru.h562.smallsite.dao;

import ru.h562.smallsite.model.Message;

public interface MessageDataDao {
    void fillMessageData(Message message);
    Message save(Message message);
    boolean delete(Message message);
}
