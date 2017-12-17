package ru.h562.smallsite.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.h562.smallsite.dao.EmailMessageDao;
import ru.h562.smallsite.model.EmailMessage;
import ru.h562.smallsite.model.MessageData;
import ru.h562.smallsite.model.MessageType;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

@Repository
public class EmailMessageDaoImpl implements EmailMessageDao {
    private final JdbcTemplate jdbcTemplate;

    private static String TABLE = "email_message";
    private static String ALL_FIELDS = "id, date_time, message_type, address_from, address_to, address_copy, address_bcc, subject, text_body";
    private static String SELECT_ALL_FIELDS = "SELECT " + ALL_FIELDS + ", CURRENT_TIMESTAMP AS sent FROM " + TABLE;
    private static String SELECT_ALL = SELECT_ALL_FIELDS + ";";
    private static String SELECT_BY_ID = SELECT_ALL_FIELDS + ", CURRENT_TIMESTAMP AS sent WHERE id = ?;";
    private static String SELECT_BY_TYPE = SELECT_ALL_FIELDS + " WHERE message_type = ?;";
    private static String INSERT_NEW = "INSERT INTO " + TABLE + "(" + ALL_FIELDS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static String DELETE_BY_ID = "DELETE FROM " + TABLE + " WHERE id = ?;";

    private static String BACKUP_TABLE = "backup_" + TABLE;
    private static String BACKUP_DATA_TABLE = "backup_message_data";
    private static String BACKUP_ALL_FIELDS = ALL_FIELDS + ", sent";
    private static String BACKUP_SELECT_ALL_FIELDS = "SELECT " + BACKUP_ALL_FIELDS + " FROM " + BACKUP_TABLE;
    private static String BACKUP_DELETE_BY_ID = "DELETE FROM " + BACKUP_TABLE + " WHERE id = ?;";
    private static String BACKUP_DATA_DELETE_BY_ID = "DELETE FROM " + BACKUP_DATA_TABLE + " WHERE id = ?;";
    private static String BACKUP_DATA_SELECT_BY_ID = "SELECT name, value FROM " + BACKUP_DATA_TABLE + " WHERE id = ?;";

    private static String BACKUP_DATA_MESSAGE = "INSERT INTO " + BACKUP_DATA_TABLE + " ( id, name, value )\n" +
            "     SELECT id, name, value \n" +
            "       FROM message_data \n" +
            "      WHERE id = ?;\n";


    private static String BACKUP_MESSAGE = "INSERT INTO " + BACKUP_TABLE + " ( " + BACKUP_ALL_FIELDS + " )\n" +
            "     SELECT " + ALL_FIELDS + ", ? \n" +
            "       FROM " + TABLE + " \n" +
            "      WHERE id = ?;\n";

    private final RowMapper<EmailMessage> rowEmailMessageMapper = new RowEmailMessageMapperImpl();
    private final RowMapper<MessageData> rowMessageDataMapperImpl = new RowMessageDataMapperImpl();

    @Autowired
    public EmailMessageDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Collection<EmailMessage> findAll() {
        Collection<EmailMessage> list = jdbcTemplate.query(
                SELECT_ALL,
                rowEmailMessageMapper
        );

        return list;
    }

    @Override
    public Collection<EmailMessage> findByType(MessageType messageType) {
        Collection<EmailMessage> list = jdbcTemplate.query(
                SELECT_BY_TYPE,
                rowEmailMessageMapper,
                messageType.toString()
        );

        return list;
    }

    @Override
    public EmailMessage findById(String id) {
        Collection<EmailMessage> list = jdbcTemplate.query(
                SELECT_BY_ID,
                rowEmailMessageMapper,
                id
        );

        if (list != null && !list.isEmpty()) {
            EmailMessage msg = list.stream().findFirst().get();
            return msg;
        } else {
            return null;
        }
    }

    @Override
    public EmailMessage save(EmailMessage msg) {
        jdbcTemplate.update(
                INSERT_NEW,
                msg.getId(),
                Timestamp.valueOf(msg.getDateTime()),
                msg.getType().toString(),
                msg.getFrom(),
                msg.getTo(),
                msg.getCc(),
                msg.getBcc(),
                msg.getSubject(),
                msg.getText()
        );

        jdbcTemplate.update(
                BACKUP_DATA_MESSAGE,
                msg.getId()
        );

        return msg;
    }

    @Override
    public boolean delete(EmailMessage msg) {
        int count = jdbcTemplate.update(
                DELETE_BY_ID,
                msg.getId()
        );

        return count != 0;
    }

    @Override
    public Collection<EmailMessage> findAllFromBackup() {
        Collection<EmailMessage> list = jdbcTemplate.query(
                BACKUP_SELECT_ALL_FIELDS,
                rowEmailMessageMapper
        );

        return list;
    }

    @Override
    public Collection<MessageData> findBackupMessageDataById(String id) {
        Collection<MessageData> list = jdbcTemplate.query(
                BACKUP_DATA_SELECT_BY_ID,
                rowMessageDataMapperImpl,
                id
        );

        return list;
    }

    @Override
    public boolean backup(EmailMessage msg) {
        int count = jdbcTemplate.update(
                BACKUP_MESSAGE,
                Timestamp.valueOf(msg.getSent()),
                msg.getId()
        );

        return count != 0;
    }

    @Override
    public boolean deleteBackupMessage(EmailMessage backupMsg) {
        int count;

        count = jdbcTemplate.update(
                BACKUP_DELETE_BY_ID,
                backupMsg.getId()
        );

        jdbcTemplate.update(
                BACKUP_DATA_DELETE_BY_ID,
                backupMsg.getId()
        );

        return count != 0;
    }

    private static class RowEmailMessageMapperImpl implements RowMapper {

        public RowEmailMessageMapperImpl() {
        }

        @Override
        public EmailMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmailMessage msg = new EmailMessage();

            msg.setId(rs.getString("id"));
            msg.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
            msg.setType(MessageType.valueOf(rs.getString("message_type")));
            msg.setFrom(rs.getString("address_from"));
            msg.setTo(rs.getString("address_to"));
            msg.setCc(rs.getString("address_copy"));
            msg.setBcc(rs.getString("address_bcc"));
            msg.setSubject(rs.getString("subject"));
            msg.setText(rs.getString("text_body"));
            msg.setSent(rs.getTimestamp("sent").toLocalDateTime());

            return msg;
        }
    }

    private static class RowMessageDataMapperImpl implements RowMapper {

        public RowMessageDataMapperImpl() {
        }

        @Override
        public MessageData mapRow(ResultSet rs, int rowNum) throws SQLException {
            MessageData msgData = new MessageData(
                    rs.getString("value"),
                    rs.getString("name")
            );

            return msgData;
        }
    }

}
