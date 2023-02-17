package com.example.datastorage.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datastorage.Adapter.TopMovieListAdapter;
import com.example.datastorage.Models.Result;
import com.example.datastorage.R;
import com.example.datastorage.Utils.SharedPreference;
import com.example.datastorage.ViewModel.MovieListViewModel;

import java.text.SimpleDateFormat;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    String roomDbName = "userRoomDb", userInput = "";
    SharedPreference sharedPreference = new SharedPreference(this);
    String sharedPrefKey = "loginTime";
    private RecyclerView recyclerView;
    private MovieListViewModel movieListViewModel;
    private TopMovieListAdapter topMovieListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        recyclerView = findViewById(R.id.recycleView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);

        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);
        movieListViewModel.getTopRatedMovieList().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> results) {
                topMovieListAdapter = new TopMovieListAdapter(DashboardActivity.this, results);
                recyclerView.setAdapter(topMovieListAdapter);
            }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_profile, menu);
        MenuItem menuItem = menu.findItem(R.id.myProfileBtn);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new java.util.Date());
        sharedPreference.SaveSharedPreference(new String[]{sharedPrefKey}, new String[]{timeStamp});
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