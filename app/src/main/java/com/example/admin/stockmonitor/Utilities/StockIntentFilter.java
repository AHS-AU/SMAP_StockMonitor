package com.example.admin.stockmonitor.Utilities;

import android.content.IntentFilter;
import static com.example.admin.stockmonitor.Utilities.SharedConstants.*;

public class StockIntentFilter extends IntentFilter {
    private IntentFilter intentFilter;

    public StockIntentFilter() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(FILTER_DATA_AVAILABLE);
        intentFilter.addAction(FILTER_DATA_UPDATE);
    }

    public IntentFilter getIntentFilter() {
        return intentFilter;
    }
}
