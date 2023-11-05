package org.example;

import org.example.dao.CustomerDao;
import org.example.exception.DaoException;
import org.example.model.Customer;
import org.springframework.dao.DataIntegrityViolationException;

public class Authentication {
    private String name;
    private String password;
    private CustomerDao cd;
    private boolean isNew;
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
            currentCustomer = lookUpCustomer();
            if (currentCustomer == null) {


            }
        }
        return currentCustomer;
    }

    public Customer lookUpCustomer() {
        try {
            currentCustomer = cd.getCustomerByNameAndPassword(name, password);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
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
