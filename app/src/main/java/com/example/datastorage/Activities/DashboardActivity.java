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

import com.bumptech.glide.RequestManager;
import com.example.datastorage.Adapter.TopMovieListAdapter;
import com.example.datastorage.Models.Movie;
import com.example.datastorage.R;
import com.example.datastorage.Utils.MovieComparator;
import com.example.datastorage.Utils.SharedPreference;
import com.example.datastorage.ViewModel.MovieListViewModel;
import com.example.datastorage.databinding.ActivityDashboardBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DashboardActivity extends AppCompatActivity {

    String roomDbName = "userRoomDb", userInput = "";
    SharedPreference sharedPreference = new SharedPreference(this);
    private ActivityDashboardBinding binding;
    private MovieListViewModel mainActivityViewModel;
    private TopMovieListAdapter moviesAdapter;

    @Inject
    RequestManager requestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        moviesAdapter = new TopMovieListAdapter(new MovieComparator(), requestManager);

        mainActivityViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        initRecyclerviewAndAdapter();

        // Subscribe to to paging data
        mainActivityViewModel.moviePagingDataFlowable.subscribe(moviePagingData -> {
            // submit new data to recyclerview adapter
            moviesAdapter.submitData(getLifecycle(), moviePagingData);
        });

/*        showData(roomDbText);
        roomDbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInput = roomDbText.getText().toString();
                new roomThread().start();
            }
        });*/
    }

    private void initRecyclerviewAndAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.recycleView.setLayoutManager(gridLayoutManager);
        binding.recycleView.setAdapter(moviesAdapter);
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
//                    mainActivityViewModel.searchMovieApi(query, 1)
//                            .observe(DashboardActivity.this, new Observer<List<Movie>>() {
//                                @Override
//                                public void onChanged(List<Movie> movies) {
////                                    topMovieListAdapter = new TopMovieListAdapter(DashboardActivity.this, results);
////                                    recyclerView.setAdapter(topMovieListAdapter);
////                                    progressBar.setVisibility(View.GONE);
//                                }
//                            });
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

    //TODO: Use dependency injection for Room Database
/*
   private void showData(EditText editText) {
        AppDatabase database = Room.databaseBuilder(
                getApplicationContext(), AppDatabase.class, roomDbName)
                .allowMainThreadQueries().build();
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