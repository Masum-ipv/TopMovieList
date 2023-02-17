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
        movieRepository = MovieRepository.getMovieRepositoryInstance(application);
    }

    public MutableLiveData<List<Result>> getTopRatedMovieList() {
        return movieRepository.getTopRatedMovieList();
    }
}
