package org.example.dao;

import org.example.exception.DaoException;
import org.example.model.Account;
import org.example.model.Customer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcCustomerDao implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcCustomerDao(DataSource datasource) {
        this.jdbcTemplate = new JdbcTemplate(datasource);
    }

    @Override
    public Customer getCustomerById(int customerId) {
        Customer customer = null;
        final String sql = "SELECT customer_id, name, password, last_login\n" +
                "FROM customer\n" +
                "WHERE customer_id = ?;";
        final String sql2 = "SELECT SUM(amount) AS total_balance " +
                "FROM transaction WHERE account_number IN (SELECT account_number FROM account WHERE customer_id = ?);";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, customerId);
            BigDecimal balance = jdbcTemplate.queryForObject(sql2, BigDecimal.class, customerId);
            if (results.next()) {
                customer = mapRowToCustomer(results);
                customer.setTotalBalance(balance);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        }
        return customer;
    }

    public Customer getCustomerByNameAndPassword(String name, String password) {
        Customer customer = null;
        final String sql = "SELECT customer_id, name, password, last_login" +
                " FROM customer WHERE name = ? AND password = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, name, password);
            if (results.next()) {
                customer = mapRowToCustomer(results);
                final String sql2 = "SELECT SUM(amount) AS total_balance " +
                        "FROM transaction WHERE account_number IN (SELECT account_number FROM account WHERE customer_id = ?);";
                BigDecimal balance = jdbcTemplate.queryForObject(sql2, BigDecimal.class, customer.getCustomerId());
                customer.setTotalBalance(balance);
                if (customer.getTotalBalance() == null) {
                    customer.setTotalBalance(BigDecimal.ZERO);
                }
                customer.setTier();
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        }
        return customer;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        Customer newCustomer;
        final String sql = "INSERT INTO customer(name, password) VALUES (?,?) RETURNING customer_id;";
        try {
            int newCustomerId = jdbcTemplate.queryForObject(sql, int.class, customer.getName(), customer.getPassword());
            newCustomer = getCustomerById(newCustomerId);
            newCustomer.setTotalBalance(BigDecimal.ZERO);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newCustomer;
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        Customer updatedCustomer;
        final String sql = "UPDATE customer\n" +
                "SET name = ?, password = ?, last_login = ?\n" +
                "WHERE customer_id = ?;";
        try {
            int numberOfRows = jdbcTemplate.update(sql, customer.getName(), customer.getPassword(), customer.getLastLogin(), customer.getCustomerId());
            if (numberOfRows == 0) {
                throw new DaoException("Zeros rows affected, expected at least one");
            } else {
                updatedCustomer = getCustomerById(customer.getCustomerId());
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedCustomer;
    }

    @Override
    public int deleteCustomerById(int customerId) {
        int numberOfRows;
        final String sql1 = "DELETE FROM transaction WHERE account_number IN " +
                "(SELECT account_number FROM account WHERE customer_id = ?)";
        final String sql2 = "DELETE FROM account WHERE customer_id = ?";
        final String sql3 = "DELETE FROM customer WHERE customer_id = ?;";
        try {
            jdbcTemplate.update(sql1, customerId);
            jdbcTemplate.update(sql2, customerId);
            numberOfRows = jdbcTemplate.update(sql3, customerId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return numberOfRows;
    }

    @Override
    public List<Account> getAccountsByCustomerId(int customerId) {
        List<Account> accounts = new ArrayList<>();
        final String sql1 = "SELECT account_number, customer_id, type FROM account WHERE customer_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql1, customerId);
            while (results.next()) {
                final String sql2 = "SELECT SUM(amount) AS total_balance FROM transaction WHERE account_number = ?;";
                BigDecimal accountBalance = jdbcTemplate.queryForObject(sql2, BigDecimal.class, results.getInt("account_number"));
                Account account = new Account();
                account.setAccountNumber(results.getInt("account_number"));
                account.setCustomerId(results.getInt("customer_id"));
                account.setAccountType(results.getString("type"));
                if (accountBalance == null) {
                    account.setAccountBalance(BigDecimal.ZERO);
                } else {
                    account.setAccountBalance(accountBalance);
                }
                accounts.add(account);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return accounts;
    }

    private Customer mapRowToCustomer(SqlRowSet rowSet) {
        Customer customer = new Customer();
        customer.setLastLogin(rowSet.getTimestamp("last_login").toLocalDateTime());
        customer.setCustomerId(rowSet.getInt("customer_id"));
        customer.setName(rowSet.getString("name"));
        customer.setPassword(rowSet.getString("password"));
        return customer;
    }
}
