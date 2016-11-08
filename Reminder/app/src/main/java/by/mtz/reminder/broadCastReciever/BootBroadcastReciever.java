package by.mtz.reminder.broadCastReciever;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import by.mtz.reminder.App;
import by.mtz.reminder.Utils.Utils;
import by.mtz.reminder.wakefulService.NotificationWakefulIntentService;
import by.mtz.reminder.wakefullBroadcastReciever.SimpleWakefulReciever;

/**
 * Created by Roma on 22.10.2016.
 */

public class BootBroadcastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!App.isRunning) {
            Log.d("wakefull", "start");
            Intent startIntent = new Intent(context, SimpleWakefulReciever.class);
            PendingIntent startPIntent = PendingIntent.getBroadcast(context, 0, startIntent, 0);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC_WAKEUP,
                    SystemClock.elapsedRealtime() + 3000, 360000, startPIntent);
            App.isRunning = true;
        }
        Log.e("bool",App.isRunning+"");
    }
}
