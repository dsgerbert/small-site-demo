package ru.h562.smallsite.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.h562.smallsite.model.Message;
import ru.h562.smallsite.service.MessageBusService;

import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class MessageBusServiceImpl implements MessageBusService {

    @Autowired
    private ConcurrentLinkedQueue<Message> messages;

    @Override
    public boolean putMessage(Message message) {
        return messages.add(message);
    }

    @Override
    public Message getMessage() {
        return messages.poll();
    }

    @Override
    public boolean hasMessage() {
        return !messages.isEmpty();
    }
}
