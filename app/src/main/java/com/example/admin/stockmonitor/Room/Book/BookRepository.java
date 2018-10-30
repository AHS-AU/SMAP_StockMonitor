package com.example.admin.stockmonitor.Room.Book;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

/**
 * Repository is not a part of the Architecture Components,
 * it's a middle-ground between the ViewModel & Room DAO.
 */
public class BookRepository {
    private BookDao bookDao;
    private LiveData<List<Book>> allBooks;

    public BookRepository(Application application){
        BookDatabase db = BookDatabase.getInstance(application);
        bookDao = db.bookDao();
        allBooks = bookDao.getAllStocks();
    }

    public void insert(Book book){
        new InsertBookAsynctask(bookDao).execute(book);
    }

    public void update(Book book){
        new UpdateBookAsynctask(bookDao).execute(book);
    }

    public void delete(Book book){
        new DeleteBookAsynctask(bookDao).execute(book);
    }

//    public void getStock(String symbol){
//
//    }

    public LiveData<List<Book>> getAllStocks(){
        return allBooks;
    }

    // Asynctask
    private static class InsertBookAsynctask extends AsyncTask<Book, Void, Void>{
        private BookDao bookDao;

        private InsertBookAsynctask(BookDao bookDao){
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            bookDao.insert(books[0]);
            return null;
        }
    }

    private static class UpdateBookAsynctask extends AsyncTask<Book, Void, Void>{
        private BookDao bookDao;

        private UpdateBookAsynctask(BookDao bookDao){
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            bookDao.update(books[0]);
            return null;
        }
    }

    private static class DeleteBookAsynctask extends AsyncTask<Book, Void, Void>{
        private BookDao bookDao;

        private DeleteBookAsynctask(BookDao bookDao){
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            bookDao.delete(books[0]);
            return null;
        }
    }

}
