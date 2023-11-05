package org.example.dao;

import org.example.exception.DaoException;
import org.example.model.Account;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcAccountDao implements AccountDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Account getAccountById(int accountNumber) {
        Account account = null;
        final String sql = "SELECT account_number, customer_id, type, withdrawal_count\n" +
                "FROM account\n" +
                "WHERE account_number = ?;";
        final String sql2 = "SELECT SUM(amount) AS account_balance " +
                "FROM transaction WHERE account_number = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountNumber);
            SqlRowSet balance = jdbcTemplate.queryForRowSet(sql2, accountNumber);
            if (results.next()) {
                account = new Account();
                account.setAccountNumber(results.getInt("account_number"));
                account.setCustomerId(results.getInt("customer_id"));
                account.setAccountType(results.getString("type"));
                account.setWithdrawalCount(results.getInt("withdrawal_count"));
                if (balance.next()){
                    account.setAccountBalance(balance.getBigDecimal("account_balance"));
                }
                if (account.getAccountBalance() == null){
                    account.setAccountBalance(BigDecimal.ZERO);
                }
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        }
        return account;
    }

    @Override
    public Account createAccount(Account account) {
        Account newAccount;
        final String sql = "INSERT INTO account(customer_id, type, withdrawal_count) VALUES (?,?,?) RETURNING account_number;";
        try {
            int newAccountId = jdbcTemplate.queryForObject(sql, int.class, account.getCustomerId(), account.getAccountType(), account.getWithdrawalCount());
            newAccount = getAccountById(newAccountId);
            newAccount.setAccountBalance(BigDecimal.ZERO);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newAccount;
    }

    @Override
    public Account updateAccount(Account account) {
        Account updatedAccount;
        final String sql = "UPDATE account\n" +
                "SET customer_id = ?, type = ?, withdrawal_count = ?\n" +
                "WHERE account_number = ?;";
        try {
            int numberOfRows = jdbcTemplate.update(sql, account.getCustomerId(), account.getAccountType(), account.getWithdrawalCount(), account.getAccountNumber());
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
        int numberOfRows;
        String sqlTransaction = "DELETE FROM transaction WHERE account_number = ?";
        String sqlAccount = "DELETE FROM account WHERE account_number = ?;";
        try {
            numberOfRows = jdbcTemplate.update(sqlTransaction, accountId);
            numberOfRows = jdbcTemplate.update(sqlAccount, accountId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return numberOfRows;
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        final String sql = "SELECT account_number, customer_id, type, withdrawal_count\n" +
                "FROM account;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                Account account = getAccountById(results.getInt("account_number"));
                accounts.add(account);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return accounts;
    }
}
