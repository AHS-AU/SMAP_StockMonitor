package com.example.admin.stockmonitor.Classes;

public enum Sector {
    /**
     * This class was inspired by Vojtech Rychnovsky after he reviewed my code.
     */
    TECHNOLOGY(0,"Technology"),
    HEALTHCARE(1,"Healthcare"),
    BASIC_MATERIALS(2,"Basic Materials");

    private final int id;
    private final String value;

    Sector(final int id, final String value){
        this.id = id;
        this.value = value;
    }
    public int getId(){
        return id;
    }
    public String getValue(){
        return value;
    }


}
