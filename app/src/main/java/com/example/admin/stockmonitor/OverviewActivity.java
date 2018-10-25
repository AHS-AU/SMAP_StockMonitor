package com.example.admin.stockmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.stockmonitor.Classes.Sector;
import com.example.admin.stockmonitor.Classes.Stock;
import com.example.admin.stockmonitor.Classes.StockAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.admin.stockmonitor.Utilities.SharedConstants.*;

public class OverviewActivity extends Activity {
    private static final String TAG = OverviewActivity.class.getSimpleName();
    // UI Variables
    private ListView lvStocks;

    // Variables
    private ArrayList<Stock> mListOfStocks = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        // Set up UI variables
        lvStocks = findViewById(R.id.lvStocks);

        // TODO Replace these with dynamic from Room Persistance DB
        Stock stock1 = new Stock("Facebook", 99, 43, Sector.TECHNOLOGY);
        Stock stock2 = new Stock("Google", 12, 69, Sector.HEALTHCARE);
        mListOfStocks.add(stock1);
        mListOfStocks.add(stock2);

        // Set up the Custom Adapter StockAdapter that creates the UI from list_of_stocks.xml
        lvStocks.setAdapter(new StockAdapter(OverviewActivity.this, mListOfStocks));

        /**
         * Listview: Shows Stocks
         * Function: Start DetailsActivity with the Stock Item
         */
        lvStocks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Stock mStockItem = mListOfStocks.get(position);
                startDetailsActivity(mStockItem);
            }
        });
    }

    /**
     * Start Details Activity when item from Listview is clicked
     * @param vStockItem: Stock Object see /Classes/Stock.java for the class
     */
    public void startDetailsActivity(Stock vStockItem){
        Intent intDetailsActivity = new Intent(OverviewActivity.this, DetailsActivity.class);
        Bundle mStockBundle = new Bundle();
        mStockBundle.putSerializable(EXTRA_STOCK, vStockItem);
        intDetailsActivity.putExtras(mStockBundle);
        startActivityForResult(intDetailsActivity, REQ_OVERVIEW_UPDATE);
    }

    /**********************************************************************************************
     *                                   Override Functions                                       *
     *********************************************************************************************/
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_OK){
//            return;
//        }
//        switch(requestCode){
//            case REQ_OVERVIEW_UPDATE:
//                Log.d(TAG, "tmpDebug: Overview RESULT_OK");
//                mStock = (Stock)data.getSerializableExtra(EXTRA_STOCK);
//                updateUI();
//                break;
//            default:
//                Log.d(TAG, "Default Case for onActivityResult()");
//                break;
//        }
//    }
//
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
//
}
