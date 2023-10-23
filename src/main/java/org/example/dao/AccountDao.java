package org.example.dao;

import org.example.exception.DaoException;
import org.example.model.Account;

public interface AccountDao {
     /**
     *Get customer by name and password. If no customer found, return null.
     * @param accountId of account to get
     * @return Account
     * @throws DaoException if failure to connect with datastore
     */
    Account getAccountById(int accountId);
    /**
     *Add a new account to the datastore
     * @param account object to add
     * @return an account object
     * @throws DaoException if an error occurs
     */
    Account createAccount(Account account);
    /**
     *Update an existing account in the datastore.
     * @param account object to update
     * @return a fully populated updated account object
     * @throws DaoException if an error occurs, or method updates zero records.
     */
    Account updateAccount(Account account);
    /**
     *Removes an account from the datastore
     * @param accountId of the account to remove
     * @return number of accounts removed
     * @throws DaoException if an error occurs
     */
    int deleteAccountById(int accountId);

}
