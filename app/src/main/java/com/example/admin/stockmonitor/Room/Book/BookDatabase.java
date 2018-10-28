package com.example.admin.stockmonitor.Room.Book;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.stockmonitor.Utilities.SharedConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.admin.stockmonitor.Utilities.SharedConstants.bookViewModel;

@Database(entities = {Book.class}, version = 1)
public abstract class BookDatabase extends RoomDatabase {
    public abstract BookDao bookDao();

    private static BookDatabase instance;
    private static Context mContext;

    /**
     * Method that creates an instance of Database if it's not already in use.
     * If the Database is in use, it will return the Existing Instance of the Database.
     * @param context
     * @return : If(Instance == null): return new Database Instance
     *           Else: return Existing Database Instance
     */
    public static synchronized BookDatabase getInstance(Context context){
        mContext = context;
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    BookDatabase.class, "book_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    // Callback called onCreate to fill up start Stocks
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    // This class populates the Db with an AsyncTask & uses method addToDb
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private BookDao bookDao;

        private PopulateDbAsyncTask(BookDatabase db){
            bookDao = db.bookDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            addToDb(bookDao);
            return null;
        }

        public static void addToDb(BookDao vBookDao){
            RequestQueue mQueue = null;
            if (mQueue == null) {
                mQueue = Volley.newRequestQueue(mContext);
            }
            SharedConstants sc = new SharedConstants();
            ArrayList<String> urlArray = new ArrayList();
            urlArray.add(sc.getApiUrl("ATVI"));
            urlArray.add(sc.getApiUrl("GOOGL"));
            urlArray.add(sc.getApiUrl("AAPL"));
            urlArray.add(sc.getApiUrl("AMZN"));
            urlArray.add(sc.getApiUrl("CERN"));
            urlArray.add(sc.getApiUrl("NFLX"));
            urlArray.add(sc.getApiUrl("FB"));
            urlArray.add(sc.getApiUrl("EA"));
            urlArray.add(sc.getApiUrl("TSLA"));
            urlArray.add(sc.getApiUrl("EBAY"));

            for (int i = 0; i < urlArray.size(); i++){
                JsonObjectRequest mRequest = new JsonObjectRequest(Request.Method.GET, urlArray.get(i), null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject("quote");
                                    String companyName = jsonObject.getString("companyName");
                                    String symbol = jsonObject.getString("symbol");
                                    String primaryExchange = jsonObject.getString("primaryExchange");
                                    String latestPrice = jsonObject.getString("latestPrice");
                                    String latestUpdate = jsonObject.getString("latestUpdate");
                                    Book mBook = new Book(companyName,symbol,primaryExchange,latestPrice,latestUpdate);
                                    bookViewModel.insert(mBook);
                                    //vBookDao.insert(mBook);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                mQueue.add(mRequest);
            }
        }
    }


}
