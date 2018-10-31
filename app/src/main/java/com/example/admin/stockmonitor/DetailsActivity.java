package com.example.admin.stockmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.stockmonitor.Room.Book.Book;
import com.example.admin.stockmonitor.Room.Book.BookDao;
import com.example.admin.stockmonitor.Room.Book.BookDatabase;
import com.example.admin.stockmonitor.Utilities.Broadcaster.StockBroadcastReceiver;
import com.example.admin.stockmonitor.Utilities.StockIntentFilter;

import static com.example.admin.stockmonitor.Utilities.SharedConstants.*;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = DetailsActivity.class.getSimpleName();

    // UI Variables
    private Button mBtnBack;
    private Button mBtnDelete;
    private Button mBtnEdit;
    private TextView mTxtDisplayName;
    private TextView mTxtDisplayPrice;
    private TextView mTxtDisplayStocks;
    private TextView mTxtDisplaySector;

    // Variables
    private Book mStock;
    private StockBroadcastReceiver mStockBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        // Get Intent Information
        final Intent mIntent = getIntent();
        Bundle mStockBundle = mIntent.getExtras();
        mStock = (Book)mStockBundle.getSerializable(EXTRA_STOCK);

        // UI References: Buttons / Switches
        mBtnBack = findViewById(R.id.btnBack);
        mBtnDelete = findViewById(R.id.btnDelete);
        mBtnEdit = findViewById(R.id.btnEdit);

        // UI References: TextViews
        mTxtDisplayName = findViewById(R.id.txtDisplayName);
        mTxtDisplayPrice = findViewById(R.id.txtDisplayPrice);
        mTxtDisplayStocks = findViewById(R.id.txtDisplayStocks);
        mTxtDisplaySector = findViewById(R.id.txtDisplaySector);

        // Update UI with retrieved Intent
        mTxtDisplayName.setText(mStock.getCompanyName());
        mTxtDisplayPrice.setText(String.valueOf(mStock.getPurchasePrice()));
        mTxtDisplayStocks.setText(String.valueOf(mStock.getNumberOfStocks()));
        mTxtDisplaySector.setText(mStock.getSector());

        // Action Bar
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.DetailsActionbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        /**
         * Button: Back
         * Function: Return to Previous Activity
         */
        mBtnBack.setOnClickListener(v -> onBackPressed());

        /**
         * Button: Delete
         * Function: Delete the chosen Stock from DB
         */
        mBtnDelete.setOnClickListener(v -> deleteStock(mStock));

        /**
         * Button: Edit
         * Function: Start "EditActivity" with necessary stock data
         */

        mBtnEdit.setOnClickListener(v -> startEditActivity());

    }

    private void deleteStock(Book stock){
        Log.d(TAG, "DELETE STOCK?");
        bookViewModel.delete(stock);
        setResult(RESULT_CANCELED);
        finish();

    }

    private void startEditActivity(){
        Intent intEditActivity = new Intent (this, EditActivity.class);
        // Create a Stock Bundle and put it in intent
        Bundle mStockBundle = new Bundle();
        mStockBundle.putSerializable(EXTRA_STOCK, mStock);
        intEditActivity.putExtras(mStockBundle);
        // Start EditActivity for Result
        startActivityForResult(intEditActivity, REQ_DETAILS_UPDATE);

    }


    /**********************************************************************************************
     *                                    Override Functions                                      *
     *********************************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch(requestCode){
            case REQ_DETAILS_UPDATE:
                setResult(RESULT_OK, data);
                finish();
                break;
        }
    }
}
