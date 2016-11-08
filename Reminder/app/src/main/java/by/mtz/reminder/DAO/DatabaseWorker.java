package by.mtz.reminder.DAO;



import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import by.mtz.reminder.Utils.Utils;
import by.mtz.reminder.activity.MainActivity;

/**
 * Created by Roma on 07.11.2016.
 */

public class DatabaseWorker extends AsyncTaskLoader<ArrayList> {

    private String command;
    private Context context;

    private DatabaseReference mDatabase;

    private ConnectivityManager conMan;
    private NetworkInfo netInfo;


    public DatabaseWorker(Context context, Bundle args) {
        super(context);
        this.context = context;
        command=args.getString(Utils.INTENT_SERVICE_INVOKE);
    }

    @Override
    public ArrayList loadInBackground() {
        Log.e("loader created","loading");
        switch (command) {
            case Utils.LOAD_All_DATA: {
                saveActivities();
                savePersons();
                savePictureData();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
                Log.e("order", "forth");
                return new ArrayList();
            }
            case Utils.READ_ACTIONS_DATA: {
                readActionData();
                return (ArrayList) Action.listAll(Action.class);
            }
            case Utils.READ_PERSONS_DATA: {
                readPersonsData();
                return (ArrayList) Person.listAll(Person.class);
            }
            default:{
                return new ArrayList();
            }
        }
    }
    private void saveActivities(){

        conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = conMan.getActiveNetworkInfo();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Activities");//https://activities-54cd5.firebaseio.com/

        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Action> actionList;
                    actionList = Action.listAll(Action.class);
                    Action action;
                    Action concreteAction = null;

                    List<Integer> numbersList;
                    numbersList = new ArrayList<Integer>();

                    Log.e("wifiState", netInfo.isConnected() + "");
                    //Action.deleteAll(Action.class);
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        action = child.getValue(Action.class);
                        numbersList.add(action.getActionId());

                        if(!actionList.contains(action)) {
                            for(Action item: actionList){
                                if(item.getActionId() == action.getActionId()){
                                    concreteAction = item;
                                }
                            }
                            if (concreteAction!=null && concreteAction.getActionId() == action.getActionId()) {
                                concreteAction.setActionId(action.getActionId());
                                concreteAction.setDate(action.getDate());
                                concreteAction.setDescription(action.getDescription());
                                concreteAction.setName(action.getName());
                                concreteAction.setRepeating(action.isRepeating());

                                concreteAction.save();
                                Log.d("update", "itemUpdated");
                            } else if(actionList.size()<=dataSnapshot.getChildrenCount()){
                                action.save();
                                Log.e("new", "newItem");
                            }
                        }
                    }
                    if (actionList.size()>numbersList.size()){
                        for(int i=0;i<actionList.size();i++){
                            for (int j=0;j<numbersList.size();j++){
                                if(numbersList.get(j) == actionList.get(i).getActionId()){
                                    break;
                                }else if(j==numbersList.size()-1){
                                    Action.delete(actionList.get(i));
                                }
                            }
                        }
                    }
                    Log.d("newDataCount",numbersList.size()+"");
                    Log.d("oldDataCount",actionList.size()+"");
                    List<Action> list = Action.listAll(Action.class);
                    for(Action act: list){
                        Log.e("data",act.getActionId()+"");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //Toast.makeText(DatabaseWorkService.this,"Error",Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Log.e("noNet","quesy data from local database");
        }
        Log.e("order","first");
    }

    private void savePersons(){

        conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = conMan.getActiveNetworkInfo();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Persons");//https://activities-54cd5.firebaseio.com/

        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Person> personList;
                    personList = Person.listAll(Person.class);
                    Person person;
                    Person concretePerson = null;

                    List<Integer> numbersList;
                    numbersList = new ArrayList<Integer>();

                    Log.e("wifiState", netInfo.isConnected() + "");
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        person = child.getValue(Person.class);
                        numbersList.add(person.getPersonId());

                        if(!personList.contains(person)) {
                            for(Person item: personList){
                                if(item.getPersonId() == person.getPersonId()){
                                    concretePerson = item;
                                }
                            }
                            if (concretePerson!=null && concretePerson.getPersonId() == person.getPersonId()) {
                                concretePerson.setPersonId(person.getPersonId());
                                concretePerson.setDescription(person.getDescription());
                                concretePerson.setName(person.getName());
                                concretePerson.setEmail(person.getEmail());

                                concretePerson.save();
                                Log.d("update", "itemUpdated");
                            } else if(personList.size()<=dataSnapshot.getChildrenCount()){
                                person.save();
                                Log.e("new", "newItem");
                            }
                        }
                    }
                    if (personList.size()>numbersList.size()){
                        for(int i=0;i<personList.size();i++){
                            for (int j=0;j<numbersList.size();j++){
                                if(numbersList.get(j) == personList.get(i).getPersonId()){
                                    break;
                                }else if(j==numbersList.size()-1){
                                    Person.delete(personList.get(i));
                                }
                            }
                        }
                    }
                    Log.d("newDataCount",numbersList.size()+"");
                    Log.d("oldDataCount",personList.size()+"");
                    List<Action> list = Action.listAll(Action.class);
                    for(Action act: list){
                        Log.e("data",act.getActionId()+"");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //Toast.makeText(DatabaseWorkService.this,"Error",Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Log.e("noNet","quesy data from local database");
        }
        Log.e("order","second");
    }

    private void readActionData(){
        Log.e("read actions data","actions");
        List<Action> actionList;
        actionList = Action.listAll(Action.class);

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(Utils.READ_ACTIONS_DATA);
        broadcastIntent.putParcelableArrayListExtra(Utils.READ_ACTIONS_DATA, (ArrayList<? extends Parcelable>) actionList);
        context.sendBroadcast(broadcastIntent);
    }
    private void readPersonsData(){
        Log.e("read actions data","persons");
        List<Person> personList;
        personList = Person.listAll(Person.class);

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(Utils.READ_PERSONS_DATA);
        broadcastIntent.putParcelableArrayListExtra(Utils.READ_PERSONS_DATA, (ArrayList<? extends Parcelable>) personList);
        context.sendBroadcast(broadcastIntent);
    }
    private void savePictureData(){
        conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = conMan.getActiveNetworkInfo();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Picture");//https://activities-54cd5.firebaseio.com/

        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {

            File dir = new File(Utils.IMG_DIR);
            try {
                FileUtils.deleteDirectory(dir);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Picture> pictureList;
                    pictureList = Picture.listAll(Picture.class);
                    Picture picture;
                    Picture concretePicture = null;

                    List<String> numbersList;
                    numbersList = new ArrayList<String>();

                    Log.e("wifiState", netInfo.isConnected() + "");
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        picture = child.getValue(Picture.class);
                        numbersList.add(picture.getName());

                        if(!pictureList.contains(picture)) {
                            for(Picture item: pictureList){
                                if(item.getName().equals(picture.getName())){
                                    concretePicture = item;
                                }
                            }
                            if (concretePicture!=null && concretePicture.getName().equals(picture.getName())) {
                                concretePicture.setBase64(picture.getBase64());
                                concretePicture.save();
                                Log.d("update", "itemUpdated");
                            } else if(pictureList.size()<=dataSnapshot.getChildrenCount()){
                                picture.save();
                                Log.e("new", "newItem");
                            }
                        }
                    }
                    if (pictureList.size()>numbersList.size()){
                        for(int i=0;i<pictureList.size();i++){
                            for (int j=0;j<numbersList.size();j++){
                                if(numbersList.get(j).equals(pictureList.get(i).getName())){
                                    break;
                                }else if(j==numbersList.size()-1){
                                    Picture.delete(pictureList.get(i));
                                }
                            }
                        }
                    }
                    Log.d("newDataCount",numbersList.size()+"");
                    Log.d("oldDataCount",pictureList.size()+"");
                    List<Picture> list = Picture.listAll(Picture.class);

                    ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
                    // path to /data/data/yourapp/app_imageDir
                    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

                    for(Picture pic: list){
                        Log.e("data",pic.getName()+"");
                        File mypath=new File(directory,pic.getName()+".jpg");
                        Log.e("path",mypath.getAbsolutePath());
                        //Utils.saveImage(Utils.decode64Bitmap(pic.getBase64()),mypath.getAbsolutePath());
                        try {
                            Utils.savebitmap(Utils.decode64Bitmap(pic.getBase64()),mypath.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //Toast.makeText(DatabaseWorkService.this,"Error",Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Log.e("noNet","quesy data from local database");
        }
        Log.e("order","third");
    }
    private void readActionDataById(int _id){//edit
        Log.e("read actions data by id","actions");
        int id = _id;
        Action action = Action.findById(Action.class,id);

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(Utils.READ_ACTION_BY_ID);
        broadcastIntent.putExtra(Utils.READ_ACTION_BY_ID,action);
        context.sendBroadcast(broadcastIntent);
    }




}
