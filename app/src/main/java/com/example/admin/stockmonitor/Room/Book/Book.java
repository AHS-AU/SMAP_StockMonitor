package com.example.admin.stockmonitor.Room.Book;

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
    private String change;

    // Constructor
    public Book(String companyName, String symbol, String primaryExchange, String latestPrice, String latestUpdate, String change) {
        this.companyName = companyName;
        this.symbol = symbol;
        this.primaryExchange = primaryExchange;
        this.latestPrice = latestPrice;
        this.latestUpdate = latestUpdate;
        this.change = change;
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

    public void setLatestPrice(String latestPrice) {
        this.latestPrice = latestPrice;
    }

    public String getLatestPrice() {
        return latestPrice;
    }

    public void setLatestUpdate(String latestUpdate) {
        this.latestUpdate = latestUpdate;
    }

    public String getLatestUpdate() {
        return latestUpdate;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getChange() {
        return change;
    }
}
