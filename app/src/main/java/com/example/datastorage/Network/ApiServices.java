package com.example.datastorage.Network;

import com.example.datastorage.Models.MovieModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiServices {

    @GET("movie/top_rated")
    Call<MovieModel> getTopRatedMovieList();
}
