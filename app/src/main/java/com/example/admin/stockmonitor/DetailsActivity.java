package com.example.admin.stockmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.stockmonitor.Classes.Stock;

import static com.example.admin.stockmonitor.Utilities.SharedConstants.*;

public class DetailsActivity extends Activity {
    private static final String TAG = DetailsActivity.class.getSimpleName();

    // UI Variables
    private Button mBtnBack;
    private Button mBtnEdit;
    private TextView mTxtDisplayName;
    private TextView mTxtDisplayPrice;
    private TextView mTxtDisplayStocks;
    private TextView mTxtDisplaySector;

    // Variables
    private Stock mStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        // Get Intent Information
        final Intent mIntent = getIntent();
        Bundle mStockBundle = mIntent.getExtras();
        mStock = (Stock)mStockBundle.getSerializable(EXTRA_STOCK);

        // UI References: Buttons / Switches
        mBtnBack = findViewById(R.id.btnBack);
        mBtnEdit = findViewById(R.id.btnEdit);

        // UI References: TextViews
        mTxtDisplayName = findViewById(R.id.txtDisplayName);
        mTxtDisplayPrice = findViewById(R.id.txtDisplayPrice);
        mTxtDisplayStocks = findViewById(R.id.txtDisplayStocks);
        mTxtDisplaySector = findViewById(R.id.txtDisplaySector);

        // Update UI with retrieved Intent
        mTxtDisplayName.setText(mStock.getStockName());
        mTxtDisplayPrice.setText(String.valueOf(mStock.getPurchasePrice()));
        mTxtDisplayStocks.setText(String.valueOf(mStock.getNumberOfStocks()));
        mTxtDisplaySector.setText(mStock.getSector().getValue());

        // Action Bar
        getActionBar().setTitle(R.string.DetailsActionbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        /**
         * Button: Back
         * Function: Return to Previous Activity
         */
        mBtnBack.setOnClickListener(v -> onBackPressed());

        /**
         * Button: Edit
         * Function: Start "EditActivity" with necessary stock data
         */

        mBtnEdit.setOnClickListener(v -> startEditActivity());

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