package ru.h562.smallsite.service.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import ru.h562.smallsite.SmallSiteConst;
import ru.h562.smallsite.SmallSiteUtils;
import ru.h562.smallsite.dao.EmailMessageDao;
import ru.h562.smallsite.model.EmailMessage;
import ru.h562.smallsite.model.Message;
import ru.h562.smallsite.model.MessageData;
import ru.h562.smallsite.model.MessageType;
import ru.h562.smallsite.service.EmailMessageService;

import java.util.Collection;

@Service
public class EmailMessageServiceImpl implements EmailMessageService {
    @Autowired
    private EmailMessageDao dao;

    @Autowired
    private SmallSiteConst smallSiteConst;

    @Autowired
    SpringTemplateEngine templateEngine;

    private static Logger logger = LogManager.getLogger(EmailMessageService.class);

    @Override
    public EmailMessage createEmail(Message message) {
        EmailMessage msg = new EmailMessage(message);
        msg.setFrom(smallSiteConst.EMAIL_FROM);
        msg.setTo(smallSiteConst.EMAIL_TO);
        if (!smallSiteConst.EMAIL_CC.isEmpty()) {
            msg.setBcc(smallSiteConst.EMAIL_CC);
        }
        if (!smallSiteConst.EMAIL_BCC.isEmpty()) {
            msg.setBcc(smallSiteConst.EMAIL_BCC);
        }

        String subject = smallSiteConst.EMAIL_PREFIX + message.getType().getStrName();

        Context ctx = new Context();

        String template = "";
        switch (message.getType()) {
            case Phone:
                subject += ": " + message.getPhone() + " " + message.getTitle();
                template = "email_t_phone";
                break;
            case Order:
                subject += ": " + message.getPhone() + " " + message.getEmail() + " " + message.getTitle();
                template = "email_t_order";
                break;
            case Email:
                subject += ": " + message.getTitle();
                template = "email_t_email";
                break;
            case Message:
                subject += ": " + message.getTitle();
                template = "email_t_message";
                break;
            case Payment:
                subject += ": " + message.getTitle();
                template = "email_t_payment";
                break;
        }

        subject = SmallSiteUtils.restrictcStr(subject, smallSiteConst.LEN_SUBJECT);
        msg.setSubject(subject);

        ctx.setVariable("title", message.getTitle());
        ctx.setVariable("id", message.getId());
        ctx.setVariable("type", message.getType());
        ctx.setVariable("dateTime", message.getDateTime());

        if (message.getType() == MessageType.Payment) {
            ctx.setVariable("notification_type", message.getNotificationType());
            ctx.setVariable("operation_id", message.getOperationId());
            ctx.setVariable("amount", message.getAmount());
            ctx.setVariable("withdraw_amount", message.getWithdrawAmount());
            ctx.setVariable("currency", message.getCurrency());
            ctx.setVariable("datetimePayment", message.getDatetimePayment());
            ctx.setVariable("sender", message.getSender());
            ctx.setVariable("label", message.getLabel());
            ctx.setVariable("codepro", message.getCodePro());
            ctx.setVariable("unaccepted", message.getUnaccepted());
            ctx.setVariable("yandexWallet", smallSiteConst.YANDEX_WALLET);
        } else {
            ctx.setVariable("name", message.getName());
            ctx.setVariable("phone", message.getPhone());
            ctx.setVariable("whenCall", message.getWhenCall());
            ctx.setVariable("email", message.getEmail());
            ctx.setVariable("body", message.getBody());
        }

        try {
            String text = templateEngine.process(template, ctx);
            msg.setText(text);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        return msg;
    }

    @Override
    public Collection<EmailMessage> findAll() {
        return dao.findAll();
    }

    @Override
    public Collection<EmailMessage> findByType(MessageType messageType) {
        return dao.findByType(messageType);
    }

    @Override
    public EmailMessage findById(String id) {
        return dao.findById(id);
    }

    @Override
    public EmailMessage save(EmailMessage msg) {
        return dao.save(msg);
    }

    @Override
    public boolean delete(EmailMessage msg) {
        return dao.delete(msg);
    }

    @Override
    public Collection<EmailMessage> findAllFromBackup() {
        return dao.findAllFromBackup();
    }

    @Override
    public Collection<MessageData> findBackupMessageDataById(String id) {
        return dao.findBackupMessageDataById(id);
    }

    @Override
    public boolean backup(EmailMessage msg) {
        return dao.backup(msg);
    }

    @Override
    public boolean deleteBackupMessage(EmailMessage backupMsg) {
        return dao.deleteBackupMessage(backupMsg);
    }
}
