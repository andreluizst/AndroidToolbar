package br.com.alsoftware.androidtoolbar.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by AndreLuiz on 20/07/2015.
 */
public class Conexao {
    public static boolean estaAtiva(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        return false;
    }

}
