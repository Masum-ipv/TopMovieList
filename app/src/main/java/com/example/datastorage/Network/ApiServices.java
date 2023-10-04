package com.example.datastorage.Network;

import com.example.datastorage.Models.MovieModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {

    @GET("movie/top_rated")
    Call<MovieModel> getTopRatedMovieList();
    @GET("search/movie")
    Call<MovieModel> searchMovieApi(@Query("query") String query, @Query("page") int pageNumber);

}
