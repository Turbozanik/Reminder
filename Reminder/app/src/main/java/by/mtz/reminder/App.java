package by.mtz.reminder;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.orm.SugarContext;

/**
 * Created by Roma on 04.10.2016.
 */

public class App extends Application {

    public static boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        isRunning = false;
    }
}
