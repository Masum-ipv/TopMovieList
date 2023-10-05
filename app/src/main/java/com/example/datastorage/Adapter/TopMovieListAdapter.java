package com.example.datastorage.Adapter;

import static com.example.datastorage.Utils.Helper.IMAGE_PATH;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.datastorage.Models.Movie;
import com.example.datastorage.databinding.MovieSingleRowBinding;

import org.jetbrains.annotations.NotNull;

public class TopMovieListAdapter extends PagingDataAdapter<Movie, TopMovieListAdapter.MyViewHolder> {
    private RequestManager glide;
    // Define Loading ViewType
    public static final int LOADING_ITEM = 0;
    // Define Movie ViewType
    public static final int MOVIE_ITEM = 1;

    public TopMovieListAdapter(@NotNull DiffUtil.ItemCallback<Movie> diffCallBack, RequestManager glide) {
        super(diffCallBack);
        this.glide = glide;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MovieSingleRowBinding binding = MovieSingleRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Movie movie = getItem(position);
        holder.movieItemBinding.movieTitle.setText(movie.getTitle());
        holder.movieItemBinding.movieRating.setText("Rating: " + movie.getVoteAverage().toString());
        holder.movieItemBinding.movieReleaseDate.setText("Release Date: " + movie.getReleaseDate());

        String imagePath = IMAGE_PATH + movie.getPosterPath();
        glide.load(imagePath).into(holder.movieItemBinding.movieAvatar);
    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() ? MOVIE_ITEM : LOADING_ITEM;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        MovieSingleRowBinding movieItemBinding;

        public MyViewHolder(@NonNull MovieSingleRowBinding movieItemBinding) {
            super(movieItemBinding.getRoot());
            this.movieItemBinding = movieItemBinding;
        }
    }
}
