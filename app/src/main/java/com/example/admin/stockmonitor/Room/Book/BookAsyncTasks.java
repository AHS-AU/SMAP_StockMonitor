package com.example.admin.stockmonitor.Room.Book;


import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.admin.stockmonitor.Utilities.SharedConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class BookAsyncTasks {
    private static final String TAG = "BookAsyncTasks";
    public BookAsyncTasks(){

    }

    /**
     * Below you will find the Public Call Methods to AsyncTasks
     */
    public void InsertBook(BookDao bookDao, Book book){
        new InsertBookAsyncTask(bookDao).execute(book);
    }

    public void UpdateBook(BookDao bookDao, Book book){
        new UpdateBookAsyncTask(bookDao).execute(book);
    }

    public void DeleteBook(BookDao bookDao, Book book){
        new DeleteBookAsyncTask(bookDao).execute(book);
    }

    public void UpdateAllBooks(BookDao bookDao, RequestQueue queue){
        new UpdateAllBooksAsyncTask(bookDao, queue).execute();
    }

    public void InitDatabase(Context context){
        new InitDatabaseAsyncTask(context).execute();
    }

    public List<Book> GetAllBooks(Context context){
        try {
            return ( new GetAllBooksAsyncTask(context).execute().get() );
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * Below you will find the AsyncTasks
     */
    private static class GetAllBooksAsyncTask extends AsyncTask<Void, Void, List<Book>>{
        private BookDatabase bookDatabase;
        private BookDao bookDao;

        GetAllBooksAsyncTask(Context context){
            bookDatabase = BookDatabase.getInstance(context);
            bookDao = bookDatabase.bookDao();
        }

        @Override
        protected List<Book> doInBackground(Void... voids) {
            return (bookDao.getAllStocksOnStart());
        }
    }

    private static class InitDatabaseAsyncTask extends AsyncTask<Void,Void,Void>{
        private BookDatabase bookDatabase;
        private BookDao bookDao;
        InitDatabaseAsyncTask(Context context){
            bookDatabase = BookDatabase.getInstance(context);
            bookDao = bookDatabase.bookDao();

        }
        @Override
        protected Void doInBackground(Void... voids) {
            bookDao.getAllStocksOnStart();
            return null;
        }
    }

    private static class InsertBookAsyncTask extends AsyncTask<Book, Void, Void> {
        private BookDao bookDao;

        InsertBookAsyncTask(BookDao vBookDao) { bookDao = vBookDao;}

        @Override
        protected Void doInBackground(Book... books) {
            bookDao.insert(books[0]);
            return null;
        }
    }

    private static class UpdateBookAsyncTask extends AsyncTask<Book, Void, Void> {
        private BookDao bookDao;

        UpdateBookAsyncTask(BookDao vBookDao) { bookDao = vBookDao;}

        @Override
        protected Void doInBackground(Book... books) {
            bookDao.update(books[0]);
            return null;
        }
    }

    private static class DeleteBookAsyncTask extends AsyncTask<Book, Void, Void> {
        private BookDao bookDao;

        DeleteBookAsyncTask(BookDao vBookDao) { bookDao = vBookDao;}

        @Override
        protected Void doInBackground(Book... books) {
            bookDao.delete(books[0]);
            return null;
        }
    }

    private static class UpdateAllBooksAsyncTask extends AsyncTask<Void, Void, Void>{
        private BookDao mBookDao;
        private RequestQueue mQueue;

        UpdateAllBooksAsyncTask(BookDao bookDao, RequestQueue queue){
            this.mBookDao = bookDao;
            this.mQueue = queue;

        }

        @Override
        protected Void doInBackground(Void... voids) {
            for(int i = 0; i < mBookDao.getSize(); i++){
                Book mBook = mBookDao.getAllStocksOnStart().get(i);
                String symbol = mBook.getSymbol();

                mBookDao.getStock(symbol);
                SharedConstants sc = new SharedConstants();
                String url = sc.getApiUrl(symbol);

                JsonObjectRequest mRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject("quote");
                                    String latestPrice = jsonObject.getString("latestPrice");
                                    String latestUpdate = jsonObject.getString("latestUpdate");
                                    String change = jsonObject.getString("change");
                                    String sector = jsonObject.getString("sector");
                                    mBook.setLatestPrice(latestPrice);
                                    mBook.setLatestUpdate(latestUpdate);
                                    mBook.setChange(change);
                                    mBook.setSector(sector);
                                    BookAsyncTasks mBookAsyncTask = new BookAsyncTasks();
                                    mBookAsyncTask.UpdateBook(mBookDao, mBook);
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

            return null;
        }
    }
}