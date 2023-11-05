package org.example;

import org.example.dao.CustomerDao;
import org.example.exception.DaoException;
import org.example.model.Customer;
import org.springframework.jdbc.CannotGetJdbcConnectionException;

public class Authentication {
    private final String name;
    private final String password;
    private final CustomerDao cd;
    private final boolean isNew;
    private Customer currentCustomer;

    public Authentication(String name, String password, CustomerDao cd, boolean isNew) {
        this.name = name;
        this.password = password;
        this.cd = cd;
        this.isNew = isNew;
    }

    public Customer authenticate() {
        if (isNew) {
            currentCustomer = createCustomer();
        } else {
            currentCustomer = lookUpCustomer(name, password);
        }
        return currentCustomer;
    }

    public Customer lookUpCustomer(String name, String password) {
        try {
            currentCustomer = cd.getCustomerByNameAndPassword(name, password);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Cannot connect to server or database", e);
        }
        return currentCustomer;
    }

    public Customer createCustomer() {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setPassword(password);
        return cd.createCustomer(customer);
    }
}
