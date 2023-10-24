package org.example.dao;

import org.example.exception.DaoException;
import org.example.model.Account;
import org.example.model.Customer;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerDao {
    /**
     * Get customer by name and password. If no customer found, return null.
     *
     * @param customerId of customer to get
     * @return Customer
     * @throws DaoException if failure to connect with datastore
     */
    Customer getCustomerById(int customerId);

    /**
     * Add a new customer to the datastore
     *
     * @param customer object to add
     * @return a customer object
     * @throws DaoException if an error occurs
     */
    Customer createCustomer(Customer customer);

    /**
     * Update an existing customer in the datastore, allowing them to change username or password.
     *
     * @param customer object to update
     * @return a fully populated updated customer object
     * @throws DaoException if an error occurs, or method updates zero records.
     */
    Customer updateCustomer(Customer customer);

    /**
     * Removes a customer from the datastore
     *
     * @param customerId of the customer to remove
     * @return number of customers removed
     * @throws DaoException if an error occurs
     */
    int deleteCustomerById(int customerId);

    /**
     * Get all accounts for a customer
     *
     * @param customerId
     * @return a list of account objects
     * @throws DaoException if an error occurs
     */
    List<Account> getAccountsByCustomerId(int customerId);

    /**
     * Get total balance for a customer
     *
     * @param customerId of customer
     * @return total balance
     * @throws DaoException if an error occurs
     */
    BigDecimal getTotalBalanceByCustomerId(int customerId);

    /**
     * @param name and password of customer
     * @return customer object
     * @throws DaoException if an error occurs
     */
    public Customer getCustomerByNameAndPassword(String name, String password);

}




