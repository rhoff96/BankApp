package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Customer {
    private int customerId;
    private String name;
    private String password;
    private Tier tier;
    private BigDecimal totalBalance;
    private LocalDateTime lastLogin;

    public void setTier(Tier tier) {
        this.tier = tier;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public Customer() {
    }

    public Customer(int customerId, String name, String password) {
        this.customerId = customerId;
        this.name = name;
        this.password = password;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return this.customerId;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        String[] firstAndLast = this.name.split(" ");
        return firstAndLast[0];
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public enum Tier {
        Bronze,
        Silver,
        Gold,
        Platinum
    }

    public void setTier() {
        if (this.totalBalance == null) {
            this.tier = Customer.Tier.Bronze;
            return;
        }
        if (this.totalBalance.compareTo(new BigDecimal("5000")) < 0) {
            this.tier = Customer.Tier.Bronze;
        } else if (this.totalBalance.compareTo(new BigDecimal("10000")) < 0) {
            this.tier = Customer.Tier.Silver;
        } else if (this.totalBalance.compareTo(new BigDecimal("25000")) < 0) {
            this.tier = Customer.Tier.Gold;
        } else {
            this.tier = Customer.Tier.Platinum;
        }
    }

    public Customer.Tier getTier() {
        return tier;
    }

    @Override
    public boolean equals(Object obj) {
        Customer other = (Customer) obj;
        if (!this.getClass().equals(other.getClass())) {
            return false;
        }
        if (!this.getName().equals(other.getName())) {
            return false;
        }
        if (!(this.getTotalBalance().equals(other.getTotalBalance()))) {
            return false;
        }
        return this.getPassword().equals(other.getPassword());
    }

}
