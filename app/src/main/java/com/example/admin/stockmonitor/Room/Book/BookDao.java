package com.example.admin.stockmonitor.Room.Book;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Dao interface for our Room DB Operations
 */
@Dao
public interface BookDao {

    @Insert
    void insert(Book book);

    @Update
    void update(Book book);

    @Delete
    void delete(Book book);

    @Query("SELECT * FROM stock_table WHERE symbol = (:symbol)")
    Book getStock(String symbol);

    @Query("SELECT COUNT(*) FROM stock_table")
    int getSize();

    @Query("SELECT * FROM stock_table")
    List<Book> getAllStocksOnStart();

}
