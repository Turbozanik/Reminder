package by.mtz.reminder.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import by.mtz.reminder.DAO.Action;
import by.mtz.reminder.R;
import by.mtz.reminder.Utils.Utils;

/**
 * Created by Roma on 24.10.2016.
 */

public class ActionDetailsActivity extends BaseActivity {

    boolean mIsReceiverRegistered = false;
    Action action;
    ImageView detailsIv;
    TextView dayAndTimeTv;
    TextView descriptionTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_action_activity);

        this.initToolbarAndDrawerWithReadableName("Описание");

        detailsIv = (ImageView)findViewById(R.id.detailsActivityIv);
        dayAndTimeTv = (TextView)findViewById(R.id.day_and_time_tv);
        descriptionTv = (TextView)findViewById(R.id.description_tv);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        action = getIntent().getParcelableExtra(Utils.READ_ACTION_BY_ID);
        initializeData(action);
        //startService(dataIntent);
        progressDialog.cancel();


    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    public void initializeData(Action action){
        this.action = action;

        List<String> str = new ArrayList<>();
        File mydir = context.getDir(Utils.IMG_DIR_NO_APP, Context.MODE_PRIVATE);
        File lister = mydir.getAbsoluteFile();
        Collections.addAll(str, lister.list());
        for (int j = 0; j < str.size(); j++) {
            if ((this.action.getName() + ".jpg").equals(str.get(j))) {
                Log.e("inIf", "yes");
                Bitmap someBitmap = BitmapFactory.decodeFile(Utils.IMG_PATH + this.action.getName() + ".jpg");
                detailsIv.setImageBitmap(someBitmap);
                break;
            } else if (j == str.size() - 1) {
                Log.e("inIf", "no");
                detailsIv.setImageResource(R.drawable.account_default);
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE-yyyy/MM/dd-hh:mm:ss", Locale.getDefault());
        String asString = formatter.format(this.action.getDate());
        String[] parsedDate = asString.split("-");
        Log.e("time", asString);

        String time = parsedDate[2];
        //String date = parsedDate[1];
        String day = parsedDate[0];
        dayAndTimeTv.setText(time+" "+day);
        descriptionTv.setText(this.action.getDescription());
    }
}