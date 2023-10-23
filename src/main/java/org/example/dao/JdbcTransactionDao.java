package org.example.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.example.exception.DaoException;
import org.example.model.Transaction;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.List;

public class JdbcTransactionDao implements TransactionDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransactionDao() {
        final BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:postgresql://localhost:5432/Bank");
        ds.setUsername("postgres");
        ds.setPassword("postgres1");
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public Transaction getTransactionById(int transactionId) {
        Transaction transaction = null;
        final String sql = "SELECT transaction_id, time, customer_id, account_number, previous balance, amount\n" +
                "FROM transaction\n" +
                "WHERE transaction_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transactionId);
            if (results.next()) {
                transaction = mapRowToTransaction(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        }
        return transaction;
    }

    @Override
    public List<Transaction> getTransactionsByCustomerId(int customerId) {
        List<Transaction> transactions = null;
        final String sql = "SELECT transaction_id, time, customer_id, account_number, previous_balance, amount\n" +
                "FROM transaction\n" +
                "WHERE customer_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, customerId);
            if (results.next()) {
                transactions.add(mapRowToTransaction(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        }
        return transactions;
    }

    @Override
    public List<Transaction> getTransactionsByAccountNumber(int accountNumber) {
        List<Transaction> transactions = null;
        final String sql = "SELECT transaction_id, time, customer_id, account_number, previous_balance, amount\n" +
                "FROM transaction\n" +
                "WHERE account_number = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountNumber);
            transactions.add(mapRowToTransaction(results));
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        }
        return transactions;
    }

    public Transaction mapRowToTransaction(SqlRowSet rowSet) {
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(rowSet.getInt("account_number"));
        transaction.setAmount(rowSet.getBigDecimal("amount"));
        transaction.setCustomerId(rowSet.getInt("customer_id"));
        transaction.setTime(rowSet.getDate("time").toLocalDate());
        transaction.setTransactionId(rowSet.getInt("transaction_id"));
        transaction.setPreviousBalance(rowSet.getBigDecimal("previous_balance"));
        return transaction;
    }
}
