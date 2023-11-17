package org.example.monthly;

import org.example.dao.*;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduleTask {

    private final AccountDao accountDao;
    private final CustomerDao customerDao;
    private final TransactionDao transactionDao;
    private final LogDao logDao;

    public ScheduleTask(AccountDao accountDao, CustomerDao customerDao, TransactionDao transactionDao, LogDao logDao) {
        this.accountDao = accountDao;
        this.customerDao = customerDao;
        this.transactionDao = transactionDao;
        this.logDao = logDao;
    }

    public void runMonthlyTasks() {
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                MaintenanceTaskFactory factory = new MaintenanceTaskFactory(accountDao, customerDao, transactionDao, logDao);
                factory.chargeMaintenanceFee();
                factory.accrueInterest();
            }
        };
        Timer timer = new Timer("Timer");

        long delay = 0L;
        long oncePerDay = 1000L * 60L * 60L * 24L;
        timer.scheduleAtFixedRate(repeatedTask, delay, oncePerDay);
    }

}
