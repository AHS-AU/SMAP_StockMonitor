package com.example.admin.stockmonitor.Utilities.Services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.support.v4.app.NotificationCompat;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.stockmonitor.Classes.Stock;
import com.example.admin.stockmonitor.OverviewActivity;
import com.example.admin.stockmonitor.R;
import com.example.admin.stockmonitor.Room.Book.Book;
import com.example.admin.stockmonitor.Room.Book.BookDatabase;
import com.example.admin.stockmonitor.Utilities.Broadcaster.StockBroadcastReceiver;
import com.example.admin.stockmonitor.Utilities.SharedConstants;
import com.example.admin.stockmonitor.Utilities.StockIntentFilter;
import com.example.admin.stockmonitor.Utilities.ViewModels.BookViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.admin.stockmonitor.Utilities.SharedConstants.*;

public class StockService extends Service {
    private List<Stock> mStockList;

    private final IBinder mBinder = new LocalBinder();
    public static final String TAG = "StockService";
    private int wildCounter = 0;
    private boolean isRunning = false;
    private static final long mServiceInterval = 6*1000;
    private StockBroadcastReceiver mStockBroadcastReceiver = new StockBroadcastReceiver();
    private StockIntentFilter mIntentFilter = new StockIntentFilter();

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

    /**
     * Background Service updates the Stocks Every 2 min
     * @param context
     * @param intent
     */
    private static void doBackgroundThing(Context context, Intent intent){
        AsyncTask<Object, Object, String> task = new AsyncTask<Object, Object, String>(){
            // Runs in UI before background thread is called
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }

            // Background computation is being handled
            @Override
            protected String doInBackground(Object... objects) {
                String s = "Background Service Job";
                try{
                    Thread.sleep(5*1000);
                    Log.d(TAG, "Background Service is about to send Broadcast with IntentAction = " + FILTER_DATA_UPDATE);
                    intent.setAction(FILTER_DATA_UPDATE);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                } catch (Exception e){
                    s += " did not finish due to error";
                    return s;
                }
                s += " has finished properly";
                return s;
            }

            // Runs in UI when background thread has finished
            @Override
            protected void onPostExecute(String stringResult) {
                super.onPostExecute(stringResult);
                Log.d(TAG, stringResult);
            }
        };

        task.execute();

    }

    /**
     * Service Binder
     */
    public class LocalBinder extends Binder {
        public StockService getService(){
            return StockService.this;
        }
    }


    /**********************************************************************************************
     *                                   Override Functions                                       *
     *********************************************************************************************/
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() Registering BroadcastReceiver = " + mStockBroadcastReceiver.TAG);
        registerReceiver(mStockBroadcastReceiver, mIntentFilter);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        isRunning = true;
        Log.d(TAG, "onRebind() isRunning = " + isRunning);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        Log.d(TAG, "StockService onDestroy() Unregistering BroadcastReceiver = " +
                mStockBroadcastReceiver.TAG + " and isRunning = " + isRunning);
        unregisterReceiver(mStockBroadcastReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() about to create serviceThread: Creates Foreground & Background Service");

        Thread serviceThread = new Thread(new Runnable() {
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

                        // Opens AsyncTask for Background Service
                        doBackgroundThing(getApplicationContext(), intent);

                    }catch (InterruptedException e){
                        Log.d(TAG, "Error: " + e + " in serviceThread");
                    }
                    Log.d(TAG, "serviceThread Service isRunning = " + isRunning);
                }

                Log.d(TAG, "serviceThread stopSelf() called");
                stopSelf();

            }
        });
        if(!isRunning){
            serviceThread.start();
            isRunning = true;
            Log.d(TAG, "Service started and isRunning = " + isRunning);
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

//    public class LocalBinder extends Binder {
//        public StockService getService(){
//            return StockService.this;
//        }
//    }

    //private final IBinder mBinder = new LocalBinder();
}
