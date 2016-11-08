package by.mtz.reminder.wakefullBroadcastReciever;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import by.mtz.reminder.App;
import by.mtz.reminder.Utils.Utils;
import by.mtz.reminder.wakefulService.NotificationWakefulIntentService;

/**
 * Created by Roma on 21.10.2016.
 */

public class SimpleWakefulReciever extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, NotificationWakefulIntentService.class);
        startWakefulService(context,service);
    }
}
