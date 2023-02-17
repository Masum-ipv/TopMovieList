package com.example.datastorage.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datastorage.Models.Result;
import com.example.datastorage.R;
import com.example.datastorage.Utils.Helper;

import java.util.List;

public class TopMovieListAdapter extends RecyclerView.Adapter<TopMovieListAdapter.MyViewHolder> {
    private Context context;
    private List<Result> mList;

    public TopMovieListAdapter(Context context, List<Result> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_single_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mTitle.setText(mList.get(position).getTitle());
        holder.mRating.setText("Rating: " + mList.get(position).getVoteAverage().toString());
        holder.mReleaseDate.setText("Release Date: " + mList.get(position).getReleaseDate());

        String imageBaseUrl = Helper.getConfigValue(context, "IMAGE_PATH");
        String imagePath = imageBaseUrl + mList.get(position).getPosterPath();
        Glide.with(context).load(imagePath).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        if (this.mList != null) {
            return mList.size();
        } else {
            return 0;
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mTitle, mRating, mReleaseDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.movie_avatar);
            mTitle = itemView.findViewById(R.id.movie_title);
            mRating = itemView.findViewById(R.id.movie_rating);
            mReleaseDate = itemView.findViewById(R.id.movie_release_date);
        }
    }
}
