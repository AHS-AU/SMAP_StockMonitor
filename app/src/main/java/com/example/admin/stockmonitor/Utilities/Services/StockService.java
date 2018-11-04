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

import com.example.admin.stockmonitor.R;
import com.example.admin.stockmonitor.Room.Book.Book;
import com.example.admin.stockmonitor.Room.Book.BookRepository;
import com.example.admin.stockmonitor.Utilities.Broadcaster.StockBroadcastReceiver;
import com.example.admin.stockmonitor.Utilities.StockIntentFilter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.admin.stockmonitor.Utilities.SharedConstants.*;

public class StockService extends Service {
    public static final String TAG = "StockService";

    // Variables
    private final IBinder mBinder = new LocalBinder();
    private boolean isRunning = false;
    private static final long mServiceInterval = 120*1000;  // 2 min service interval
    private StockBroadcastReceiver mStockBroadcastReceiver = new StockBroadcastReceiver();
    private StockIntentFilter mIntentFilter = new StockIntentFilter();
    BookRepository mBookRepository = new BookRepository();


    /**
     * @return : A List of Stocks from Database
     */
    public List<Book> getAllStocks(){
        return mBookRepository.GetAllBooks(getApplicationContext());
//        BookDatabase db = BookDatabase.getInstance(getApplicationContext());
//        BookDao mBookDao = db.bookDao();
//        return mBookDao.getAllStocksOnStart();
    }


    /**
     * @param symbol : Book Object's stock symbol
     * @return : A single Stock from Book Object
     */
    public Book getStock(String symbol){
        Book mBook = mBookRepository.GetBookBySymbol(getApplicationContext(),symbol);
        if(mBook != null){
            Log.d(TAG, "getStock() found Book with Symbol = " + mBook.getSymbol());
            return mBook;
        }
        Log.d(TAG, "getStock() no Book with matched symbol = " + symbol + " was found");
        return null;
    }

    /**
     * Background Service updates the Stocks Every 2 min
     * @param context : Application Context
     * @param intent : Intent
     */
    private static void doBackgroundThing(Context context, Intent intent){
        AsyncTask<Object, Object, String> asyncTask = new AsyncTask<Object, Object, String>(){
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }

            // Background computation is being handled
            @Override
            protected String doInBackground(Object... objects) {
                String s = "Background Service job";
                try{
                    Thread.sleep(5*1000);
                    intent.setAction(FILTER_DATA_UPDATE);
                    Log.d(TAG, "Background Service is about to send Broadcast with IntentAction = " + intent.getAction());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                } catch (Exception e){
                    s += " did not finish due to error";
                    return s;
                }
                s += " has finished properly";
                return s;
            }

            @Override
            protected void onPostExecute(String stringResult) {
                super.onPostExecute(stringResult);
                intent.setAction(FILTER_DB_UI_CHANGES);
                Log.d(TAG, "Background Service is about to send Broadcast with IntentAction = " + intent.getAction());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                Log.d(TAG, stringResult);
            }
        };

        asyncTask.execute();

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
        Log.d(TAG, "onCreate()");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "onRebind() isRunning = " + isRunning);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        Log.d(TAG, "onDestroy() isRunning = " + isRunning);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() about to create serviceThread: Creates Foreground & Background Service");

        Thread serviceThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isRunning){
                    try{
                        Thread.sleep(mServiceInterval);
                        SimpleDateFormat mTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                        SimpleDateFormat mDate = new SimpleDateFormat("EEEE, dd. MMMM YYYY", Locale.getDefault());
                        String mNotificationMessage = "Last checked stocks prices at: " + mTime.format(new Date());
                        Notification mNotification = new NotificationCompat.Builder(StockService.this, NOTIF_CHANNEL_ID_STOCKSERVICE)
                                .setContentTitle(getApplicationContext().getString(R.string.alt_app_name))
                                .setContentText(mNotificationMessage)
                                .setTicker(mNotificationMessage)
                                .setSubText(mDate.format(new Date()))
                                .setShowWhen(false) // Removes the Default Timestamp
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setChannelId(NOTIF_CHANNEL_ID_STOCKSERVICE)
                                //.setDefaults(Notification.DEFAULT_SOUND) // Default sound, very annoying :)
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

}
