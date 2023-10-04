package com.example.datastorage.Activities;

import static com.example.datastorage.Utils.Helper.SHARED_PREF_KEY;
import static com.example.datastorage.Utils.Helper.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.datastorage.Adapter.TopMovieListAdapter;
import com.example.datastorage.Models.Result;
import com.example.datastorage.R;
import com.example.datastorage.Utils.SharedPreference;
import com.example.datastorage.ViewModel.MovieListViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DashboardActivity extends AppCompatActivity {

    //    String roomDbName = "userRoomDb", userInput = "";
    SharedPreference sharedPreference = new SharedPreference(this);
    private RecyclerView recyclerView;
    private MovieListViewModel movieListViewModel;
    private TopMovieListAdapter topMovieListAdapter;
    private ProgressBar progressBar;

    @Inject
    RequestManager requestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        recyclerView = findViewById(R.id.recycleView);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);
        movieListViewModel.getTopRatedMovieList().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {
                topMovieListAdapter = new TopMovieListAdapter(DashboardActivity.this, results, requestManager);
                recyclerView.setAdapter(topMovieListAdapter);
                progressBar.setVisibility(View.GONE);
            }
        });

        //TODO: Need to handle 2 API call seperatly, insert new value in mutable data, handle the last page
        //RecyclerView Pagination
        //Loading next page of pagination
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                if(!recyclerView.canScrollVertically(1)){
//                    //Display next page result
//                    movieListViewModel.searchNextPage()
//                            .observe(DashboardActivity.this, new Observer<List<Result>>() {
//                                @Override
//                                public void onChanged(List<Result> results) {
//                                    topMovieListAdapter = new TopMovieListAdapter(DashboardActivity.this, results);
//                                    recyclerView.setAdapter(topMovieListAdapter);
//                                    progressBar.setVisibility(View.GONE);
//                                }
//                            });
//
//                }
//            }
//        });

/*        showData(roomDbText);
        roomDbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInput = roomDbText.getText().toString();
                new roomThread().start();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.myProfileBtn) {
            startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
            return false;
        } else if (item.getItemId() == R.id.search) {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    progressBar.setVisibility(View.VISIBLE);
                    movieListViewModel.searchMovieApi(query, 1)
                            .observe(DashboardActivity.this, new Observer<List<Result>>() {
                                @Override
                                public void onChanged(List<Result> results) {
//                                    topMovieListAdapter = new TopMovieListAdapter(DashboardActivity.this, results);
//                                    recyclerView.setAdapter(topMovieListAdapter);
//                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Date date = new Date();
        String result = new SimpleDateFormat("yyyy-MM-dd").format(date);
        Log.d(TAG, "onPause: " + result);
        sharedPreference.SaveSharedPreference(SHARED_PREF_KEY, String.valueOf(result));
    }

/*    private void showData(EditText editText) {
        AppDatabase database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, roomDbName).allowMainThreadQueries().build();
        UserDao userDao = database.userDao();

        int size = userDao.getUserData().size();
        if (size > 0) {
            String text = userDao.getUserData().get(size - 1).getUserInput();
            editText.setText(text);
        }
    }

    class roomThread extends Thread {
        @Override
        public void run() {
            super.run();
            AppDatabase database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, roomDbName).build();
            UserDao userDao = database.userDao();
            userDao.insertAll(new UserData(userInput));
        }
    }*/
}