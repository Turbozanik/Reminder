package by.mtz.reminder.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import by.mtz.reminder.DAO.Action;
import by.mtz.reminder.DAO.DatabaseWorkService;
import by.mtz.reminder.DAO.DatabaseWorker;
import by.mtz.reminder.R;
import by.mtz.reminder.Utils.Utils;
import by.mtz.reminder.adapters.ActionsRVAdapter;


public class MainActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<ArrayList>{

    boolean mIsReceiverRegistered = false;
    ActionsHotBroadcastReceiver receiver;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    List<Action> actions;
    ActionsRVAdapter adapter;
    ProgressDialog progressDialog;
    private int LOADER_ID =2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initToolbarAndDrawerWithReadableName(getString(R.string.special_activities));
        recyclerView = (RecyclerView)findViewById(R.id.activitiesHotRecyclerView);

        progressDialog = new ProgressDialog(this);
        progressDialog.show();

        Intent dataIntent = new Intent(this, DatabaseWorkService.class);
        dataIntent.putExtra(Utils.INTENT_SERVICE_INVOKE, Utils.READ_ACTIONS_DATA);
        startService(dataIntent);
//        Bundle bundle = new Bundle();
//        bundle.putString(Utils.INTENT_SERVICE_INVOKE, Utils.READ_ACTIONS_DATA);
//        getSupportLoaderManager().initLoader(LOADER_ID, bundle, this).forceLoad();

        actions = new ArrayList<>();
        manager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        adapter = new ActionsRVAdapter(actions,context);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mIsReceiverRegistered) {
            unregisterReceiver(receiver);
            receiver = null;
            mIsReceiverRegistered = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mIsReceiverRegistered) {
            if (receiver == null) {
                receiver = new ActionsHotBroadcastReceiver();
            }
            IntentFilter filter = new IntentFilter();
            filter.addAction(Utils.READ_ACTIONS_DATA);
            receiver.setMainActivityHandler(this);
            registerReceiver(receiver, filter);
            mIsReceiverRegistered = true;
        }
    }
    public void initializeAdapter(List<Action> actionList){
        Log.e("init","update adapte");
        actions.clear();
        for (Action act :actionList) {
            if (!act.isRepeating()) {
                actions.add(act);
            }
        }
        Log.e("list",actions.size()+"");
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }


    @Override
    public Loader<ArrayList> onCreateLoader(int id, Bundle args) {
        Log.e("loader created","loader created");
        return new DatabaseWorker(this,args);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList> loader, ArrayList data) {
        Log.e("loader","loader finished");
        initializeAdapter(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList> loader) {

    }

}
class ActionsHotBroadcastReceiver extends BroadcastReceiver{

    MainActivity activity = null;
    ArrayList<Action> list;

    public void setMainActivityHandler(MainActivity main){
        activity = main;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("reciever","reciev");
        list = intent.getParcelableArrayListExtra(Utils.READ_ACTIONS_DATA);
        Log.e("reciever",list.size()+"");
        activity.initializeAdapter(list);
    }
}