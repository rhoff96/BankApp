package org.example.model;

import java.sql.Timestamp;

public class MonthlyLogEntry {
    private Timestamp time;

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public MonthlyLogEntry(){}

    public MonthlyLogEntry(Timestamp time, String type){
        this.time = time;
        this.type = type;
    }
    @Override
    public boolean equals(Object obj){
        MonthlyLogEntry other = (MonthlyLogEntry) obj;
        if (!this.getClass().equals(other.getClass())) {
            return false;
        }
        if (!this.getTime().equals(other.getTime())) {
            return false;
        }
        return this.getType().equals(other.getType());
    }
}
