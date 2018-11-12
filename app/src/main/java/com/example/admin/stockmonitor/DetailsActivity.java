package com.example.admin.stockmonitor;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.stockmonitor.Room.Book.Book;
import com.example.admin.stockmonitor.Room.Book.BookDao;
import com.example.admin.stockmonitor.Room.Book.BookDatabase;
import com.example.admin.stockmonitor.Room.Book.BookRepository;
import com.example.admin.stockmonitor.Utilities.Broadcaster.StockBroadcastReceiver;
import com.example.admin.stockmonitor.Utilities.Services.StockService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

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
    private TextView mTxtDisplaySymbol;
    private TextView mTxtDisplayPrimaryExchange;
    private TextView mTxtDisplayLatestValue;
    private TextView mTxtDisplayLatestUpdate;


    // Variables
    private Book mStock;
    private StockBroadcastReceiver mStockBroadcastReceiver;
    private static BookRepository mBookRepository = new BookRepository();
    private StockService mStockService;
    private boolean mStockServiceBound = false;
    private ServiceConnection mStockServiceConnection;
    public static String mSymbol;

    private BroadcastReceiver onDatabaseUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(FILTER_DB_UI_CHANGES.equals(action)){
                Log.d(TAG, FILTER_DB_UI_CHANGES);
                updateUi(mStockService.getStock(mSymbol));
            }
        }
    };

    private void SetupConnectionToStockService(){
        mStockServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mStockService = ((StockService.LocalBinder)service).getService();
                IntentFilter filter = new IntentFilter(FILTER_DB_UI_CHANGES);
                LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onDatabaseUpdateReceiver, filter);
                Book vStock = mStockService.getStock(mSymbol);
                Log.d(TAG, "Book = " + vStock.getSymbol() + " Price = " + vStock.getLatestPrice());
                updateUi(vStock);
                Log.d(TAG, "onServiceConnected " + TAG);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mStockService = null;
                LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onDatabaseUpdateReceiver);
                Log.d(TAG, "onServiceDisconnected " + TAG);
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        // Get Intent Information
        final Intent mIntent = getIntent();
        Bundle mStockBundle = mIntent.getExtras();
        mStock = (Book)mStockBundle.getSerializable(EXTRA_STOCK);
        mSymbol = mStock.getSymbol();

        // UI References: Buttons / Switches
        mBtnBack = findViewById(R.id.btnBack);
        mBtnDelete = findViewById(R.id.btnDelete);
        mBtnEdit = findViewById(R.id.btnEdit);

        // UI References: TextViews
        mTxtDisplayName = findViewById(R.id.txtDisplayName);
        mTxtDisplayPrice = findViewById(R.id.txtDisplayPrice);
        mTxtDisplayStocks = findViewById(R.id.txtDisplayStocks);
        mTxtDisplaySector = findViewById(R.id.txtDisplaySector);
        mTxtDisplaySymbol = findViewById(R.id.txtDisplaySymbol);
        mTxtDisplayPrimaryExchange = findViewById(R.id.txtDisplayPrimaryExchange);
        mTxtDisplayLatestValue = findViewById(R.id.txtDisplayLatestValue);
        mTxtDisplayLatestUpdate = findViewById(R.id.txtDisplayLatestUpdate);

        // Update UI with retrieved Intent
//        mTxtDisplayName.setText(mStock.getCompanyName());
//        mTxtDisplayPrice.setText(String.valueOf(mStock.getPurchasePrice()));
//        mTxtDisplayStocks.setText(String.valueOf(mStock.getNumberOfStocks()));
//        mTxtDisplaySector.setText(mStock.getSector());

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

        SetupConnectionToStockService();

    }


    private void deleteStock(Book stock){
        Log.d(TAG, "DELETE STOCK?");
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onDatabaseUpdateReceiver);
        BookDatabase db = BookDatabase.getInstance(getApplication());
        BookDao mBookDao = db.bookDao();
        mBookRepository.DeleteBook(mBookDao,stock);
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

    private void updateUi(Book book){
        mTxtDisplayName.setText(book.getCompanyName());
        mTxtDisplayPrice.setText(String.valueOf(book.getPurchasePrice()));
        mTxtDisplayStocks.setText(String.valueOf(book.getNumberOfStocks()));
        mTxtDisplaySymbol.setText(book.getSymbol());
        mTxtDisplayPrimaryExchange.setText(book.getPrimaryExchange());
        mTxtDisplayLatestValue.setText(book.getLatestPrice());
        mTxtDisplayLatestUpdate.setText(epochTimeConverter(book.getLatestUpdate()));
        mTxtDisplaySector.setText(book.getSector());
        mTxtDisplayPrice.setText(book.getPurchasePrice());
        mTxtDisplayStocks.setText(String.valueOf(book.getNumberOfStocks()));
    }

    private String epochTimeConverter(String rawTime){
        long epochValue = Long.parseLong(rawTime);
        Date epochTime = new Date(epochValue);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy @ HH:mm:ss", Locale.getDefault());
        String timeFormatted = simpleDateFormat.format(epochTime);
        return timeFormatted;
    }

    private void bindToStockService(){
        Log.d(TAG, mStockServiceConnection.toString());
        bindService(new Intent(DetailsActivity.this, StockService.class), mStockServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindToStockService(){
        Log.d(TAG, mStockServiceConnection.toString());
        unbindService(mStockServiceConnection);
        mStockService = null;
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onDatabaseUpdateReceiver);
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


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        bindToStockService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
        unbindToStockService();

    }

}
