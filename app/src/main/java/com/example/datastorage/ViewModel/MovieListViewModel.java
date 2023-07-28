package com.example.datastorage.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.datastorage.Models.Result;
import com.example.datastorage.Repository.MovieRepository;

import java.util.List;

public class MovieListViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;

    public MovieListViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
    }

    public MutableLiveData<List<Result>> getTopRatedMovieList() {
        return movieRepository.getTopRatedMovieList();
    }

    public MutableLiveData<List<Result>> getTopRatedMovieListDB() {
        return movieRepository.getTopRatedMovieListDB();
    }

    public void insertTopRatedMovieListDB(List<Result> results) {
        movieRepository.insertTopRatedMovieListDB(results);
    }
}
