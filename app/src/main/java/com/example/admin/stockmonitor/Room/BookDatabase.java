package com.example.admin.stockmonitor.Room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

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
     *
     */
    public static synchronized BookDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    BookDatabase.class, "book_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
