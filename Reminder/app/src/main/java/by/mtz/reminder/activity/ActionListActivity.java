package by.mtz.reminder.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import by.mtz.reminder.DAO.Action;
import by.mtz.reminder.DAO.DatabaseWorkService;
import by.mtz.reminder.R;
import by.mtz.reminder.Utils.Utils;
import by.mtz.reminder.adapters.ActionsRVAdapter;

/**
 * Created by Roma on 17.10.2016.
 */

public class ActionListActivity extends BaseActivity {

    boolean mIsReceiverRegistered = false;
    ActionsBroadcastReceiver receiver;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    List<Action> actions;
    ActionsRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_list_activity);
        this.initToolbarAndDrawerWithReadableName(getString(R.string.our_actions));
        recyclerView = (RecyclerView)findViewById(R.id.activitiesRecyclerView);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        Intent dataIntent = new Intent(this, DatabaseWorkService.class);
        dataIntent.putExtra(Utils.INTENT_SERVICE_INVOKE, Utils.READ_ACTIONS_DATA);
        startService(dataIntent);

        actions = new ArrayList<>();
        manager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        adapter = new ActionsRVAdapter(actions,context);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();
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
                receiver = new ActionsBroadcastReceiver();
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
            if (act.isRepeating()) {
                actions.add(act);
            }
        }
        Log.e("list",actions.size()+"");
        adapter.notifyDataSetChanged();
    }


}
class ActionsBroadcastReceiver extends BroadcastReceiver{

    ActionListActivity activity = null;
    ArrayList<Action> list;

    public void setMainActivityHandler(ActionListActivity main){
        activity = main;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("reciever","reciev");
        list = intent.getParcelableArrayListExtra(Utils.READ_ACTIONS_DATA);
        activity.initializeAdapter(list);
    }
}