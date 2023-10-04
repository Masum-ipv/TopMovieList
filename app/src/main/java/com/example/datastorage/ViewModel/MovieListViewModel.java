package com.example.datastorage.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
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

    public MutableLiveData<List<Result>> searchMovieApi(String query, int pageNumber) {
        return movieRepository.searchMovieApi(query, pageNumber);
    }

    public MutableLiveData<List<Result>> searchNextPage() {
        return movieRepository.searchNextPage();
    }
}
