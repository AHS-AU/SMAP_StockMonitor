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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.admin.stockmonitor.Room.Book.Book;
import com.example.admin.stockmonitor.Room.Book.BookDao;
import com.example.admin.stockmonitor.Room.Book.BookDatabase;
import com.example.admin.stockmonitor.Utilities.Adapters.StockAdapter;
import com.example.admin.stockmonitor.Room.Book.BookRepository;
import com.example.admin.stockmonitor.Utilities.Broadcaster.StockBroadcastReceiver;
import com.example.admin.stockmonitor.Utilities.Dialogs.AddStockDialog;
import com.example.admin.stockmonitor.Utilities.Services.StockService;
import com.example.admin.stockmonitor.Utilities.StockIntentFilter;

import java.util.List;

import static com.example.admin.stockmonitor.Utilities.SharedConstants.*;


public class OverviewActivity extends AppCompatActivity implements AddStockDialog.AddStockDialogListener {
    private static final String TAG = OverviewActivity.class.getSimpleName();
    // UI Variables
    private ListView lvStocks;
    private Button btnAddStock;


    // Variables
    private StockBroadcastReceiver mStockBroadcastReceiver = new StockBroadcastReceiver();
    private boolean isServiceBound = false;
    private Book mStock;
    private static BookRepository mBookRepository = new BookRepository();
    private boolean isRefreshing = false;
    private StockIntentFilter mIntentFilter = new StockIntentFilter();
    private StockService mStockService;
    private StockAdapter mStockAdapter = new StockAdapter(OverviewActivity.this, null);

    private BroadcastReceiver onDatabaseUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            notifyStockAdapterChanges(mStockService.getAllStocks());
        }
    };


    public void notifyStockAdapterChanges(List<Book> books){
        mStockAdapter.setBookList(books);
        mStockAdapter.notifyDataSetChanged();
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            mStockService = ((StockService.LocalBinder)service).getService();

            mStockAdapter.setBookList(mStockService.getAllStocks());
            mStockAdapter.notifyDataSetChanged();
            // Handler with delay in case of slow connection.
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mStockAdapter.setBookList(mStockService.getAllStocks());
                    mStockAdapter.notifyDataSetChanged();
                }
            }, 1000);
            IntentFilter filter = new IntentFilter(FILTER_DB_UI_CHANGES);
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onDatabaseUpdateReceiver, filter);
            Log.d(TAG, "Service Connected to OverviewActivity");

        }

        public void onServiceDisconnected(ComponentName name) {
            mStockService = null;
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onDatabaseUpdateReceiver);
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

        // Access the DB
        initDb();
        lvStocks.setAdapter(mStockAdapter);

        /**
         * Listview: Shows Stocks
         * Function: Start DetailsActivity with the Stock Item
         */
        lvStocks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book stock = mStockService.getAllStocks().get(position);
                startDetailsActivity(stock);
            }
        });

        btnAddStock.setOnClickListener(v -> openAddStockDialog());

        // Init Service & its necessary data
        Intent mStockServiceIntent = new Intent(this, StockService.class);
        startService(mStockServiceIntent);
        LocalBroadcastManager.getInstance(this).registerReceiver(mStockBroadcastReceiver, mIntentFilter.getIntentFilter());

    }

    public void openAddStockDialog(){
        AddStockDialog dialog = new AddStockDialog();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), dialog.getTag());

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

    private void refreshStocks(final boolean enable){
        if(enable){
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LocalBroadcastManager.getInstance(OverviewActivity.this).sendBroadcast(new Intent(FILTER_DB_UI_CHANGES));
                    isRefreshing = false;
                    invalidateOptionsMenu();
                }
            }, 1000);   // This delay should be fine for about 50 stocks, it's lazy implementation ;)
            isRefreshing = true;
            LocalBroadcastManager.getInstance(OverviewActivity.this).sendBroadcast(new Intent(FILTER_DATA_UPDATE));
        } else{
            isRefreshing = false;
        }
        invalidateOptionsMenu();
    }

    public void initDb(){
        mBookRepository.InitDatabase(getApplicationContext());
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
//        Intent mStockServiceIntent = new Intent(this, StockService.class);
//        startService(mStockServiceIntent);
//        LocalBroadcastManager.getInstance(this).registerReceiver(mStockBroadcastReceiver, mIntentFilter.getIntentFilter());
//        mIntent = new Intent(FILTER_DATA_SINGLE_UPDATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() Unregistering Receivers");
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mStockBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mStockBroadcastReceiver);

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
                mStock = (Book)data.getSerializableExtra(EXTRA_STOCK);
                BookDatabase db = BookDatabase.getInstance(getApplication());
                BookDao mBookDao = db.bookDao();
                mBookRepository.UpdateBook(mBookDao, mStock);

                break;
            default:
                Log.d(TAG, "onActivityResult() default case");
                break;
        }
    }

    /**
     * See AddStockDialog
     * @param symbol : user input symbol
     * @param purchasePrice : user input purchase price
     * @param numberOfStocks : user input number of stocks
     */
    @Override
    public void getStockInfo(String symbol, String purchasePrice, int numberOfStocks) {
        Log.d(TAG,"getStockInfo(), symbol = " + symbol + ", purchasePrice = " + purchasePrice + ", numOfStocks = " + numberOfStocks);
        Book mStock = mStockService.getStock(symbol);
        Log.d(TAG, "getStockInfo() after getStock");
        String mSymbol;
        if (mStock != null){
            mSymbol = mStock.getSymbol();
        } else {
            mSymbol = "ERROR_NULL";
        }

        if (mSymbol.equals(symbol)){
            Log.d(TAG, "getStockInfo() Stock(" + mSymbol + ") already exists in the Database");
            Toast.makeText(OverviewActivity.this, "Stock: " + mSymbol + " already exists", Toast.LENGTH_SHORT).show();
        }else if (symbol.equals("ERROR")){
            Toast.makeText(OverviewActivity.this, "Error: All inputs required", Toast.LENGTH_SHORT).show();
        } else{
            Intent intent = new Intent(FILTER_DATA_SINGLE_UPDATE);
            intent.putExtra(EXTRA_SYMBOL, symbol );
            intent.putExtra(EXTRA_PURCHASE_PRICE, purchasePrice);
            intent.putExtra(EXTRA_NUMBER_OF_STOCKS, numberOfStocks);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            Log.d(TAG, "getStockInfo() Stock(" + symbol + ") sent to Broadcastreceiver with action = " + intent.getAction());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //menu.findItem(R.id.menu_refresh).setActionView(R.layout.actionbar_refresh);
        if(!isRefreshing){
            menu.findItem(R.id.menu_refresh).setActionView(null);
            menu.findItem(R.id.menu_refresh).setVisible(true);
        } else{
            menu.findItem(R.id.menu_refresh).setActionView(R.layout.actionbar_refresh);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_refresh:
                refreshStocks(true);
                return true;
        }
        return true;
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
