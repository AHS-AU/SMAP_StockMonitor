package com.example.admin.stockmonitor.Utilities;

/**
 * Class that stores data.
 * Activies can refer to these datas without getting in conflict.
 */
public final class SharedConstants {
    // Intent Request Codes
    public static final int REQ_OVERVIEW_UPDATE = 101;
    public static final int REQ_DETAILS_UPDATE = 102;

    // Notification & Service Channel & Notice Ids
    public static final String NOTIF_CHANNEL_ID_STOCKSERVICE = "STOCKSERVICE_CHANNEL_ID";
    public static final int NOTIF_ID_STOCKSERVICE = 201;
    public static final int NOTIF_ID_STOCKSERVICE28 = 202;

    // Intent Extras
    public static final String EXTRA_STOCK = "EXTRA_STOCK";
    public static final String EXTRA_SYMBOL = "EXTRA_SYMBOL";
    public static final String EXTRA_PURCHASE_PRICE = "EXTRA_PURCHASE_PRICE";
    public static final String EXTRA_NUMBER_OF_STOCKS = "EXTRA_NUMBER_OF_STOCKS";

    // Intent Filter
    public static final String FILTER_DATA_SINGLE_UPDATE = "FILTER_DATA_SINGLE_UPDATE";
    public static final String FILTER_DATA_UPDATE = "FILTER_DATA_UPDATE";
    public static final String FILTER_DB_UI_CHANGES = "FILTER_DB_UI_CHANGES";

    // Save Instance Keys
    public static final String SAVE_STOCK = "SAVE_STOCK";

    // API Keys
    public static final String IEXTRADING_STOCK_API_KEY = "c49a5a1d9ce7452c824a7eed76552e65";

    // API CALLS
    public String getApiUrl(String symbol){
        return ("https://api.iextrading.com/1.0/stock/" + symbol  + "/book/" + IEXTRADING_STOCK_API_KEY);
    }

}
