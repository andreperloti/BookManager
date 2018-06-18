package br.com.developers.perloti.bookmanager.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import br.com.developers.perloti.bookmanager.R;
import br.com.developers.perloti.bookmanager.util.ApplicationUtil;

/**
 * Created by perloti on 17/06/18.
 */

public class BMUtil {

    public static final java.lang.String ID_BOOK = "ID_BOOK";
    public static final String TAG_REQUEST = "BM_REQUEST";
    public static final String TAG_PERSISTENCE = "BM_PERSISTENCE";

    public static void toastShort(String msg) {
        Toast.makeText(ApplicationUtil.context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(String msg) {
        Toast.makeText(ApplicationUtil.context, msg, Toast.LENGTH_LONG).show();
    }

    public static boolean isStatusCode(int statusCode) {
        Log.d(TAG_REQUEST, "StatusCode: " + statusCode);
        if (statusCode == 200 || statusCode == 201 || statusCode == 202) {
            return true;
        } else if (statusCode == 503) {
            toastLong(ApplicationUtil.context.getString(R.string.server_currently_unavailable));
            return false;
        } else if (statusCode == 401) {
            toastLong(ApplicationUtil.context.getString(R.string.not_authorized));
            return false;
        } else {
            toastLong(ApplicationUtil.context.getString(R.string.error_try_again_later));
            return false;
        }
    }

    public static void snackbarMSG(Activity context, String msg) {
        Snackbar snackbar = Snackbar.make(context.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static boolean statusNetwork(Activity activity) {
        if (statusConnection(activity)) {
            return true;
        } else {
            String msg = activity.getString(R.string.no_internet_connection);
            snackbarMSG(activity, msg);
            return false;
        }
    }

    private static boolean statusConnection(Activity context) {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }

}
