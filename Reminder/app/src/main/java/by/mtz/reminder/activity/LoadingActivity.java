package by.mtz.reminder.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import by.mtz.reminder.DAO.DatabaseWorkService;
import by.mtz.reminder.DAO.DatabaseWorker;
import by.mtz.reminder.R;
import by.mtz.reminder.Utils.Utils;
import by.mtz.reminder.resultReciever.AppResultsReceiver;

/**
 * Created by Roma on 27.09.2016.
 */
public class LoadingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList>{
    Context context;

    boolean mIsReceiverRegistered = false;
    private AppResultsReceiver mReceiver;
    private int LOADER_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laoding_activity);

        context = this;

        Bundle bundle = new Bundle();
        bundle.putString(Utils.INTENT_SERVICE_INVOKE, Utils.LOAD_All_DATA);
        getSupportLoaderManager().initLoader(LOADER_ID, bundle, this).forceLoad();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(Utils.MANUAL_START_RECIEVER);
        sendBroadcast(broadcastIntent);
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public Loader<ArrayList> onCreateLoader(int id, Bundle args) {
        Log.e("loader created","loader created");
        return new DatabaseWorker(this,args);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList> loader, ArrayList data) {
        Log.e("loader","loader finished");
//    Intent intent = new Intent(this,MainActivity.class);
//    startActivity(intent);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList> loader) {

    }


}

