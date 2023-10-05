package com.example.datastorage.Network;

import com.example.datastorage.Models.MovieResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {

    @GET("movie/top_rated")
    Single<MovieResponse> getTopRatedMovieList(@Query("page") int page);

    @GET("search/movie")
    Call<MovieResponse> searchMovieApi(@Query("query") String query, @Query("page") int pageNumber);

}
