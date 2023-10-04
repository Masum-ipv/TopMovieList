package com.example.datastorage.Adapter;

import static com.example.datastorage.Utils.Helper.IMAGE_PATH;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.datastorage.Models.Result;
import com.example.datastorage.R;

import java.util.List;

public class TopMovieListAdapter extends RecyclerView.Adapter<TopMovieListAdapter.MyViewHolder> {
    private Context context;
    private List<Result> mList;
    private RequestManager glide;

    public TopMovieListAdapter(Context context, List<Result> mList, RequestManager glide) {
        this.context = context;
        this.mList = mList;
        this.glide = glide;
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

        String imagePath = IMAGE_PATH + mList.get(position).getPosterPath();
        glide.load(imagePath).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
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
