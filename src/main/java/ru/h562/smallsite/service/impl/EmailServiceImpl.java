package ru.h562.smallsite.service.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.h562.smallsite.model.EmailMessage;
import ru.h562.smallsite.service.EmailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    private static Logger logger = LogManager.getLogger(EmailService.class);

    @Override
    public void sendMessage(EmailMessage msg) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            if (!msg.getFrom().isEmpty()) {
                helper.setFrom(msg.getFrom());
            }

            if (!msg.getTo().isEmpty()) {
                helper.setTo(msg.getTo());
            }

            if (!msg.getCc().isEmpty()) {
                helper.setCc(msg.getCc());
            }

            if (!msg.getBcc().isEmpty()) {
                helper.setBcc(msg.getBcc());
            }

            helper.setSubject(msg.getSubject());
            helper.setText(msg.getText(), true);

            emailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            logger.error(ex.getMessage());
        }
    }
}
