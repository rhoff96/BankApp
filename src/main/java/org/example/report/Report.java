package org.example.report;

import org.example.CLI.UserInterface;
import org.example.dao.TransactionDao;
import org.example.model.Transaction;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class Report {

    private final UserInterface ui;
    private final TransactionDao td;

    public Report(UserInterface ui, TransactionDao td){
        this.ui = ui;
        this.td = td;
    }

    public List<Transaction> getReportParameters() {
        ui.put("Enter start time (yyyy-MM-dd HH:mm:ss): ");
        Timestamp start = ui.getTimestamp();
        ui.put("Enter end time (yyyy-MM-dd HH:mm:ss): ");
        Timestamp end = ui.getTimestamp();
        ui.put("Enter account number (or 0 for all accounts): ");
        int accountNumber = ui.getInt();
        ui.put("Enter transaction amount (or 0 for all amounts): ");
        BigDecimal amount = ui.getBigDec();
        return td.generateReport(start,end,accountNumber,amount);
    }
}
