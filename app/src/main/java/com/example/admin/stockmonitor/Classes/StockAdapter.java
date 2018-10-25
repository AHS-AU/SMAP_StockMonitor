package com.example.admin.stockmonitor.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.stockmonitor.R;

import java.util.List;

public class StockAdapter extends BaseAdapter {
    private List<Stock> mStockList;

    Stock mStock;
    private Context mContext;

    public StockAdapter(Context context, List<Stock> list){
        this.mContext = context;
        this.mStockList = list;
    }

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            LayoutInflater mInflater;
            mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.list_of_stocks, null);
        }

        if (mStockList != null && mStockList.size() > position){
            mStock = mStockList.get(position);

            TextView txtStockName = convertView.findViewById(R.id.txtStockName);
            txtStockName.setText(mStock.getStockName().toString());

            TextView txtPurchasePrice = convertView.findViewById(R.id.txtPurchasePrice);
            txtPurchasePrice.setText(String.format("%f", mStock.getPurchasePrice()));

            TextView txtDifferencePrice = convertView.findViewById(R.id.txtDifferencePrice);
            txtDifferencePrice.setText(mStock.getStockName().toString());

            return convertView;
        }

        return null;
    }
}
