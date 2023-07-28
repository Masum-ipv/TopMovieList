package com.example.datastorage.Utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Helper {

    public static final String TAG = Helper.class.getSimpleName();
    public static String BASE_URL = "https://api.themoviedb.org/3/";
    public static String IMAGE_PATH = "https://image.tmdb.org/t/p/w500/";
    public static String API_KEY = "3060508c8943af543224a8152841f34a";
    public static String SHARED_PREF_KEY = "loginTime";

    public static boolean isNetworkAvailable(Context context) {
        // Check the status of the network connection.
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        // If the network is available, connected
        if (networkInfo != null && networkInfo.isConnectedOrConnecting())
            return true;
        else {
            Toast.makeText(context, "Network connection error!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
