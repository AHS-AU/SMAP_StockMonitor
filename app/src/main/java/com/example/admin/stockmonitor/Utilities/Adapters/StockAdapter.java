package com.example.admin.stockmonitor.Utilities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.stockmonitor.Classes.Stock;
import com.example.admin.stockmonitor.R;

import java.util.List;

/**
 * StockAdapter is a class that sets up a Custom Adapter
 * to work with layout given in list_of_stocks.xml
 */
public class StockAdapter extends BaseAdapter {

    private List<Stock> mStockList;
    private Context mContext;

    /**
     * Default Constructor
     * @param context: Context
     * @param list: List of Stock Objects from /Classes/Stock.java
     */
    public StockAdapter(Context context, List<Stock> list){
        this.mContext = context;
        this.mStockList = list;
    }

    /**********************************************************************************************
     *                                   Override Functions                                       *
     *********************************************************************************************/
    @Override
    public int getCount(){
        if (mStockList == null){
            return 0;
        }
        return mStockList.size();
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public Object getItem(int position){
        if (mStockList != null && mStockList.size() > position){
            return mStockList.get(position);
        }
        return null;
    }

    /**
     * Method sets up the View
     * @param position Position of Item
     * @param convertView ignore
     * @param parent ignore
     * @return Returns view to the layout
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            LayoutInflater mInflater;
            mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.list_of_stocks, null);
        }

        if (mStockList != null && mStockList.size() > position){
            Stock mStock = mStockList.get(position);

            TextView txtStockName = convertView.findViewById(R.id.txtStockName);
            txtStockName.setText(mStock.getStockName());

            TextView txtPurchasePrice = convertView.findViewById(R.id.txtPurchasePrice);
            txtPurchasePrice.setText(String.format(java.util.Locale.US, "%.2f", mStock.getPurchasePrice()));

            TextView txtDifferencePrice = convertView.findViewById(R.id.txtDifferencePrice);
            txtDifferencePrice.setText(mStock.getStockName());

            return convertView;
        }

        return null;
    }
}
