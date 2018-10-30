package com.example.admin.stockmonitor.Utilities.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.stockmonitor.R;
import com.example.admin.stockmonitor.Room.Book.Book;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * StockAdapter is a class that sets up a Custom Adapter
 * to work with layout given in list_of_stocks.xml
 */
public class StockAdapter extends BaseAdapter {

    private List<Book> mBookList;
    private Context mContext;

    public StockAdapter(Context context, List<Book> books){
        this.mContext = context;
        this.mBookList = books;
    }

    /**********************************************************************************************
     *                                   Override Functions                                       *
     *********************************************************************************************/
    @Override
    public int getCount(){
        if (mBookList == null){
            return 0;
        }
        return mBookList.size();
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public Object getItem(int position){
        if (mBookList != null && mBookList.size() > position){
            return mBookList.get(position);
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

        if (mBookList != null && mBookList.size() > position){
            //Stock mStock = mStockList.get(position);
            Book mBook = mBookList.get(position);

            // Symbol
            TextView txtSymbol = convertView.findViewById(R.id.txtSymbol);
            txtSymbol.setText(mBook.getSymbol());
            // CompanyName
            TextView txtCompanyName = convertView.findViewById(R.id.txtCompanyName);
            txtCompanyName.setText(mBook.getCompanyName());

            // LatestPrice
            TextView txtLatestPrice = convertView.findViewById(R.id.txtLatestPrice);
            txtLatestPrice.setText(mBook.getLatestPrice());

            // CurrentChange, which is from API
            TextView txtCurrentChange = convertView.findViewById(R.id.txtCurrentChange);
            txtCurrentChange.setText(mBook.getChange());
            double mChange = Double.parseDouble(mBook.getChange());
            if (mChange >= 0){
                txtCurrentChange.setTextColor(mContext.getResources().getColor(R.color.change_good));
            }else{
                txtCurrentChange.setTextColor(mContext.getResources().getColor(R.color.change_bad));
            }

            // PurchasePrice
            TextView txtPurchasePrice = convertView.findViewById(R.id.txtPurchasePrice);
            txtPurchasePrice.setText(mBook.getPurchasePrice());

            // PurchasePrice vs LatestPrice difference
            TextView txtDifference = convertView.findViewById(R.id.txtDifference);
            txtDifference.setText(mBook.getPriceDifference());
            double mDifference = Double.parseDouble(mBook.getPriceDifference());
            if(mDifference >= 0){
                txtDifference.setTextColor(mContext.getResources().getColor(R.color.change_good));
            }else{
                txtDifference.setTextColor(mContext.getResources().getColor(R.color.change_bad));
            }

            // Example with latestUpdate Epoch
//            long epochValue = Long.parseLong(mBook.getLatestUpdate());
//            Date epochTime = new Date(epochValue);
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy\nHH:mm:ss", Locale.getDefault());
//            String timeFormatted = simpleDateFormat.format(epochTime);



            return convertView;
        }

        return null;
    }
}
