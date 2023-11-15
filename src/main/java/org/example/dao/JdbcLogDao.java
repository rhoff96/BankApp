package org.example.dao;

import org.example.exception.DaoException;
import org.example.model.MonthlyLogEntry;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class JdbcLogDao implements LogDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcLogDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

    }

    @Override
    public MonthlyLogEntry getLastLoggedTime(String action) {
        MonthlyLogEntry lastEntry = null;
        final String sql = "SELECT time, action\n" +
                "FROM log\n" +
                "WHERE action = ?" +
                "ORDER BY time DESC\n" +
                "LIMIT 1;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, action);
            if (results.next()) {
                lastEntry = new MonthlyLogEntry();
                lastEntry.setTime(results.getTimestamp("time"));
                lastEntry.setType(results.getString("action"));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Cannot connect to server or database");
        }
        return lastEntry;
    }

    @Override
    public int createLogEntry(String type) {
        int newId;
        final String sql = "INSERT INTO log(time, action)\n" +
                "VALUES (?,?)\n" +
                "RETURNING entry_id;";
        try {
            newId = jdbcTemplate.queryForObject(sql, int.class, Timestamp.valueOf(LocalDateTime.now()), type);
            if (newId < 0) {
                throw new DaoException("Insert failed. Expected to create one new entry");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation");
        }
        return newId;
    }
}
