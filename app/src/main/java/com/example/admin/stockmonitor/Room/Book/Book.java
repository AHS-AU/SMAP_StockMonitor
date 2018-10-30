package com.example.admin.stockmonitor.Room.Book;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.math.BigDecimal;
import java.util.Locale;

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
    private String sector;
    private String purchasePrice;

    // Constructor
    public Book(String companyName, String symbol, String primaryExchange, String latestPrice,
                String latestUpdate, String change, String sector, String purchasePrice) {
        this.companyName = companyName;
        this.symbol = symbol;
        this.primaryExchange = primaryExchange;
        this.latestPrice = latestPrice;
        this.latestUpdate = latestUpdate;
        this.change = change;
        this.sector = sector;
        this.purchasePrice = purchasePrice;
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

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getSector() {
        return sector;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public String getPriceDifference(){
        double mLatestPrice = Double.parseDouble(latestPrice);
        double mPurchasePrice = Double.parseDouble(purchasePrice);
        double mPriceDifference = (mLatestPrice - mPurchasePrice);
        return (String.format(Locale.getDefault(),"%.3f",mPriceDifference));
    }
}
