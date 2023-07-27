package com.example.datastorage.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.datastorage.Dao.MovieDao;
import com.example.datastorage.Models.MovieModel;
import com.example.datastorage.Models.Result;
import com.example.datastorage.Network.ApiServices;
import com.example.datastorage.Network.RetrofitInstance;
import com.example.datastorage.Utils.MovieDatabase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private Application application;
    private static MovieRepository movieRepositoryInstance;
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
                new insertAsyncTask(movieDao).execute(mResult);
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                Log.d("MovieRepository", "Calling Room-database");
                new getAsyncTask(movieDao).execute();//On failure calling local room-database
            }
        });

        return mutableLiveData;
    }

    private class insertAsyncTask extends AsyncTask<List<Result>, Void, Void> {
        private MovieDao asyncTaskDao;

        public insertAsyncTask(MovieDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(List<Result>... movies) {
            asyncTaskDao.insert(movies[0]);
            return null;
        }
    }

    private class getAsyncTask extends AsyncTask<Void, Void, List<Result>> {
        private MovieDao asyncTaskDao;

        public getAsyncTask(MovieDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected List<Result> doInBackground(Void... voids) {
            return asyncTaskDao.getAllMovies();
        }

        @Override
        protected void onPostExecute(List<Result> results) {
            super.onPostExecute(results);
            mutableLiveData.postValue(results);
        }
    }
}
