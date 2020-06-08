package app.perfecto.com.expencemanager.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Singleton;

import app.perfecto.com.expencemanager.di.ApplicationContext;


@Singleton
public class NetworkUtils {

    private Context context;

    public NetworkUtils(@ApplicationContext Context context) {
        this.context = context;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
