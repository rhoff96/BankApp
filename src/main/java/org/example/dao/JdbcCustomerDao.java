package org.example.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.example.exception.DaoException;
import org.example.model.Account;
import org.example.model.Customer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcCustomerDao implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcCustomerDao() {
        final BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:postgresql://localhost:5432/Bank");
        ds.setUsername("postgres");
        ds.setPassword("postgres1");
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public Customer getCustomerById(int customerId) {
        Customer customer = null;
        final String sql = "SELECT customer_id, name, password\n" +
                "FROM customer\n" +
                "WHERE customer_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, customerId);
            if (results.next()) {
                customer = mapRowToCustomer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        }
        return customer;
    }
    public Customer getCustomerByNameAndPassword(String name, String password){
        Customer customer = null;
        final String sql = "SELECT customer_id, name, password " +
                "FROM customer WHERE name = ? AND password = ?";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, name, password);
            if (results.next()){
                customer = mapRowToCustomer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        }
        return customer;
    }
    public BigDecimal getTotalBalanceByCustomerId(int customerId) {
        BigDecimal totalBalance = null;
        final String sql = "SELECT SUM(previous_balance + amount) AS total_balance \n" +
                "FROM transaction\n" +
                "WHERE customer_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, customerId);
            if (results.next()) {
                totalBalance = results.getBigDecimal("total_balance");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        }
        return totalBalance;
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
    public Customer createCustomer(Customer customer) {
        Customer newCustomer = null;
        final String sql = "INSERT INTO customer(name, password) VALUES (?,?) RETURNING customer_id;";
        try {
            int newCustomerId = jdbcTemplate.queryForObject(sql, int.class, customer.getName(),customer.getPassword());
            newCustomer = getCustomerById(newCustomerId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database");
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newCustomer;
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        Customer updatedCustomer = null;
        final String sql = "UPDATE customer\n" +
                "SET name = ?, password = ?\n" +
                "WHERE customer_id = ?;";
        try {
            int numberOfRows = jdbcTemplate.update(sql, customer.getName(), customer.getPassword(), customer.getCustomerId());
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
        int numberOfRows = 0;
        String sql = "DELETE FROM customer WHERE customer_id = ?;";
        try {
            numberOfRows = jdbcTemplate.update(sql, customerId);
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
        String sql = "SELECT account_number, customer_id" +
                "FROM account" +
                "WHERE customer_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, customerId);
            while (results.next()) {
                Account account = new Account();
                account.setAccountNumber(results.getInt("account_number"));
                account.setCustomerId(results.getInt("customer_id"));
                accounts.add(account);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return accounts;
    }

    private Customer mapRowToCustomer(SqlRowSet rowSet) {
        Customer customer = new Customer();
        customer.setCustomerId(rowSet.getInt("customer_id"));
        customer.setName(rowSet.getString("name"));
        customer.setPassword(rowSet.getString("password"));
        return customer;
    }
}
