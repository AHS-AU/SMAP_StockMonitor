package com.example.admin.stockmonitor.Room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Book Model Class used for Room DB
 */
@Entity(tableName = "stock_table")
public class Book {
    // Unique ID in each entry
    @PrimaryKey(autoGenerate = true)
    private int uid;

    // Attributes
    private String companyName;
    private String symbol;
    private String primaryExchange;
    private String latestPrice; // Latest Value in Assignment
    private String latestUpdate; // Latest Timestamp in Assignment

    // Constructor
    public Book(String companyName, String symbol, String primaryExchange, String latestPrice, String latestUpdate) {
        this.companyName = companyName;
        this.symbol = symbol;
        this.primaryExchange = primaryExchange;
        this.latestPrice = latestPrice;
        this.latestUpdate = latestUpdate;
    }

    // Setters
    public void setUid(int uid) {
        this.uid = uid;
    }

    // Getters
    public int getUid() {
        return uid;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getPrimaryExchange() {
        return primaryExchange;
    }

    public String getLatestPrice() {
        return latestPrice;
    }

    public String getLatestUpdate() {
        return latestUpdate;
    }
}
