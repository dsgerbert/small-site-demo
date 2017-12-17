package ru.h562.smallsite.model;

/**
 * Message - сообщение от сайта
 * Phone   - обратный звонок
 * Email   - связь по электронной почте
 * Order   - заказ
 */
public enum MessageType {
    Message,
    Phone,
    Email,
    Order,
    Payment;

    public String getStrName() {
        String value = "";

        switch (this) {
            case Message:
                value = "Сайт";
                break;
            case Phone:
                value = "Звонок";
                break;
            case Email:
                value = "Email";
                break;
            case Order:
                value = "Заказ";
                break;
            case Payment:
                value = "Платеж";
                break;
        }

        return value;
    }
}
