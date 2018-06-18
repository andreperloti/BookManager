package br.com.developers.perloti.bookmanager;

import android.app.Application;

import br.com.developers.perloti.bookmanager.util.ApplicationUtil;
import io.realm.Realm;

/**
 * Created by perloti on 17/06/18.
 */

public class ApplicationManager extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationUtil.context = getApplicationContext();

        Realm.init(getApplicationContext());

    }
}
