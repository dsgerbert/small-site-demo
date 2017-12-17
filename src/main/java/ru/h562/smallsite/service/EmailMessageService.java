package ru.h562.smallsite.service;

import ru.h562.smallsite.model.EmailMessage;
import ru.h562.smallsite.model.Message;
import ru.h562.smallsite.model.MessageData;
import ru.h562.smallsite.model.MessageType;

import java.util.Collection;

/**
 * Работа с Email сообщениями
 */
public interface EmailMessageService {
    EmailMessage createEmail(Message message);
    Collection<EmailMessage> findAll();
    Collection<EmailMessage> findByType(MessageType messageType);
    EmailMessage findById(String id);
    EmailMessage save(EmailMessage msg);
    boolean delete(EmailMessage msg);
    Collection<EmailMessage> findAllFromBackup();
    Collection<MessageData> findBackupMessageDataById(String id);
    boolean backup(EmailMessage msg);
    boolean deleteBackupMessage(EmailMessage backupMsg);
}
