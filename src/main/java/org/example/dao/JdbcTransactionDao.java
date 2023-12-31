package org.example.dao;

import org.example.exception.DaoException;
import org.example.model.Transaction;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransactionDao implements TransactionDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransactionDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Transaction getTransactionById(int transactionId) {
        Transaction transaction = null;
        final String sql = "SELECT transaction_id, time, account_number, previous_balance, amount\n" +
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
    public Transaction createTransaction(Transaction transaction) {
        Transaction newTransaction;
        final String sql = "INSERT INTO transaction (time, previous_balance, account_number, amount) " +
                "VALUES (?, ?, ?, ?) RETURNING transaction_id;";
        try {
            int newTransactionId = jdbcTemplate.queryForObject(sql, int.class, transaction.getTime(),
                    transaction.getPreviousBalance(),
                    transaction.getAccountNumber(), transaction.getAmount());
            newTransaction = getTransactionById(newTransactionId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation");
        }
        return newTransaction;
    }

    @Override
    public List<Transaction> getTransactionsByCustomerId(int customerId) {
        List<Transaction> transactions = new ArrayList<>();
        final String sql = "SELECT transaction_id, time, a.account_number, previous_balance, amount \n" +
                "FROM transaction t\n" +
                "INNER JOIN account a ON t.account_number = a.account_number\n" +
                "INNER JOIN customer c ON c.customer_id = a.customer_id\n" +
                "WHERE c.customer_id = ?\n" +
                "ORDER BY transaction_id DESC;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, customerId);
            while (results.next()) {
                transactions.add(mapRowToTransaction(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        }
        return transactions;
    }

    @Override
    public List<Transaction> getTransactionsByAccountNumber(int accountNumber) {
        List<Transaction> transactions = new ArrayList<>();
        final String sql = "SELECT transaction_id, time, account_number, previous_balance, amount\n" +
                "FROM transaction\n" +
                "WHERE account_number = ?" +
                "ORDER BY transaction_id DESC;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountNumber);
            while (results.next()) {
                transactions.add(mapRowToTransaction(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        }
        return transactions;
    }

    @Override
    public List<Transaction> generateReport(Timestamp begin, Timestamp end, int accountNumber, BigDecimal amount) {
        List<Transaction> transactions = new ArrayList<>();
        final String sqlByTime = "SELECT transaction_id, time, account_number, previous_balance, amount " +
                "FROM transaction WHERE time > ? AND time < ? ORDER BY time ASC";
        final String sqlSortAmount = "SELECT transaction_id, time, account_number, previous_balance, amount \n" +
                "FROM transaction WHERE time > ? AND time < ? AND amount = ? ORDER BY time ASC;";
        final String sqlSortAccount = "SELECT transaction_id, time, account_number, previous_balance, amount \n" +
                "FROM transaction WHERE time > ? AND time < ? AND account_number = ? ORDER BY time ASC;";
        final String sqlSortAmountAndAccount = "SELECT transaction_id, time, account_number, previous_balance, amount \n" +
                "FROM transaction WHERE time > ? AND time < ? AND amount = ? AND account_number = ? ORDER BY time ASC;";

        try {
            SqlRowSet results = null;
            if ((accountNumber == 0) && (amount.compareTo(BigDecimal.ZERO)) == 0) {
                results = jdbcTemplate.queryForRowSet(sqlByTime, begin, end);
            } else if ((accountNumber == 0) && (amount.compareTo(BigDecimal.ZERO) != 0)) {
                results = jdbcTemplate.queryForRowSet(sqlSortAmount, begin, end, amount);
            } else if ((accountNumber != 0) && (amount.compareTo(BigDecimal.ZERO) == 0)) {
                results = jdbcTemplate.queryForRowSet(sqlSortAccount, begin,end, accountNumber);
            } else if ((accountNumber != 0) && (amount.compareTo(BigDecimal.ZERO) != 0)) {
                results = jdbcTemplate.queryForRowSet(sqlSortAmountAndAccount, begin, end, amount, accountNumber);
            }
            while (results.next()) {
                transactions.add(mapRowToTransaction(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        }
        return transactions;
    }

    public Transaction mapRowToTransaction(SqlRowSet rowSet) {
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(rowSet.getInt("account_number"));
        transaction.setAmount(rowSet.getBigDecimal("amount"));
        Timestamp timestamp = rowSet.getTimestamp("time");
        transaction.setTime(timestamp);
        transaction.setTransactionId(rowSet.getInt("transaction_id"));
        transaction.setPreviousBalance(rowSet.getBigDecimal("previous_balance"));
        return transaction;
    }


}
