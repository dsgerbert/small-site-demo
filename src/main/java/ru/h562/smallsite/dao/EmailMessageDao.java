package ru.h562.smallsite.dao;

import ru.h562.smallsite.model.EmailMessage;
import ru.h562.smallsite.model.MessageData;
import ru.h562.smallsite.model.MessageType;

import java.util.Collection;

public interface EmailMessageDao {
    Collection<EmailMessage> findAll();
    Collection<EmailMessage> findByType(MessageType messageType);
    EmailMessage findById(String id);
    EmailMessage save(EmailMessage message);
    boolean delete(EmailMessage message);

    Collection<EmailMessage> findAllFromBackup();
    Collection<MessageData> findBackupMessageDataById(String id);
    boolean backup(EmailMessage message);
    boolean deleteBackupMessage(EmailMessage backupMsg);
}
