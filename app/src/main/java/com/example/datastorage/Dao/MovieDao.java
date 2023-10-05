package com.example.datastorage.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.datastorage.Models.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert
    void insert(List<Movie> movies);

    //@Query("SELECT * FROM Movie")
    //List<Movie> getAllMovies();
}
