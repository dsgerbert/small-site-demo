package ru.h562.smallsite.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.h562.smallsite.dao.MessageDao;
import ru.h562.smallsite.dao.MessageDataDao;
import ru.h562.smallsite.model.Message;
import ru.h562.smallsite.model.MessageType;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

@Repository
public class MessageDaoImpl implements MessageDao {
    private final JdbcTemplate jdbcTemplate;

    private static String TABLE = "message";
    private static String ALL_FIELDS = "id, date_time, message_type";
    private static String SELECT_ALL_FIELDS = "SELECT " + ALL_FIELDS + " FROM " + TABLE;
    private static String SELECT_ALL = SELECT_ALL_FIELDS + ";";
    private static String SELECT_BY_ID = SELECT_ALL_FIELDS + " WHERE id = ?;";
    private static String SELECT_BY_TYPE = SELECT_ALL_FIELDS + " WHERE message_type = ?;";
    private static String INSERT_NEW = "INSERT INTO " + TABLE + "(" + ALL_FIELDS + ") VALUES (?, ?, ?);";
    private static String DELETE_BY_ID = "DELETE FROM " + TABLE + " WHERE id = ?;";

    private final RowMapper<Message> rowMessageMapper = new RowMessageMapperImpl();

    @Autowired
    private MessageDataDao messageDataDao;

    @Autowired
    public MessageDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Collection<Message> findAll() {
        Collection<Message> list = jdbcTemplate.query(
                SELECT_ALL,
                rowMessageMapper
        );

        list.stream().forEach(messageDataDao::fillMessageData);
        return list;
    }

    @Override
    public Collection<Message> findByType(MessageType messageType) {
        Collection<Message> list = jdbcTemplate.query(
                SELECT_BY_TYPE,
                rowMessageMapper,
                messageType.toString()
        );

        list.stream().forEach(messageDataDao::fillMessageData);
        return list;
    }

    @Override
    public Message findById(String id) {
        Collection<Message> list = jdbcTemplate.query(
                SELECT_BY_ID,
                rowMessageMapper,
                id
        );

        if (list != null && !list.isEmpty()) {
            Message message = list.stream().findFirst().get();
            messageDataDao.fillMessageData(message);
            return message;
        } else {
            return null;
        }
    }

    @Override
    public Message save(Message message) {
        jdbcTemplate.update(
                INSERT_NEW,
                message.getId(),
                Timestamp.valueOf(message.getDateTime()),
                message.getType().toString()
        );

        messageDataDao.delete(message);
        messageDataDao.save(message);

        return message;
    }

    @Override
    public boolean delete(Message message) {
        int count = jdbcTemplate.update(
                DELETE_BY_ID,
                message.getId()
        );

        return count != 0;
    }

    private static class RowMessageMapperImpl implements RowMapper {

        public RowMessageMapperImpl() {
        }

        @Override
        public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
            Message message = new Message();

            message.setId(rs.getString("id"));
            message.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
            message.setType(MessageType.valueOf(rs.getString("message_type")));

            return message;
        }
    }
}
