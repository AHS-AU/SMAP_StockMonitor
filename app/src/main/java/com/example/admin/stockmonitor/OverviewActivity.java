package com.example.admin.stockmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.stockmonitor.Classes.Sector;
import com.example.admin.stockmonitor.Classes.Stock;

import static com.example.admin.stockmonitor.Utilities.SharedConstants.*;

public class OverviewActivity extends Activity {
    private static final String TAG = OverviewActivity.class.getSimpleName();
    // UI Variables

    // Variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);


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
