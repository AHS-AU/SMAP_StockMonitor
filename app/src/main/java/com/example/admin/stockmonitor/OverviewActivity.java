package com.example.admin.stockmonitor;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.stockmonitor.Room.Book.Book;
import com.example.admin.stockmonitor.Utilities.Adapters.StockAdapter;
import com.example.admin.stockmonitor.Utilities.Broadcaster.StockBroadcastReceiver;
import com.example.admin.stockmonitor.Utilities.Services.StockService;
import com.example.admin.stockmonitor.Utilities.SharedConstants;
import com.example.admin.stockmonitor.Utilities.StockIntentFilter;
import com.example.admin.stockmonitor.Utilities.ViewModels.BookViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.admin.stockmonitor.Utilities.SharedConstants.*;
import static com.google.gson.reflect.TypeToken.get;

public class OverviewActivity extends AppCompatActivity {
    private static final String TAG = OverviewActivity.class.getSimpleName();
    // UI Variables
    private ListView lvStocks;
    private Button btnAddStock;


    // Variables
    private RequestQueue mQueue;
    private StockBroadcastReceiver mStockBroadcastReceiver = new StockBroadcastReceiver();
    private boolean isServiceBound = false;

    //private BookViewModel bookViewModel;
    private StockIntentFilter mIntentFilter = new StockIntentFilter();
    private Intent mIntent;

    private StockService mStockService;


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            mStockService = ((StockService.LocalBinder)service).getService();
            Log.d(TAG, "Service Connected to OverviewActivity");

        }

        public void onServiceDisconnected(ComponentName name) {
            mStockService = null;
            Log.d(TAG, "Service Disconnected from OverviewActivity");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        // Set up UI variables
        lvStocks = findViewById(R.id.lvStocks);
        btnAddStock = findViewById(R.id.btnAddStock);

        // TODO Replace these with dynamic from Room Persistance DB

        bookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);
        bookViewModel.getAllStocks().observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(@Nullable List<Book> books) {
                // Set up the Custom Adapter StockAdapter that creates the UI from list_of_stocks.xml
                lvStocks.setAdapter(new StockAdapter(OverviewActivity.this, books));
            }
        });

        // Set up Service Intent
//        Intent mStockServiceIntent = new Intent(this, StockService.class);
//        startService(mStockServiceIntent);
        //ContextCompat.startForegroundService(this, mStockServiceIntent);


        /**
         * Listview: Shows Stocks
         * Function: Start DetailsActivity with the Stock Item
         */
        lvStocks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (bookViewModel.getAllStocks().getValue()!= null ){
                    Book stock = bookViewModel.getAllStocks().getValue().get(position);
                    startDetailsActivity(stock);
                }
            }
        });

        btnAddStock.setOnClickListener(v -> addStock());

    }

    public void addStock() {

    }

    /**
     * Start Details Activity when item from Listview is clicked
     * @param vStockItem: Stock Object see /Classes/Stock.java for the class
     */
    public void startDetailsActivity(Book vStockItem){
        Intent intDetailsActivity = new Intent(OverviewActivity.this, DetailsActivity.class);
        Bundle mStockBundle = new Bundle();
        mStockBundle.putSerializable(EXTRA_STOCK, vStockItem);
        intDetailsActivity.putExtras(mStockBundle);
        startActivityForResult(intDetailsActivity, REQ_OVERVIEW_UPDATE);
    }


    /**********************************************************************************************
     *                                   Override Functions                                       *
     *********************************************************************************************/
    @Override
    protected void onResume() {
        super.onResume();
        if (!isServiceBound){
            Log.d(TAG, "onResume() Service Starting");
            bindService(new Intent(OverviewActivity.this, StockService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
            isServiceBound = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mServiceConnection != null && isServiceBound){
            Log.d(TAG, "onDestroy() Service Dead");
            unbindService(mServiceConnection);
            isServiceBound = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() Registering Receivers");
        Intent mStockServiceIntent = new Intent(this, StockService.class);
        startService(mStockServiceIntent);
        mIntent = new Intent(FILTER_DATA_AVAILABLE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mStockBroadcastReceiver, mIntentFilter.getIntentFilter());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() Unregistering Receivers");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mStockBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        if (mServiceConnection != null && isServiceBound){
//            Log.d(TAG, "onDestroy() Service Dead");
//            unbindService(mServiceConnection);
//            isServiceBound = false;
//        }
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK){
            Log.d(TAG, "onActivityResult() resultCode != RESULT_OK");
            return;
        }
        switch(requestCode){
            case REQ_OVERVIEW_UPDATE:
                Log.d(TAG, "onActivityResult() requestCode = REQ_OVERVIEW_UPDATE");
//                mStock = (Stock)data.getSerializableExtra(EXTRA_STOCK);
//                updateUI();
                break;
            default:
                Log.d(TAG, "onActivityResult() default case");
                break;
        }
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putSerializable(SAVE_STOCK,mStock);
//    }
//
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        mStock = (Stock)savedInstanceState.getSerializable(SAVE_STOCK);
//        updateUI();
//    }

}
