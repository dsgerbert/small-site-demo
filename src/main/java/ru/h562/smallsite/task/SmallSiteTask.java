package ru.h562.smallsite.task;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import ru.h562.smallsite.SmallSiteConst;
import ru.h562.smallsite.model.EmailMessage;
import ru.h562.smallsite.model.Message;
import ru.h562.smallsite.model.MessageData;
import ru.h562.smallsite.service.*;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;

public class SmallSiteTask {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageBusService messageBusService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailMessageService emailMessageService;

    @Autowired
    private DBService dbService;

    @Autowired
    private SmallSiteConst smallSiteConst;

    private static Logger logger = LogManager.getLogger(SmallSiteTask.class);

    /**
     * Takes messages from bus and put to DB
     */
    @Scheduled(fixedRateString = "${task.process.messages}")
    public void processMessages() {
        while (messageBusService.hasMessage()) {
            Message message = messageBusService.getMessage();
            messageService.save(message);
            logger.info("Save message to queue.");
        }
    }

    /**
     * Prepares emails messages
     */
    @Scheduled(fixedRateString = "${task.create.emails}")
    public void createEmailMessages() {
        Collection<Message> list = messageService.findAll();
        for (Message message : list) {
            EmailMessage msg = emailMessageService.createEmail(message);
            emailMessageService.save(msg);
            logger.info("Create email message " + msg.getId());
            messageService.delete(message);
            logger.info("Delete message " + message.getId());
        }
    }

    /**
     * Sends the emails
     */
    @Scheduled(fixedRateString = "${task.send.emails}")
    public void sendEmailMessages() {
        Collection<EmailMessage> list = emailMessageService.findAll();
        for (EmailMessage msg : list) {
            try {
                emailService.sendMessage(msg);
                msg.setSent(LocalDateTime.now());
                emailMessageService.backup(msg);
                emailMessageService.delete(msg);
            } catch (Exception ex) {
                logger.error("Error sending email. " + ex.getMessage());
                System.out.println(ex.getMessage());
                break; // проблемы с отправкой - попробуем в другой раз
            }
        }
    }

    /**
     * Backup sent messages in to file
     */
    @Scheduled(cron = "${task.backup.emails}")
    public void backupEmails() {
        try {
            try (FileWriter wr = new FileWriter(smallSiteConst.BACKUP_MESSAGE_FILE_NAME, true);
                 FileWriter wr_data = new FileWriter(smallSiteConst.BACKUP_MESSAGE_DATA_FILE_NAME, true)) {
                for (EmailMessage backupMsg : emailMessageService.findAllFromBackup()) {
                    wr.write(backupMsg.toString());

                    for (MessageData msgData : emailMessageService.findBackupMessageDataById(backupMsg.getId())) {
                        wr_data.write(backupMsg.getId());
                        wr_data.write('\t');
                        wr_data.write(msgData.toString());
                    }

                    emailMessageService.deleteBackupMessage(backupMsg);
                }
            }
        } catch (IOException ex) {
            logger.error("Error backup messages. " + ex.getMessage());
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Compact DB
    /* doesn't work properly
    */
    public void compactDB() {
        dbService.ShutdownCompactDB();
    }
}
