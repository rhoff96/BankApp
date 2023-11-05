package org.example.dao;
import org.example.exception.DaoException;
import org.example.model.Transaction;

import java.math.BigDecimal;
import java.sql.Timestamp;
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
     * Creates a new transaction
     * @param transaction object
     * @return full transaction object
     * @throws DaoException if an error occurs
     */
    Transaction createTransaction(Transaction transaction);
    /**
     *Gets all transactions for a customer
     * @param customerId of customer
     * @return a list of transaction objects, ordered by date, newest to oldest.
     * @throws DaoException if an error occurs
     */
    List<Transaction> getTransactionsByCustomerId(int customerId);
    /**
     * Gets all transactions for an account
     * @param accountNumber of account
     * @return a list of transaction objects, ordered by date, newest to oldest.
     * @throws DaoException if an error occurs
     */
    List<Transaction> getTransactionsByAccountNumber(int accountNumber);

    /**
     * Generates a list of all transactions by given criteria
     * @param from - beginning time
     * @param to - end time
     * @return a list of transaction objects
     */
    List<Transaction> generateReport(Timestamp from, Timestamp to, int accountNumber, BigDecimal amount);
}
