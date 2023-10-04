package com.example.datastorage.Di;


import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.datastorage.R;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    //Glide library to load image from the Application
    @Provides
    @Singleton
    public RequestManager getGlide(@ApplicationContext Context context) {
        return Glide.with(context)
                .applyDefaultRequestOptions(new RequestOptions()
                        .error(R.drawable.ic_image_holder)
                        .placeholder(R.drawable.ic_image_holder));

    }
}
