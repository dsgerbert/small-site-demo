package ru.h562.smallsite.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.h562.smallsite.SmallSiteConst;
import ru.h562.smallsite.SmallSiteUtils;
import ru.h562.smallsite.model.Message;
import ru.h562.smallsite.model.MessageType;
import ru.h562.smallsite.service.MessageBusService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class PostMessageController {

    private static Logger logger = LogManager.getLogger(PostMessageController.class);

    private static final String SUCCESS_TYPE = "alert-success";
    private static final String ERROR_TYPE = "alert-danger";
    private static final String WARNING_TYPE = "alert-warning";

    private static final String SUCCESS_TITLE_SEND_MESSAGE = "Сообщение отправлено";
    private static final String SUCCESS_TITLE_SEND_ORDER = "Заявка направлена на обработку";

    private static final String OK_SEND_MESSAGE = "Мы рассмотрим Ваше обращение в ближайщее время";
    private static final String SOON_ANSWER_MESSAGE = "Мы свяжемся с Вами в ближайщее время";

    private static final String ERROR_TITLE = "Ошибка";
    private static final String ERROR_TITLE_SEND = "Внимание";
    private static final String ERROR_SEND = "При отправке сообщения возникла ошибка";

    private static final String ERROR_PHONE_NUMBER = "Пожалуйста укажите номер телефона, например в виде +7(xxx)xxx-xx-xx";
    private static final String ERROR_BODY_TEXT = "Пожалуйста заполните текст сообщения";
    private static final String ERROR_NEED_PHONE = "Пожалуйста укажите Ваш телефон";
    private static final String ERROR_NEED_EMAIL = "Пожалуйста заполните адрес Email";
    private static final String ERROR_NEED_CONTACT = "Пожалуйста заполните контактные данные";

    private static final String PHONE_REG_EXP = "^(\\+7|8)?\\(?\\d{3}\\)?\\d{3}-?\\d{2}-?\\d{2}$";

    private void putOutInfo(RedirectAttributes attributes, String type, String title, String message) {
        attributes.addFlashAttribute("outMessageType", type);
        attributes.addFlashAttribute("outMessageTitle", title);
        attributes.addFlashAttribute("outMessage", message);
    }

    @Autowired
    private MessageBusService bus;

    @Autowired
    private SmallSiteConst smallSiteConst;

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public String postMessageForm(
            @RequestParam(value = "text", required = false, defaultValue = "") String body,
            @RequestParam(value = "title", required = false, defaultValue = "") String title,
            RedirectAttributes attributes) {

        body = SmallSiteUtils.restrictcStr(body, SmallSiteConst.LEN_TEXT);

        if (!body.equals(SmallSiteConst.VALUE_NULL)) {
            title = SmallSiteUtils.restrictcStr(title, SmallSiteConst.LEN_TITLE);

            Message message = new Message(MessageType.Message);
            message.setBody(body);
            message.setTitle(title);

            boolean msgSendOk = bus.putMessage(message);

            if (msgSendOk) {
                putOutInfo(attributes, SUCCESS_TYPE, SUCCESS_TITLE_SEND_MESSAGE, OK_SEND_MESSAGE);
            } else {
                putOutInfo(attributes, ERROR_TYPE, ERROR_TITLE_SEND, ERROR_SEND);
            }

            return "redirect:/";
        } else {
            putOutInfo(attributes, WARNING_TYPE, ERROR_TITLE, ERROR_BODY_TEXT);

            return "redirect:message";
        }
    }

    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public String postChatForm(
            @RequestParam(value = "text", required = false, defaultValue = "") String body,
            @RequestParam(value = "title", required = false, defaultValue = "") String title,
            @RequestParam(value = "email", required = false, defaultValue = "") String email,
            RedirectAttributes attributes) {

        email = SmallSiteUtils.restrictcStr(email, SmallSiteConst.LEN_EMAIL);

        if (!email.equals(SmallSiteConst.VALUE_NULL)) {
            body = SmallSiteUtils.restrictcStr(body, SmallSiteConst.LEN_TEXT);
            title = SmallSiteUtils.restrictcStr(title, SmallSiteConst.LEN_TITLE);

            Message message = new Message(MessageType.Email);
            message.setBody(body);
            message.setEmail(email);
            message.setTitle(title);

            boolean msgSendOk = bus.putMessage(message);

            if (msgSendOk) {
                putOutInfo(attributes, SUCCESS_TYPE, SUCCESS_TITLE_SEND_MESSAGE, SOON_ANSWER_MESSAGE);
            } else {
                putOutInfo(attributes, ERROR_TYPE, ERROR_TITLE_SEND, ERROR_SEND);
            }

            return "redirect:/";
        } else {
            putOutInfo(attributes, WARNING_TYPE, ERROR_TITLE, ERROR_NEED_EMAIL);
            return "redirect:email";
        }
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public String postOrderForm(
            @RequestParam(value = "text", required = false, defaultValue = "") String body,
            @RequestParam(value = "title", required = false, defaultValue = "") String title,
            @RequestParam(value = "name", required = false, defaultValue = "") String name,
            @RequestParam(value = "phone", required = false, defaultValue = "") String phone,
            @RequestParam(value = "email", required = false, defaultValue = "") String email,
            RedirectAttributes attributes) {

        email = SmallSiteUtils.restrictcStr(email, SmallSiteConst.LEN_EMAIL);
        phone = SmallSiteUtils.restrictcStr(phone, SmallSiteConst.LEN_PHONE);

        if (!email.equals(SmallSiteConst.VALUE_NULL) || !phone.equals(SmallSiteConst.VALUE_NULL)) {

            if (!phone.equals(SmallSiteConst.VALUE_NULL)) {
                Pattern patternPhone = Pattern.compile(PHONE_REG_EXP);
                Matcher matcher = patternPhone.matcher(phone);
                if (!matcher.find()) {
                    // телефон, по которому не позвонить
                    putOutInfo(attributes, WARNING_TYPE, ERROR_TITLE, ERROR_PHONE_NUMBER);
                    return "redirect:order";
                }
            }

            name = SmallSiteUtils.restrictcStr(name, SmallSiteConst.LEN_NAME);
            body = SmallSiteUtils.restrictcStr(body, SmallSiteConst.LEN_TEXT);
            title = SmallSiteUtils.restrictcStr(title, SmallSiteConst.LEN_TITLE);

            Message message = new Message(MessageType.Order);
            message.setBody(body);
            message.setName(name);
            message.setEmail(email);
            message.setPhone(phone);
            message.setTitle(title);

            boolean msgSendOk = bus.putMessage(message);

            if (msgSendOk) {
                putOutInfo(attributes, SUCCESS_TYPE, SUCCESS_TITLE_SEND_ORDER, SOON_ANSWER_MESSAGE);
            } else {
                putOutInfo(attributes, ERROR_TYPE, ERROR_TITLE_SEND, ERROR_SEND);
            }

            return "redirect:/";
        } else {
            putOutInfo(attributes, WARNING_TYPE, ERROR_TITLE, ERROR_NEED_CONTACT);
            return "redirect:order";
        }
    }

    @RequestMapping(value = "/phone", method = RequestMethod.POST)
    public String postPhoneForm(
            @RequestParam(value = "title", required = false, defaultValue = "") String title,
            @RequestParam(value = "text", required = false, defaultValue = "") String body,
            @RequestParam(value = "phone", required = false, defaultValue = "") String phone,
            @RequestParam(value = "whenCall", required = false, defaultValue = "") String whenCall,
            RedirectAttributes attributes) {

        phone = SmallSiteUtils.restrictcStr(phone, SmallSiteConst.LEN_PHONE);

        if (!phone.equals(SmallSiteConst.VALUE_NULL)) {

            Pattern patternPhone = Pattern.compile(PHONE_REG_EXP);
            Matcher matcher = patternPhone.matcher(phone);
            if (!matcher.find()) {
                // телефон, по которому не позвонить
                putOutInfo(attributes, WARNING_TYPE, ERROR_TITLE, ERROR_PHONE_NUMBER);
                return "redirect:phone";
            }

            whenCall = SmallSiteUtils.restrictcStr(whenCall, SmallSiteConst.LEN_WHEN_CALL);
            body = SmallSiteUtils.restrictcStr(body, SmallSiteConst.LEN_TEXT);
            title = SmallSiteUtils.restrictcStr(title, SmallSiteConst.LEN_TITLE);

            Message message = new Message(MessageType.Phone);
            message.setBody(body);
            message.setPhone(phone);
            message.setWhenCall(whenCall);
            message.setTitle(title);

            boolean msgSendOk = bus.putMessage(message);

            if (msgSendOk) {
                putOutInfo(attributes, SUCCESS_TYPE, SUCCESS_TITLE_SEND_MESSAGE, SOON_ANSWER_MESSAGE);
            } else {
                putOutInfo(attributes, ERROR_TYPE, ERROR_TITLE_SEND, ERROR_SEND);
            }

            return "redirect:/";
        } else {
            putOutInfo(attributes, WARNING_TYPE, ERROR_TITLE, ERROR_NEED_PHONE);
            return "redirect:phone";
        }
    }

    @RequestMapping(value = "/success_payment", method = RequestMethod.POST)
    public String successPayment(
            @RequestParam(value = "notification_type", required = false, defaultValue = "") String notificationType,
            @RequestParam(value = "operation_id", required = false, defaultValue = "") String operationId,
            @RequestParam(value = "amount", required = false, defaultValue = "") String amount,
            @RequestParam(value = "withdraw_amount", required = false, defaultValue = "") String withdrawAmount,
            @RequestParam(value = "currency", required = false, defaultValue = "") String currency,
            @RequestParam(value = "datetime", required = false, defaultValue = "") String datetimePayment,
            @RequestParam(value = "sender", required = false, defaultValue = "") String sender,
            @RequestParam(value = "label", required = false, defaultValue = "") String label,
            @RequestParam(value = "codepro",  required = false, defaultValue = "") String codepro,
            @RequestParam(value = "sha1_hash",  required = false, defaultValue = "") String sha1Hash,
            @RequestParam(value = "unaccepted", required = false, defaultValue = "false") String unaccepted) {

        // проверка подлинности
        StringBuilder builder = new StringBuilder();
        builder.append(notificationType);
        builder.append('&');
        builder.append(operationId);
        builder.append('&');
        builder.append(amount);
        builder.append('&');
        builder.append(currency);
        builder.append('&');
        builder.append(datetimePayment);
        builder.append('&');
        builder.append(sender);
        builder.append('&');
        builder.append(codepro);
        builder.append('&');
        builder.append(smallSiteConst.YANDEX_SECRET);
        builder.append('&');
        builder.append(label);


        if (DigestUtils.sha1Hex(builder.toString()).equalsIgnoreCase(sha1Hash)) {
            try {
                Message message = new Message(MessageType.Payment);
                message.setTitle("Получен платеж");
                message.setNotificationType(notificationType);
                message.setOperationId(operationId);
                message.setAmount(amount);
                message.setWithdrawAmount(withdrawAmount);
                message.setCurrency(currency);
                message.setDatetimePayment(datetimePayment);
                message.setSender(sender);
                message.setLabel(label);

                bus.putMessage(message);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        } else {
            builder.append(" - не пройден контроль подлинности");
            logger.error(builder.toString());
        }

        return "index";
    }
}
