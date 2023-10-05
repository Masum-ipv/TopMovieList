package com.example.datastorage.Paging;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.example.datastorage.Models.Movie;
import com.example.datastorage.Models.MovieResponse;
import com.example.datastorage.Network.RetrofitInstance;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MoviePagingSource extends RxPagingSource<Integer, Movie> {

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Movie> pagingState) {
        return null;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, Movie>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        try {
            //If there is no page number, default page number 1
            int page = loadParams.getKey() != null ? loadParams.getKey() : 1;
            //Call the server with page number
            return RetrofitInstance.getRetrofitInstance()
                    .getTopRatedMovieList(page)
                    //Subscribe the result
                    .subscribeOn(Schedulers.io())
                    // Map result top List of movies
                    .map(MovieResponse::getResults)
                    // Map result to LoadResult Object
                    .map(movies -> toLoadResult(movies, page))
                    // when error is there return error
                    .onErrorReturn(LoadResult.Error::new);

        } catch (Exception e) {
            // Request ran into error return error
            return Single.just(new LoadResult.Error(e));
        }
    }

    // Method to map Movies to LoadResult object
    private LoadResult<Integer, Movie> toLoadResult(List<Movie> movies, int page) {
        Log.d("TAGY", "Size: " + movies.size() + " Page: " + page);
        return new LoadResult.Page(movies, page == 1 ? null : page - 1, page + 1);
    }
}
