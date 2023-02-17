package com.example.datastorage.Network;

import com.example.datastorage.Models.MovieModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiServices {

    @GET("3/movie/top_rated?api_key=3060508c8943af543224a8152841f34a")
    Call<MovieModel> getTopRatedMovieList();
}
