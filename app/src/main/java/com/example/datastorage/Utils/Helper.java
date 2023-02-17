package com.example.datastorage.Utils;


import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.example.datastorage.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Helper {

    private static final String TAG = Helper.class.getSimpleName();

    public static String getConfigValue(Context context, String name) {
        Resources resources = context.getResources();

        try {
            InputStream rawResource = resources.openRawResource(R.raw.config);
            Properties properties = new Properties();
            properties.load(rawResource);
            return properties.getProperty(name);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Unable to find the config file: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Failed to open config file.");
        } catch (Exception e) {
            Log.e(TAG, "Failed to open config file." + e.getMessage());
        }

        return null;
    }

    public static boolean isNetworkAvailable(Context context) {
        // Check the status of the network connection.
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        // If the network is available, connected
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else {
            Toast.makeText(context, "Network connection error!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
