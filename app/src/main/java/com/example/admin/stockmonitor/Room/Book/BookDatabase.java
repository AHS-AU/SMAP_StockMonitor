package com.example.admin.stockmonitor.Room.Book;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Book.class}, version = 1)
public abstract class BookDatabase extends RoomDatabase {
    public abstract BookDao bookDao();

    private static BookDatabase instance;

    /**
     * Method that creates an instance of Database if it's not already in use.
     * If the Database is in use, it will return the Existing Instance of the Database.
     * @param context
     * @return : If(Instance == null): return new Database Instance
     *           Else: return Existing Database Instance
     */
    public static synchronized BookDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    BookDatabase.class, "book_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private BookDao bookDao;

        private PopulateDbAsyncTask(BookDatabase db){
            bookDao = db.bookDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            bookDao.insert(new Book("Company 1", "Symbol 1", "PE 1", "LP 1", "LU 1"));
            bookDao.insert(new Book("Company 2", "Symbol 2", "PE 2", "LP 2", "LU 2"));
            bookDao.insert(new Book("Company 3", "Symbol 3", "PE 3", "LP 3", "LU 3"));
            return null;
        }
    }
}
