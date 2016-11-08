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

import by.mtz.reminder.DAO.DatabaseWorkService;
import by.mtz.reminder.DAO.Person;
import by.mtz.reminder.R;
import by.mtz.reminder.Utils.Utils;
import by.mtz.reminder.adapters.PersonsRVAdapter;

/**
 * Created by Roma on 19.10.2016.
 */

public class PersonsListActivity extends BaseActivity {

    boolean mIsReceiverRegistered = false;
    PersonsBroadcastReceiver receiver;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    List<Person> persons;
    PersonsRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.persons_list_activity);

        this.initToolbarAndDrawerWithReadableName(getString(R.string.persons));

            recyclerView = (RecyclerView) findViewById(R.id.personsRecyclerView);

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.show();
            Intent dataIntent = new Intent(this, DatabaseWorkService.class);
            dataIntent.putExtra(Utils.INTENT_SERVICE_INVOKE, Utils.READ_PERSONS_DATA);
            startService(dataIntent);

            persons = new ArrayList<>();
            manager = new LinearLayoutManager(this);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(manager);

            adapter = new PersonsRVAdapter(persons, context);
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
                    receiver = new PersonsBroadcastReceiver();
                }
                IntentFilter filter = new IntentFilter();
                filter.addAction(Utils.READ_PERSONS_DATA);
                receiver.setMainActivityHandler(this);
                registerReceiver(receiver, filter);
                mIsReceiverRegistered = true;
            }
        }
    public void initializeAdapter(List<Person> personList){
        Log.e("init","update adapte");
        persons.clear();
        for (Person pers :personList) {
            persons.add(pers);
        }
        Log.e("list",persons.size()+"");
        adapter.notifyDataSetChanged();
    }

}
class PersonsBroadcastReceiver extends BroadcastReceiver {

    PersonsListActivity activity = null;
    ArrayList<Person> list;

    public void setMainActivityHandler(PersonsListActivity main){
        activity = main;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("reciever","reciev");
        list = intent.getParcelableArrayListExtra(Utils.READ_PERSONS_DATA);
        for (Person person:list){
            Log.d("list",person.getName());
            if(activity!=null) {
                activity.initializeAdapter(list);
            }
        }
    }
}