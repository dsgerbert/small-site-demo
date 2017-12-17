package ru.h562.smallsite.service;

import ru.h562.smallsite.model.EmailMessage;

/**
 * Отправка сообщений по email
 */
public interface EmailService {
    void sendMessage(EmailMessage msg);
}
