package com.example.admin.stockmonitor.Utilities.Broadcaster;

import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.admin.stockmonitor.OverviewActivity;
import com.example.admin.stockmonitor.Room.Book.Book;
import com.example.admin.stockmonitor.Utilities.ViewModels.BookViewModel;

import static com.example.admin.stockmonitor.Utilities.SharedConstants.*;
public final class StockBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if(FILTER_DATA_AVAILABLE.equals(action)){
            Log.d(StockBroadcastReceiverTag, FILTER_DATA_AVAILABLE);
            Book newBook = new Book("NewCompany", "NewSymbol", "NewPE", "NewLP", "NewLU" );
            bookViewModel.insert(newBook);

        }

    }
}
