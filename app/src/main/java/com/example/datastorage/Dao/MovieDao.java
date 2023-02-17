package com.example.datastorage.Dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.datastorage.Models.Result;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert
    void insert(List<Result> results);

    @Query("SELECT * FROM movie_table")
    List<Result> getAllMovies();
}
