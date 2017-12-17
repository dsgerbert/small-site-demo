package ru.h562.smallsite;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"application.properties"})
public class SmallSiteConst {
    // Restrict length
    public static final int LEN_EMAIL = 120;
    public static final int LEN_TITLE = 40;
    public static final int LEN_TEXT = 255;
    public static final int LEN_PHONE = 60;
    public static final int LEN_WHEN_CALL = 80;
    public static final int LEN_NAME = 160;
    public static final int LEN_SUBJECT = 255;

    // replace empty strings
    public static final String VALUE_NULL = "<null>";

    public static final String ORDER_TYPE = "order_type";

    // Email params
    @Value("${app.email.from:}")
    public String EMAIL_FROM;
    @Value("${app.email.to:}")
    public String EMAIL_TO;
    @Value("${app.email.copy:}")
    public String EMAIL_COPY;
    @Value("${app.email.cc:}")
    public String EMAIL_CC;
    @Value("${app.email.bcc:}")
    public String EMAIL_BCC;
    @Value("${app.email.prefix:}")
    public String EMAIL_PREFIX;

    // Backup files names
    @Value("${task.backup.message:email_messages_backup.tab}")
    public String BACKUP_MESSAGE_FILE_NAME;
    @Value("${task.backup.message-data:email_messages_data_backup.tab}")
    public String BACKUP_MESSAGE_DATA_FILE_NAME;

    // Yandex payments
    @Value("${yandex.secret:}")
    public String YANDEX_SECRET;
    @Value("${yandex.wallet:}")
    public String YANDEX_WALLET;
    @Value("${yandex.success:}")
    public String YANDEX_SUCCESS;

    public SmallSiteConst() {
    }
}
