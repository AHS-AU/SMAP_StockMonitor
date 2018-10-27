package com.example.admin.stockmonitor.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Dao interface for our Room DB Operations
 */
@Dao
public interface BookDao {

    @Query("SELECT * FROM stock_table WHERE symbol = (:symbol)")
    Book getStock(String symbol);

    @Query("SELECT * FROM stock_table")
    List<Book> getAllStocks();

    // TODO : Consider LiveData 5:00 part 3
}
