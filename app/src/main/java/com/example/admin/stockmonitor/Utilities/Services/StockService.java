package com.example.admin.stockmonitor.Utilities.Services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
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
import com.example.admin.stockmonitor.Utilities.SharedConstants;
import com.example.admin.stockmonitor.Utilities.ViewModels.BookViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
                Log.d(StockServiceTag, stringResult);
            }
        };

        task.execute();

    }

    public void addToDb(){
        BookDatabase db;
        RequestQueue mQueue = null;
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }
        SharedConstants sc = new SharedConstants();
        ArrayList<String> urlArray = new ArrayList();
        urlArray.add(sc.getApiUrl("ATVI"));
        urlArray.add(sc.getApiUrl("GOOGL"));
        urlArray.add(sc.getApiUrl("AAPL"));
        urlArray.add(sc.getApiUrl("AMZN"));
        urlArray.add(sc.getApiUrl("CERN"));
        urlArray.add(sc.getApiUrl("NFLX"));
        urlArray.add(sc.getApiUrl("FB"));
        urlArray.add(sc.getApiUrl("EA"));
        urlArray.add(sc.getApiUrl("TSLA"));
        urlArray.add(sc.getApiUrl("EBAY"));

        for (int i = 0; i < urlArray.size(); i++){
            JsonObjectRequest mRequest = new JsonObjectRequest(Request.Method.GET, urlArray.get(i), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject jsonObject = response.getJSONObject("quote");
                                String companyName = jsonObject.getString("companyName");
                                String symbol = jsonObject.getString("symbol");
                                String primaryExchange = jsonObject.getString("primaryExchange");
                                String latestPrice = jsonObject.getString("latestPrice");
                                String latestUpdate = jsonObject.getString("latestUpdate");
                                Book mBook = new Book(companyName,symbol,primaryExchange,latestPrice,latestUpdate);
                                bookViewModel.insert(mBook);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            mQueue.add(mRequest);
        }
    }

    /**********************************************************************************************
     *                                   Override Functions                                       *
     *********************************************************************************************/
    @Override
    public void onCreate() {
        super.onCreate();
        //isRunning = true;
        //addToDb();
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
