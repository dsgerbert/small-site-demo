package ru.h562.smallsite.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.h562.smallsite.dao.MessageDao;
import ru.h562.smallsite.model.Message;
import ru.h562.smallsite.model.MessageType;
import ru.h562.smallsite.service.MessageService;

import java.util.Collection;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao dao;

    @Override
    public Collection<Message> findAll() {
        return dao.findAll();
    }

    @Override
    public Collection<Message> findByType(MessageType messageType) {
        return dao.findByType(messageType);
    }

    @Override
    public Message findById(String id) {
        return dao.findById(id);
    }

    @Override
    public Message save(Message message) {
        return dao.save(message);
    }

    @Override
    public boolean delete(Message message) {
        return dao.delete(message);
    }
}
