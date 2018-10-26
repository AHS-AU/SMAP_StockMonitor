package com.example.admin.stockmonitor.Utilities;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.support.v4.app.NotificationCompat;


import com.example.admin.stockmonitor.Classes.Stock;
import com.example.admin.stockmonitor.R;

import java.util.List;

import static com.example.admin.stockmonitor.Utilities.SharedConstants.*;

public class StockService extends Service {
    private List<Stock> mStockList;

    private int wildCounter = 0;
    private boolean isRunning = false;
    private static final long mServiceInterval = 10*1000;


    // Constructor
    public StockService() {
    }

    private List<Stock> getStocks(){
        return mStockList;
    }


    // TODO: Complete the method
    private Stock getStock(String symbol){
        return null;
    }

    private static void doBackgroundThing(final long waitTimeInMillis){
        AsyncTask<Object, Object, String> task = new AsyncTask<Object, Object, String>(){
            // Runs in UI before background thread is called
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }

            // Background computation is being handled
            @Override
            protected String doInBackground(Object... objects) {
                String s = "Background job";
                try{
                    Log.d(StockServiceTag, "Task Started");
                    Thread.sleep(waitTimeInMillis);
                    Log.d(StockServiceTag, "Task Completed");
                } catch (Exception e){
                    s += " did not finish due to error";
                    return s;
                }

                s += " completed after " + waitTimeInMillis + " ms";
                return s;
            }

            // Runs in UI when background thread has finished
            @Override
            protected void onPostExecute(String stringResult) {
                super.onPostExecute(stringResult);
                Log.d(StockServiceTag, "tmpDebug Broadcasting. " + stringResult);
            }
        };

        task.execute();

    }

    /**********************************************************************************************
     *                                   Override Functions                                       *
     *********************************************************************************************/
    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = true;
        Log.d(StockServiceTag, "StockService CREATED");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        Log.d(StockServiceTag, "StockService DESTROYED");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(StockServiceTag, "StockService on Start Command");
        new Thread(new Runnable() {
            @Override
            public void run() {

                while(isRunning){
                    try{
                        wildCounter++;
                        Thread.sleep(mServiceInterval);
                        Notification mNotification = new NotificationCompat.Builder(StockService.this, NOTIF_CHANNEL_ID_STOCKSERVICE)
                                .setContentTitle("Stock Service Turd Master " + wildCounter)
                                .setContentText("Hello Content Text? retard :^) " + wildCounter)
                                .setTicker("Hello Content Text? retard :^) " + wildCounter)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setChannelId(NOTIF_CHANNEL_ID_STOCKSERVICE)
                                .build();
                        startForeground(NOTIF_ID_STOCKSERVICE, mNotification);

                        // TODO: Background Thing
                        doBackgroundThing(5*1000);

                    }catch (Exception e){
                        Log.d(StockServiceTag, "Error in Thread Sleep onStartCommand");
                    }
                    Log.d(StockServiceTag, "Service is Running");
                }

                stopSelf();

            }
        }).start();

//        //Create Notification
//        Notification mNotification = new NotificationCompat.Builder(this, NOTIF_CHANNEL_ID_STOCKSERVICE)
//                .setContentTitle("Stock Service Turd Master")
//                .setContentText("Hello Content Text? retard :^)")
//                .setTicker("Hello Content Text? retard :^)")
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setChannelId(NOTIF_CHANNEL_ID_STOCKSERVICE)
//                .build();
//        startForeground(NOTIF_ID_STOCKSERVICE, mNotification);
//
//        // TODO: Background Thing
//        doBackgroundThing(5*1000);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        // Activities must bind to the Service to get the newest stock data.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
