package com.example.admin.stockmonitor;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.admin.stockmonitor.Room.Book.Book;
import com.example.admin.stockmonitor.Utilities.Services.StockService;

import static com.example.admin.stockmonitor.Utilities.SharedConstants.*;

public class EditActivity extends AppCompatActivity {
    private static final String TAG = EditActivity.class.getSimpleName();
    // UI Variables
    private Button mBtnCancel;
    private Button mBtnSave;
    private EditText mEditName;
    private EditText mEditPrice;
    private EditText mEditStocks;
    private TextView mTxtSector;

    // Variables
    private Book mStock;
    private ServiceConnection mStockServiceConnection;
    private StockService mStockService;
    private static String mSymbol;

    /**
     * BroadcastReceiver
     */
    private BroadcastReceiver onDatabaseUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(FILTER_DB_UI_CHANGES.equals(action)){
                Log.d(TAG, FILTER_DB_UI_CHANGES);
                // Update UI
                UpdateStock(mStockService.getStock(mSymbol));
            }
        }
    };


    /**
     * ServiceConnection
     */
    private void SetupConnectionToStockService(){
        mStockServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mStockService = ((StockService.LocalBinder)service).getService();
                IntentFilter filter = new IntentFilter(FILTER_DB_UI_CHANGES);
                LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(onDatabaseUpdateReceiver, filter);
                UpdateStock(mStockService.getStock(mSymbol));
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
        setContentView(R.layout.activity_edit);
        // Get Intent Information
        final Intent mIntent = getIntent();
        Bundle mStockBundle = mIntent.getExtras();
        mStock = (Book)mStockBundle.getSerializable(EXTRA_STOCK);
        mSymbol = mStock.getSymbol();

        // UI References: Buttons / Switches
        mBtnCancel = findViewById(R.id.btnCancel);
        mBtnSave = findViewById(R.id.btnSave);
        mEditName = findViewById(R.id.editName);
        mEditPrice = findViewById(R.id.editPrice);
        mEditStocks = findViewById(R.id.editStocks);
        mTxtSector = findViewById(R.id.txtShowSector);

        // Update UI with retrieved Intent, these are static/defined by user so intent is fine.
        mEditName.setText(mStock.getCompanyName());
        mEditPrice.setText(String.valueOf(mStock.getPurchasePrice()));
        mEditStocks.setText(String.valueOf(mStock.getNumberOfStocks()));
        mTxtSector.setText(mStock.getSector());

        // Action Bar
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.DetailsActionbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        /**
         * Button: Cancel
         * Function: Return to Previous Activity without any changes
         */
        mBtnCancel.setOnClickListener(v -> onBackPressed());

        /**
         * Button: Save
         * Function: Save changes from the four categories
         * Then finish and continue from OverviewActivity
         */
        mBtnSave.setOnClickListener(v -> saveButtonFunction());

        // Setup the StockService
        SetupConnectionToStockService();

    }

    /**
     * Retrieves enter information and saves it.
     * Returns to DetailActivity with updated information filled by the user.
     */
    private void saveButtonFunction(){
        // First Error handling to ensure all fields have been filled.
        if (TextUtils.isEmpty(mEditPrice.getText())){
            mEditPrice.setError("Field cannot be empty");
        }
        if (TextUtils.isEmpty(mEditStocks.getText())){
            mEditStocks.setError("Field cannot be empty");
        }
        if(!TextUtils.isEmpty(mEditPrice.getText()) && !TextUtils.isEmpty(mEditStocks.getText() )){
            Intent intFinishActivity = new Intent(this,DetailsActivity.class);
            mStock.setPurchasePrice(mEditPrice.getText().toString());
            mStock.setNumberOfStocks(Integer.parseInt(mEditStocks.getText().toString()));
            intFinishActivity.putExtra(EXTRA_STOCK,mStock);
            setResult(RESULT_OK,intFinishActivity);
            finish();
        }
    }

    private void bindToStockService(){
        Log.d(TAG, mStockServiceConnection.toString());
        bindService(new Intent(EditActivity.this, StockService.class), mStockServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindToStockService(){
        Log.d(TAG, mStockServiceConnection.toString());
        unbindService(mStockServiceConnection);
        mStockService = null;
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(onDatabaseUpdateReceiver);
    }

    private void UpdateStock(Book stock){
        this.mStock = stock;
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
    protected void onStart() {
        super.onStart();
        bindToStockService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindToStockService();
    }
}
