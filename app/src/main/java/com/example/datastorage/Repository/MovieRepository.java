package com.example.datastorage.Repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.MutableLiveData;

import com.example.datastorage.Dao.MovieDao;
import com.example.datastorage.Models.MovieModel;
import com.example.datastorage.Models.Result;
import com.example.datastorage.Network.ApiServices;
import com.example.datastorage.Network.RetrofitInstance;
import com.example.datastorage.Utils.MovieDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private Application application;
    private static MovieDao movieDao;
    private List<Result> mResult;
    private MutableLiveData<List<Result>> mutableLiveData = new MutableLiveData<>();

    public MovieRepository(Application application) {
        this.application = application;
        MovieDatabase db = MovieDatabase.getDatabase(application.getApplicationContext());
        movieDao = db.movieDao();
    }

    public MutableLiveData<List<Result>> getTopRatedMovieList() {
        ApiServices apiServices = RetrofitInstance.getRetrofitInstance();
        Call<MovieModel> call = apiServices.getTopRatedMovieList();
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                MovieModel movieModel = response.body();
                if (movieModel != null && movieModel.getResults() != null) {
                    mResult = movieModel.getResults();
                    mutableLiveData.postValue(mResult);
                }
//                new insertAsyncTask(movieDao).execute(mResult);
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
//                Log.d("MovieRepository", "Calling Room-database");
//                new getAsyncTask(movieDao).execute();//On failure calling local room-database
            }
        });

        return mutableLiveData;
    }

    public void insertTopRatedMovieListDB(List<Result> results) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //On Background
                movieDao.insert(results);

                //On Post Execution
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
    }

    public MutableLiveData<List<Result>> getTopRatedMovieListDB() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //On Background
                mResult = movieDao.getAllMovies();
                mutableLiveData.postValue(mResult);

                //On Post Execution
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });
        return mutableLiveData;
    }
}
