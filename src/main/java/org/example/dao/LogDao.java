package org.example.dao;

import org.example.model.MonthlyLogEntry;

import java.sql.Timestamp;

public interface LogDao {

    /**
     *
     * @return timestamp of the last time the monthly tasks were executed
     */
    MonthlyLogEntry getLastLoggedTime(String action);

    /**
     * @param type of the entry, either "fee" or "interest"
     * @return timestamp of created entry
     */

    int createLogEntry(String type);
}
