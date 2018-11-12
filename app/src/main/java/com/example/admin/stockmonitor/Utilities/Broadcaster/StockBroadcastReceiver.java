package com.example.admin.stockmonitor.Utilities.Broadcaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.stockmonitor.R;
import com.example.admin.stockmonitor.Room.Book.Book;
import com.example.admin.stockmonitor.Room.Book.BookDao;
import com.example.admin.stockmonitor.Room.Book.BookDatabase;
import com.example.admin.stockmonitor.Room.Book.BookRepository;
import com.example.admin.stockmonitor.Utilities.SharedConstants;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.admin.stockmonitor.Utilities.SharedConstants.*;

public final class StockBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = "StockBroadcastReceiver";
    private RequestQueue mQueue;
    private BookRepository mBookRepository = new BookRepository();


    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if(FILTER_DATA_SINGLE_UPDATE.equals(action)){
            Log.d(TAG, FILTER_DATA_SINGLE_UPDATE);
            addStock(context,intent,action);

        }else if (FILTER_DATA_UPDATE.equals(action)){
            Log.d(TAG, FILTER_DATA_UPDATE);

            BookDatabase db = BookDatabase.getInstance(context);
            BookDao mBookDao = db.bookDao();
            if (mQueue == null){
                Log.d(TAG, "mQueue is null, requesting new Volley RequestQueue");
                mQueue = Volley.newRequestQueue(context);
            }
            mBookRepository.UpdateAllBooks(mBookDao,mQueue);
        }

    }

    /**
     * Convenient Method to add stock into DB
     * @param context : Context
     * @param intent : Intent
     * @param action : Filter Action
     */
    private void addStock(Context context, Intent intent, String action){
        String mSymbol = intent.getStringExtra(EXTRA_SYMBOL);
        String mPurchasePrice = intent.getStringExtra(EXTRA_PURCHASE_PRICE);
        int mNumberOfStocks = intent.getIntExtra(EXTRA_NUMBER_OF_STOCKS, 1);

        SharedConstants sc = new SharedConstants();
        String url = sc.getApiUrl(mSymbol);
        if (mQueue == null){
            Log.d(TAG, "RequestQueue mQueue is null, requesting new queue");
            mQueue = Volley.newRequestQueue(context);
        }

        JsonObjectRequest mRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("quote");
                            String companyName = jsonObject.getString("companyName");
                            String symbol = jsonObject.getString("symbol"); // yes even symbol to validate correct
                            String primaryExchange = jsonObject.getString("primaryExchange");
                            String latestPrice = jsonObject.getString("latestPrice");
                            String latestUpdate = jsonObject.getString("latestUpdate");
                            String change = jsonObject.getString("change");
                            String sector = jsonObject.getString("sector");
                            Book addBook = new Book(companyName,symbol,primaryExchange,
                                    latestPrice,latestUpdate,change,sector,
                                    mPurchasePrice,mNumberOfStocks);
                            BookDatabase db = BookDatabase.getInstance(context);
                            BookDao mBookDao = db.bookDao();
                            mBookRepository.InsertBook(mBookDao, addBook);
                            Toast.makeText(context,context.getResources().getString(R.string.adding) +
                                    mSymbol + context.getResources().getString(R.string.tothedb), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,context.getResources().getString(R.string.nosymbolfound) + mSymbol, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onErrorResponse() in " + action + " see Stack Trace for more info");
                error.printStackTrace();
            }
        });
        mQueue.add(mRequest);
    }
}
