package ru.h562.smallsite.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.h562.smallsite.dao.DBServiceDao;

import javax.sql.DataSource;

@Repository
public class DBServiceDaoImpl implements DBServiceDao {
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    private static String SHUTDOWN = "SHUTDOWN;";
    private static String SHUTDOWN_COMPACT = "SHUTDOWN COMPACT;";

    @Autowired
    public DBServiceDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void ShutdownDB() {
        jdbcTemplate.execute(SHUTDOWN);
    }

    @Override
    public void ShutdownCompactDB() {
        jdbcTemplate.execute(SHUTDOWN_COMPACT);
    }
}
