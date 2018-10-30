package com.example.admin.stockmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.admin.stockmonitor.Classes.Sector;
import com.example.admin.stockmonitor.Classes.Stock;

import static com.example.admin.stockmonitor.Utilities.SharedConstants.*;

public class EditActivity extends AppCompatActivity {
    private static final String TAG = EditActivity.class.getSimpleName();
    // UI Variables
    private Button mBtnCancel;
    private Button mBtnSave;
    private EditText mEditName;
    private EditText mEditPrice;
    private EditText mEditStocks;
    private RadioGroup mRgSector;
    private RadioButton mRbTechnology;
    private RadioButton mRbHealthcare;
    private RadioButton mRbBasicMats;

    // Variables
    private Stock mStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        // Get Intent Information
        final Intent mIntent = getIntent();
        Bundle mStockBundle = mIntent.getExtras();
        mStock = (Stock)mStockBundle.getSerializable(EXTRA_STOCK);

        // UI References: Buttons / Switches
        mBtnCancel = findViewById(R.id.btnCancel);
        mBtnSave = findViewById(R.id.btnSave);
        mEditName = findViewById(R.id.editName);
        mEditPrice = findViewById(R.id.editPrice);
        mEditStocks = findViewById(R.id.editStocks);
        mRgSector = findViewById(R.id.rgSector);
        mRbTechnology = findViewById(R.id.radioTechnology);
        mRbHealthcare = findViewById(R.id.radioHealthcare);
        mRbBasicMats = findViewById(R.id.radioBasicMats);

        // Update UI with retrieved Intent
        mEditName.setText(mStock.getStockName());
        mEditPrice.setText(String.valueOf(mStock.getPurchasePrice()));
        mEditStocks.setText(String.valueOf(mStock.getNumberOfStocks()));
        setDisplaySector(mStock.getSector());

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

        /**
         * RadioGroup Iteration
         * Checks which radiobutton is active in the Radiogroup
         */
        mRgSector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            public void onCheckedChanged(RadioGroup group, int checkedId){
                switch(checkedId){
                    case R.id.radioTechnology:
                        mStock.setSector(Sector.TECHNOLOGY);
                        break;
                    case R.id.radioHealthcare:
                        mStock.setSector(Sector.HEALTHCARE);
                        break;
                    case R.id.radioBasicMats:
                        mStock.setSector(Sector.BASIC_MATERIALS);
                        break;
                    default:
                        Log.e(TAG, "No Valid Sector, Program may be unstable!");
                        break;
                }
            }
        });

    }

    /**
     * Retrieves enter information and saves it.
     * Returns to DetailActivity with updated information filled by the user.
     */
    private void saveButtonFunction(){
        // First Error handling to ensure all fields have been filled.
        if (mEditName.getText().toString().equals("")){
            mEditName.setError("Field cannot be empty");
        }
        if (mEditPrice.getText().toString().equals("")){
            mEditPrice.setError("Field cannot be empty");
        }
        if (mEditStocks.getText().toString().equals("")){
            mEditStocks.setError("Field cannot be empty");
        }
        if (!mEditName.getText().toString().equals("") &&
                !mEditPrice.getText().toString().equals("") &&
                !mEditStocks.getText().toString().equals("")){
            Intent intFinishActivity = new Intent(this,DetailsActivity.class);
            mStock.setStockName(mEditName.getText().toString());
            mStock.setPurchasePrice(Double.parseDouble(mEditPrice.getText().toString()));
            mStock.setNumberOfStocks(Integer.parseInt(mEditStocks.getText().toString()));
            intFinishActivity.putExtra(EXTRA_STOCK,mStock);
            setResult(RESULT_OK,intFinishActivity);
            finish();
        }
    }

    /**
     * Function: Check one of the Radio Buttons
     * @param sector: mStock.getSector()
     */
    private void setDisplaySector(Sector sector){
        switch(sector){
            case TECHNOLOGY:
                mRbTechnology.setChecked(true);
                break;
            case HEALTHCARE:
                mRbHealthcare.setChecked(true);
                break;
            case BASIC_MATERIALS:
                mRbBasicMats.setChecked(true);
                break;
            default:
                Log.e(TAG, "Sector Image Not Found!");
                break;
        }
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
