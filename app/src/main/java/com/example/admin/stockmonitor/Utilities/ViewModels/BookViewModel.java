package com.example.admin.stockmonitor.Utilities.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;

import com.example.admin.stockmonitor.Room.Book.Book;
import com.example.admin.stockmonitor.Room.Book.BookRepository;

import java.util.List;

public class BookViewModel extends AndroidViewModel {
    private BookRepository repository;
    private LiveData<List<Book>> allStocks;

    public BookViewModel(@NonNull Application application) {
        super(application);
        repository = new BookRepository(application);
        allStocks = repository.getAllStocks();
    }

    public void insert(Book book){
        repository.insert(book);
    }

    public void update(Book book){
        repository.update(book);
    }

    public void delete(Book book){
        repository.delete(book);
    }

    public LiveData<List<Book>> getAllStocks(){
        return allStocks;
    }
}
