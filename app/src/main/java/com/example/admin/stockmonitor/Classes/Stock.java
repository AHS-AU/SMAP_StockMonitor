package com.example.admin.stockmonitor.Classes;

import java.io.Serializable;

public class Stock implements Serializable {
    // Attributes
    private String StockName;
    private double PurchasePrice;
    private int NumberOfStocks;
    private Sector Sector;

    // Constructor
    public Stock(String name, double price, int numOfStocks, Sector sector){
        this.StockName = name;
        this.PurchasePrice = price;
        this.NumberOfStocks = numOfStocks;
        this.Sector = sector;
    }

    // Methods
    public String getStockName(){
        return StockName;
    }
    public void setStockName(String name){ this.StockName = name; }

    public double getPurchasePrice(){
        return PurchasePrice;
    }
    public void setPurchasePrice(double price){ this.PurchasePrice = price; }

    public int getNumberOfStocks(){
        return NumberOfStocks;
    }
    public void setNumberOfStocks(int numOfStocks) { this.NumberOfStocks = numOfStocks; }

    public Sector getSector(){
        return Sector;
    }
    public void setSector(Sector sector){ this.Sector = sector; }

}
