package com.example.admin.stockmonitor.Utilities.Broadcaster;

import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.stockmonitor.OverviewActivity;
import com.example.admin.stockmonitor.Room.Book.Book;
import com.example.admin.stockmonitor.Utilities.SharedConstants;
import com.example.admin.stockmonitor.Utilities.ViewModels.BookViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.admin.stockmonitor.Utilities.SharedConstants.*;
public final class StockBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = "StockBroadCastReceiver";
    private RequestQueue mQueue;
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if(FILTER_DATA_AVAILABLE.equals(action)){
            Log.d(TAG, FILTER_DATA_AVAILABLE);
            Book newBook = new Book("NewCompany", "NewSymbol", "NewPE", "NewLP", "NewLU" );
            bookViewModel.insert(newBook);

        }else if (FILTER_DATA_UPDATE.equals(action)){
            Log.d(TAG, FILTER_DATA_UPDATE);
            // Size
            int bookSize = 0;
            if (bookViewModel.getAllStocks().getValue() != null){
                bookSize = bookViewModel.getAllStocks().getValue().size();
            }
            for (int i = 0; i < bookSize; i++){
                if (bookViewModel.getAllStocks().getValue() != null){
                    Book updateBook = bookViewModel.getAllStocks().getValue().get(i);
                    String symbol = updateBook.getSymbol();
                    SharedConstants sc = new SharedConstants();
                    String url = sc.getApiUrl(symbol);

                    if (mQueue == null) {
                        Log.d(TAG, "RequestQueue mQueue is null, requesting new queue");
                        mQueue = Volley.newRequestQueue(context);
                    }
                    JsonObjectRequest mRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONObject jsonObject = response.getJSONObject("quote");
                                        String latestPrice = jsonObject.getString("latestPrice");
                                        String latestUpdate = jsonObject.getString("latestUpdate");
                                        updateBook.setLatestPrice(latestPrice);
                                        bookViewModel.update(updateBook);
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

        }

    }
}
