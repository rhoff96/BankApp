package org.example.workflow;

import org.example.dao.AccountDao;
import org.example.model.Account;

public class AccountHandler {
    private int customerId;
    private String accountType;
    private final AccountDao ad;

    public AccountHandler(int customerId, String accountType, AccountDao ad) {
        this.customerId = customerId;
        this.accountType = accountType;
        this.ad = ad;
    }

    public Account createAccount(){
        Account account = new Account();
        account.setAccountType(accountType);
        account.setCustomerId(customerId);
        return ad.createAccount(account);
    }
}
