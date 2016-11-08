package by.mtz.reminder.wakefulService;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import by.mtz.reminder.DAO.Action;
import by.mtz.reminder.R;
import by.mtz.reminder.Utils.Utils;
import by.mtz.reminder.activity.ActionDetailsActivity;
import by.mtz.reminder.activity.MainActivity;
import by.mtz.reminder.wakefullBroadcastReciever.SimpleWakefulReciever;

/**
 * Created by Roma on 21.10.2016.
 */

public class NotificationWakefulIntentService extends IntentService {

    public NotificationWakefulIntentService() {
        super("NotificationWakefulIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("time",(System.currentTimeMillis()/1000)+"");

        int i = 0;
        Date dateTemp = null;
        Date dateTempLocal = null;
        Date timeTemp = null;
        Date timeTempLocal = null;
        List<Action> actionList = Action.listAll(Action.class);

        for (Action act: actionList){
            i++;
            if (act.isRepeating()){
                SimpleDateFormat formatter = new SimpleDateFormat("EEEE-yyyy/MM/dd-hh:mm:ss", Locale.getDefault());
                String asString = formatter.format(act.getDate());
                String[] parsedDate = asString.split("-");
                Log.e("time", asString);

                String time = parsedDate[2];
                //String date = parsedDate[1];
                String day = parsedDate[0];

                String asStringLocal = formatter.format(System.currentTimeMillis());
                String[] parsedDateLocal = asStringLocal.split("-");

                String timeLocal = parsedDateLocal[2];
                //String dateLocal = parsedDateLocal[1];
                String dayLocal = parsedDateLocal[0];

                try {
                    SimpleDateFormat formatterTemp = new SimpleDateFormat("hh:mm:ss",Locale.getDefault());
                    dateTemp = formatterTemp.parse(time);
                    dateTempLocal = formatterTemp.parse(timeLocal);
                    Log.e("date", dateTemp+"");
                    Log.e("dateLocal", dateTempLocal+"");
                    Log.e("dateEquality", (dateTemp.compareTo(dateTempLocal))+"");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (dateTemp!=null && dateTempLocal!=null) {
                    Long timeBeforeAction = (act.getDate() - System.currentTimeMillis())/36000;//edit
                    if (day.equals(dayLocal) && dateTemp.compareTo(dateTempLocal) > 0) {
                        notify(getString(R.string.upcoming_activity),act.getName(),act);
                    }
                }
            }else {
                SimpleDateFormat formatter = new SimpleDateFormat("EEEE-yyyy/MM/dd-hh/mm/ss", Locale.getDefault());
                String asString = formatter.format(act.getDate());
                String[] parsedDate = asString.split("-");
                Log.e("time", asString);

                String time = parsedDate[2];
                String date = parsedDate[1];
                String day = parsedDate[0];

                String asStringLocal = formatter.format(System.currentTimeMillis());
                String[] parsedDateLocal = asStringLocal.split("-");

                String timeLocal = parsedDateLocal[2];
                String dateLocal = parsedDateLocal[1];
                String dayLocal = parsedDateLocal[0];

                try {
                    SimpleDateFormat formatterTemp = new SimpleDateFormat("hh/mm/ss",Locale.getDefault());
                    timeTemp = formatterTemp.parse(time);
                    timeTempLocal = formatterTemp.parse(timeLocal);
                    dateTemp = formatterTemp.parse(date);
                    dateTempLocal = formatterTemp.parse(dateLocal);
                    Log.e("date", dateTemp+"");
                    Log.e("dateLocal", dateTempLocal+"");
                    Log.e("dateEquality", (dateTemp.compareTo(dateTempLocal))+"");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(dateTemp!=null && dateTempLocal!=null && timeTemp!=null && timeTempLocal!=null){
                    Long timeBeforeAction = (act.getDate() - System.currentTimeMillis())/36000;
                    if (day.equals(dayLocal) && timeTemp.compareTo(timeTempLocal) > 0 && timeBeforeAction<=3 && date.compareTo(dateLocal)==0){
                        notify(act.getName(),act.getDescription(),act);
                    }
                }
            }
        }

        SimpleWakefulReciever.completeWakefulIntent(intent);
    }

    private void notify(String notificationTitle, String notificationMessage,Action action){
        Intent notificationIntent = new Intent(this, ActionDetailsActivity.class);
        notificationIntent.putExtra(Utils.READ_ACTION_BY_ID,action);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Resources res = this.getResources();
        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.account_default)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.account_default))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage);
        Notification n = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            n = builder.build();
        }else {
            n = builder.getNotification();
        }

        nm.notify(action.getActionId(), n);
    }

}
