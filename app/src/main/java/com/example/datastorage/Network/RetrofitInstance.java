package com.example.datastorage.Network;

import static com.example.datastorage.Utils.Helper.API_KEY;
import static com.example.datastorage.Utils.Helper.BASE_URL;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;

    public static ApiServices getRetrofitInstance() {
        if (retrofit == null) {
            OkHttpClient.Builder client = new OkHttpClient.Builder();

            client.addInterceptor(chain -> {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();
                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key",API_KEY)
                        .build();

                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            });

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client.build())
                    .build();
        }
        return retrofit.create(ApiServices.class);
    }
}
