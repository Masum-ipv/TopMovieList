package com.example.datastorage.Repository;

import android.content.Context;
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
    private static Context mContext;
    private static MovieRepository movieRepositoryInstance;
    private static MovieDao movieDao;
    private MovieModel movieModel;
    private List<Result> mResult;
    private MutableLiveData mutableLiveData;

    public static MovieRepository getMovieRepositoryInstance(Context context) {
        if (movieRepositoryInstance == null) {
            MovieDatabase db = MovieDatabase.getDatabase(context);
            movieDao = db.movieDao();
            mContext = context;
            movieRepositoryInstance = new MovieRepository();
        }
        return movieRepositoryInstance;
    }

    public MutableLiveData<List<Result>> getTopRatedMovieList() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData();
        }


        ApiServices apiServices = RetrofitInstance.getRetrofitInstance().create(ApiServices.class);
        Call<MovieModel> call = apiServices.getTopRatedMovieList();
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                movieModel = response.body();
                mResult = movieModel.getResults();
                mutableLiveData.postValue(mResult);
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
