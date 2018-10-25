package com.example.admin.stockmonitor.Utilities;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.support.v4.app.NotificationCompat;


import com.example.admin.stockmonitor.Classes.Stock;
import com.example.admin.stockmonitor.R;

import java.util.List;

import static com.example.admin.stockmonitor.Utilities.SharedConstants.*;

public class StockService extends Service {
    private List<Stock> mStockList;

    public StockService() {
    }

    private List<Stock> getStocks(){
        return mStockList;
    }


    // TODO: Complete the method
    private Stock getStock(String symbol){
        return null;
    }

    /**********************************************************************************************
     *                                   Override Functions                                       *
     *********************************************************************************************/
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(StockServiceTag, "StockService CREATED");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(StockServiceTag, "StockService DESTROYED");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(StockServiceTag, "StockService on Start Command");

        // Create Notification
        Notification mNotification = new NotificationCompat.Builder(this, NOTIF_CHANNEL_ID_STOCKSERVICE)
                .setContentTitle("Stock Service Turd Master")
                .setContentText("Hello Content Text? retard :^)")
                .setTicker("Hello Content Text? retard :^)")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setChannelId(NOTIF_CHANNEL_ID_STOCKSERVICE)
                .build();
        startForeground(NOTIF_ID_STOCKSERVICE, mNotification);

        // TODO: Background Thing

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
