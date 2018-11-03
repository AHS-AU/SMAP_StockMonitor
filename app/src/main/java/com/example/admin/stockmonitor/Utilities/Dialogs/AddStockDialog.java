package com.example.admin.stockmonitor.Utilities.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.admin.stockmonitor.OverviewActivity;
import com.example.admin.stockmonitor.R;

public class AddStockDialog extends AppCompatDialogFragment {
    private static final String TAG = AddStockDialog.class.getSimpleName();
    // UI Variables
    private EditText mEditSymbol;
    private EditText mEditPurchasePrice;
    private EditText mEditNumOfStocks;
    private AddStockDialogListener mListener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder mADBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater mInflater = getActivity().getLayoutInflater();
        View mView = mInflater.inflate(R.layout.popup_addstock_layout, null);

        mEditSymbol = mView.findViewById(R.id.editAddSymbol);
        mEditPurchasePrice = mView.findViewById(R.id.editAddPurchasePrice);
        mEditNumOfStocks = mView.findViewById(R.id.editAddNumStocks);

        mADBuilder.setView(mView)
                .setTitle(R.string.addTitle)
                .setNegativeButton(R.string.btnCancel, (DialogInterface dialogInterface, int i) -> negativeButton())
                .setPositiveButton(R.string.addPositive, (DialogInterface dialogInterface, int i) -> positiveButton());
        return mADBuilder.create();
    }

    public void negativeButton(){}

    public void positiveButton(){
        dismiss();

        if(!TextUtils.isEmpty(mEditSymbol.getText()) &&
                !TextUtils.isEmpty(mEditPurchasePrice.getText()) &&
                !TextUtils.isEmpty(mEditNumOfStocks.getText())) {
            String mSymbol = mEditSymbol.getText().toString();
            String mPurchasePrice = mEditPurchasePrice.getText().toString();
            int mNumberOfStocks = Integer.parseInt(mEditNumOfStocks.getText().toString());
            mListener.getStockInfo(mSymbol, mPurchasePrice, mNumberOfStocks);
        }else{
            mListener.getStockInfo("ERROR","ERROR",0);
        }


    }

    public interface AddStockDialogListener{
        void getStockInfo(String symbol, String purchasePrice, int numberOfStocks);
    }

    /**********************************************************************************************
     *                                   Override Functions                                       *
     *********************************************************************************************/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (AddStockDialogListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "AddStockDialog is not implemented");
        }
    }
}
