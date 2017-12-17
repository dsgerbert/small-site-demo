package ru.h562.smallsite.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.h562.smallsite.model.Message;
import ru.h562.smallsite.model.MessageData;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
public class MessageDataDaoImpl implements ru.h562.smallsite.dao.MessageDataDao {
    private static String TABLE = "message_data";
    private static String ALL_FIELDS = "id, name, value";
    private static String SELECT_ALL_FIELDS_BY_ID = "SELECT " + ALL_FIELDS + " FROM " + TABLE + " WHERE id = ?;";
    private static String DELETE_ALL_BY_ID = "DELETE FROM " + TABLE + " WHERE id = ?;";
    private static String INSERT_NEW = "INSERT INTO " + TABLE + " (" + ALL_FIELDS + ") VALUES(?, ?, ?);";

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<MessageData> rowMessageDataMapper = new RowMessageDataMapperImpl();

    @Autowired
    public MessageDataDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void fillMessageData(Message message) {
        Collection<MessageData> list = jdbcTemplate.query(
                SELECT_ALL_FIELDS_BY_ID,
                rowMessageDataMapper,
                message.getId()
        );

        message.setData(list);
    }

    @Override
    public Message save(Message message) {
        delete(message);

        for (MessageData messageData : message.getData()) {
            jdbcTemplate.update(
                    INSERT_NEW,
                    message.getId(),
                    messageData.getName(),
                    messageData.getValue()
            );
        }

        return message;
    }

    @Override
    public boolean delete(Message message) {
        int count = jdbcTemplate.update(
                DELETE_ALL_BY_ID,
                message.getId()
        );

        return count != 0;
    }

    private static class RowMessageDataMapperImpl implements RowMapper {

        public RowMessageDataMapperImpl() {
        }

        @Override
        public MessageData mapRow(ResultSet rs, int rowNum) throws SQLException {
            MessageData messageData = new MessageData(
                    rs.getString("name"),
                    rs.getString("value")
            );

            return messageData;
        }
    }
}
