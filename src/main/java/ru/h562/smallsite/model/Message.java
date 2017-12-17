package ru.h562.smallsite.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Message from site visitors
 */
public class Message {
    private String id;
    private LocalDateTime dateTime;
    private MessageType type;
    private Collection<MessageData> data;

    private final String PHONE = "phone";
    private final String EMAIL = "email";
    private final String NAME = "name";
    private final String BODY = "body";
    private final String WHEN_CALL = "whenCall";
    private final String TITLE = "title";

    private final String NOTIFICATION_TYPE = "notification_type";
    private final String OPERATION_ID = "operation_id";
    private final String AMOUNT = "amount";
    private final String WITHDRAW_AMOUNT = "withdraw_amount";
    private final String CURRENCY = "currency";
    private final String DATETIME_PAYMENT = "datetime";
    private final String SENDER = "sender";
    private final String LABEL = "label";
    private final String UNACCEPTED = "unaccepted";
    private final String CODEPRO = "codepro";


    public Message() {
        this.data = new ArrayList<>();
    }

    public Message(MessageType type) {
        this();
        this.id = UUID.randomUUID().toString();
        this.dateTime = LocalDateTime.now();
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public MessageType getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Collection<MessageData> getData() {
        return data;
    }

    public void setData(Collection<MessageData> data) {
        if (data != null) {
            this.data = data;
        } else {
            this.data = new ArrayList<>();
        }
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message = (Message) o;

        return id.equals(message.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public void setTitle(String whenCall) {
        saveToData(TITLE, whenCall);
    }

    public void setPhone(String phone) {
        saveToData(PHONE, phone);
    }

    public void setEmail(String email) {
        saveToData(EMAIL, email);
    }

    public void setName(String name) {
        saveToData(NAME, name);
    }

    public void setBody(String body) {
        saveToData(BODY, body);
    }

    public void setWhenCall(String whenCall) {
        saveToData(WHEN_CALL, whenCall);
    }

    public void setNotificationType(String notificationType) {
        saveToData(NOTIFICATION_TYPE, notificationType);
    }

    public void setOperationId(String operationId) {
        saveToData(OPERATION_ID, operationId);
    }

    public void setAmount(String amount) {
        saveToData(AMOUNT, amount);
    }

    public void setWithdrawAmount(String withdrawAmount) {
        saveToData(WITHDRAW_AMOUNT, withdrawAmount);
    }

    public void setCurrency(String currency) {
        saveToData(CURRENCY, currency);
    }

    public void setDatetimePayment(String datetimePayment) {
        saveToData(DATETIME_PAYMENT, datetimePayment);
    }

    public void setSender(String sender) {
        saveToData(SENDER, sender);
    }

    public void setLabel(String label) {
        saveToData(LABEL, label);
    }

    public void setUnaccepted(String unaccepted) {
        saveToData(UNACCEPTED, unaccepted);
    }

    public void setCodePro(String codePro) {
        saveToData(CODEPRO, codePro);
    }

    public String getTitle() {
        return getFromData(TITLE);
    }

    public String getPhone() {
        return getFromData(PHONE);
    }

    public String getEmail() {
        return getFromData(EMAIL);
    }

    public String getName() {
        return getFromData(NAME);
    }

    public String getBody() {
        return getFromData(BODY);
    }

    public String getWhenCall() {
        return getFromData(WHEN_CALL);
    }

    public String getNotificationType() {
        return getFromData(NOTIFICATION_TYPE);
    }

    public String getOperationId() {
        return getFromData(OPERATION_ID);
    }

    public String getAmount() {
        return getFromData(AMOUNT);
    }

    public String getWithdrawAmount() {
        return getFromData(WITHDRAW_AMOUNT);
    }

    public String getCurrency() {
        return getFromData(CURRENCY);
    }

    public String getDatetimePayment() {
        return getFromData(DATETIME_PAYMENT);
    }

    public String getSender() {
        return getFromData(SENDER);
    }

    public String getLabel() {
        return getFromData(LABEL);
    }

    public String getUnaccepted() {
        return getFromData(UNACCEPTED);
    }

    public String getCodePro() {
        return getFromData(CODEPRO);
    }

    private void saveToData(String name, String value) {
        boolean update = false;
        for (MessageData messageData : this.data) {
            if (messageData.getName().equals(name)) {
                messageData.setValue(value);
                update = true;
                break;
            }
        }

        if (!update) {
            MessageData messageData = new MessageData(name, value);
            getData().add(messageData);
        }
    }

    private String getFromData(String name) {
        for (MessageData messageData : this.data) {
            if (messageData.getName().equals(name)) {
                return messageData.getValue();
           }
        }

        return "";
    }
}
