package org.example.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.example.exception.DaoException;
import org.example.model.Account;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;

public class JdbcAccountDao implements AccountDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao() {
        final BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:postgresql://localhost:5432/Bank");
        ds.setUsername("postgres");
        ds.setPassword("postgres1");
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public Account getAccountById(int accountId) {
        Account account = null;
        final String sql = "SELECT account_number, customer_id, type" +
                "FROM account\n" +
                "WHERE account_number = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
            if (results.next()) {
                account.setAccountNumber(results.getInt("account_number"));
                account.setCustomerId(results.getInt("customer_id"));
                account.setAccountType(results.getString("type"));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        }
        return account;
    }
    public BigDecimal getAccountBalanceByAccountNumber(int accountNumber) {
        BigDecimal accountBalance = null;
        final String sql = "SELECT SUM(previous_balance + amount) AS account_balance \n" +
                "FROM transaction\n" +
                "WHERE account_number = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountNumber);
            accountBalance = results.getBigDecimal("account_balance");

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        }
        return accountBalance;
    }

    @Override
    public Account createAccount(Account account) {
        Account newAccount = null;
        final String sql = "INSERT INTO account(account_number, customer_id, type) VALUES (?,?,?) RETURNING customer_id;";
        try {
            int newAccountId = jdbcTemplate.queryForObject(sql, int.class, account.getAccountNumber(), account.getCustomerId(),account.getAccountType());
            newAccount = getAccountById(newAccountId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newAccount;
    }

    @Override
    public Account updateAccount(Account account) {
        Account updatedAccount = null;
        final String sql = "UPDATE account\n" +
                "SET customer_id = ?, type = ?\n" +
                "WHERE customer_id = ?;";
        try {
            int numberOfRows = jdbcTemplate.update(sql, account.getCustomerId(), account.getAccountType(),account.getCustomerId());
            if (numberOfRows == 0) {
                throw new DaoException("Zeros rows affected, expected at least one");
            } else {
                updatedAccount = getAccountById(account.getCustomerId());
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedAccount;
    }

    @Override
    public int deleteAccountById(int accountId) {
        int numberOfRows = 0;
        String sql = "DELETE FROM account WHERE account_number = ?;";
        try {
            numberOfRows = jdbcTemplate.update(sql, accountId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return numberOfRows;
    }
}
