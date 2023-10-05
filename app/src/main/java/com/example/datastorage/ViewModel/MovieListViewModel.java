package com.example.datastorage.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.example.datastorage.Models.Movie;
import com.example.datastorage.Paging.MoviePagingSource;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class MovieListViewModel extends ViewModel {

    public Flowable<PagingData<Movie>> moviePagingDataFlowable;
//    private MovieRepository movieRepository;

    public MovieListViewModel() {
        Log.d("TAGY", "MovieListViewModel init called");
        init();
    }

    private void init() {
        MoviePagingSource pagingSource = new MoviePagingSource();

        //Create new Pager
        Pager<Integer, Movie> pager = new Pager(
                // Create new paging config
                new PagingConfig(20, //  Count of items in one page
                        20, //  Number of items to prefetch
                        false, // Enable placeholders for data which is not yet loaded
                        20, // initialLoadSize - Count of items to be loaded initially
                        20 * 499),// maxSize - Count of total items to be shown in recyclerview
                () -> pagingSource); // set paging source

        //Init Flowable
        moviePagingDataFlowable = PagingRx.getFlowable(pager);
        CoroutineScope coroutineScope = ViewModelKt.getViewModelScope(this);
        PagingRx.cachedIn(moviePagingDataFlowable, coroutineScope);
    }

//    public MovieListViewModel(@NonNull Application application) {
//        //      super(application);
//        movieRepository = new MovieRepository(application);
//    }
//
//    public MutableLiveData<List<Movie>> getTopRatedMovieListDB() {
//        return movieRepository.getTopRatedMovieListDB();
//    }
//
//    public void insertTopRatedMovieListDB(List<Movie> movies) {
//        movieRepository.insertTopRatedMovieListDB(movies);
//    }
}
