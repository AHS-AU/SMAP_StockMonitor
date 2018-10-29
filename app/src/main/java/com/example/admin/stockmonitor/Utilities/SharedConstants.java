package com.example.admin.stockmonitor.Utilities;

import com.example.admin.stockmonitor.Utilities.ViewModels.BookViewModel;

/**
 * Class that stores data.
 * Activies can refer to these datas without getting in conflict.
 */
public final class SharedConstants {
    // Intent Request Codes
    public static final int REQ_OVERVIEW_UPDATE = 101;
    public static final int REQ_DETAILS_UPDATE = 102;

    // Log Tags Identifiers for non-activities, i.e. could be used for Services
    public static final String StockServiceTag = "StockService";
    public static final String StockBroadcastReceiverTag = "StockBroadCastReceiver";

    // Notification & Service Channel & Notice Ids
    public static final String NOTIF_CHANNEL_ID_STOCKSERVICE = "STOCKSERVICE_CHANNEL_ID";
    public static final int NOTIF_ID_STOCKSERVICE = 201;

    // TODO: BookViewModel Used Throughout Everything Necessary
    public static BookViewModel bookViewModel;

    // Intent Extras
    public static final String EXTRA_STOCK = "EXTRA_STOCK";

    // Intent Filter
    public static final String FILTER_DATA_AVAILABLE = "FILTER_DATA_AVAILABLE";
    public static final String FILTER_DATA_UPDATE = "FILTER_DATA_UPDATE";

    // Save Instance Keys
    public static final String SAVE_STOCK = "SAVE_STOCK";

    // API Keys
    public static final String IEXTRADING_STOCK_API_KEY = "c49a5a1d9ce7452c824a7eed76552e65";

    // API CALLS
    public static final String IEXTRADING_STOCK_API_CALL =
            "https://api.iextrading.com/1.0/stock/" + "ATVI"  + "/book/" + IEXTRADING_STOCK_API_KEY;

    public String getApiUrl(String symbol){
        return ("https://api.iextrading.com/1.0/stock/" + symbol  + "/book/" + IEXTRADING_STOCK_API_KEY);
    }

}
