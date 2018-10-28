package com.example.admin.stockmonitor.Utilities.Services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.support.v4.app.NotificationCompat;


import com.example.admin.stockmonitor.Classes.Stock;
import com.example.admin.stockmonitor.OverviewActivity;
import com.example.admin.stockmonitor.R;
import com.example.admin.stockmonitor.Room.Book.Book;
import com.example.admin.stockmonitor.Utilities.ViewModels.BookViewModel;

import java.util.List;

import static com.example.admin.stockmonitor.Utilities.SharedConstants.*;

public class StockService extends Service {
    private List<Stock> mStockList;

    private int wildCounter = 0;
    private boolean isRunning = false;
    private static final long mServiceInterval = 6*1000;


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

    private static void doBackgroundThing(){
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
                    Thread.sleep(5*1000);
//                    Book newBook = new Book("NewCompany", "NewSymbol", "NewPE", "NewLP", "NewLU" );
//                    bookViewModel.insert(newBook);
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
                Book newBook = new Book("NewCompany", "NewSymbol", "NewPE", "NewLP", "NewLU" );
                bookViewModel.insert(newBook);
                Log.d(StockServiceTag, stringResult);
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
        //isRunning = true;
        Log.d(StockServiceTag, "tmpDebug: StockService CREATED");
    }

    // Important, this avoids Memory Leak from Service
    @Override
    public boolean onUnbind(Intent intent) {
        //isRunning = false;
        Log.d(StockServiceTag, "tmpDebug: StockService onUnbind");
        return super.onUnbind(intent);
    }



    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        isRunning = true;
        Log.d(StockServiceTag, "tmpDebug: StockService onRebind");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        Log.d(StockServiceTag, "tmpDebug: StockService DESTROYED");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(StockServiceTag, "tmpDebug: StockService on Start Command");
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

                        // TODO: Background Thing
                        doBackgroundThing();

                    }catch (InterruptedException e){
                        Log.d(StockServiceTag, "tmpDebug: Error in Thread Sleep onStartCommand");
                    }
                    Log.d(StockServiceTag, "tmpDebug: Service is Running");
                }

                stopSelf();

            }
        });
        if(!isRunning){
            serviceThread.start();
            isRunning = true;
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        // Activities must bind to the Service to get the newest stock data.
        //throw new UnsupportedOperationException("Not yet implemented");
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public StockService getService(){
            return StockService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();
}
