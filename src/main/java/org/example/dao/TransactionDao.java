package org.example.dao;
import org.example.exception.DaoException;
import org.example.model.Transaction;

import java.util.List;

public interface TransactionDao {

    /**
     * Gets a transaction
     * @param transactionId of transaction
     * @return transaction object
     * @throws DaoException if an error occurs
     */
    Transaction getTransactionById(int transactionId);
    /**
     *Gets all transactions for a customer
     * @param customerId
     * @return a list of transaction objects, ordered by date, newest to oldest.
     * @throws DaoException if an error occurs
     */
    List<Transaction> getTransactionsByCustomerId(int customerId);
    /**
     * Gets all transactions for an account
     * @param accountNumber
     * @return a list of transaction objects, ordered by date, newest to oldest.
     * @throws DaoException if an error occurs
     */
    List<Transaction> getTransactionsByAccountNumber(int accountNumber);
}
