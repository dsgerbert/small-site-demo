package ru.h562.smallsite.model;

import java.time.LocalDateTime;

public class EmailMessage {
    private String id;
    private LocalDateTime dateTime;
    private MessageType type;
    private String from;
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String text;
    private LocalDateTime sent;

    public EmailMessage(){
        this.from = "";
        this.to="";
        this.cc = "";
        this.bcc = "";
        this.subject="";
        this.text="";
    }

    public EmailMessage(Message message){
        this();
        this.id = message.getId();
        this.dateTime = message.getDateTime();
        this.type = message.getType();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getSent() {
        return sent;
    }

    public void setSent(LocalDateTime sent) {
        this.sent = sent;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    @Override
    public String toString() {
        // subject and message text does't put in backup file
        return String.format(
                "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
                id,
                dateTime.toString(),
                type.getStrName(),
                from,
                to,
                cc,
                bcc,
                sent.toString()
        );
    }
}
