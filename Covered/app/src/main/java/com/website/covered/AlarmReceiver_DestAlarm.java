package com.website.covered;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by jayashreemadhanraj on 11/30/16.
 * Reference: http://javapapers.com/android/android-alarm-clock-tutorial/
 */

public class AlarmReceiver_DestAlarm extends WakefulBroadcastReceiver {

    ArrayList<Integer> daysSet = new ArrayList<>();
    public AlarmReceiver_DestAlarm() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        daysSet = intent.getIntegerArrayListExtra("days");

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if(daysSet.contains(day)) {

            //Sound the alarm tone
            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
            Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
            ringtone.play();
            ringtone.stop();

            //send a notification message
            ComponentName comp = new ComponentName(context.getPackageName(),
                    AlarmService_DestAlarm.class.getName());
            startWakefulService(context, (intent.setComponent(comp)));
            setResultCode(Activity.RESULT_OK);
        }

    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        return super.peekService(myContext, service);
    }
}
