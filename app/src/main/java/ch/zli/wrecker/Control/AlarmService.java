package ch.zli.wrecker.Control;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import ch.zli.wrecker.Activities.RunningAlarmActivity;
import ch.zli.wrecker.Model.Alarm;

/**
 * Created by admin on 07.11.2017.
 */

public class AlarmService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Alarm alarm = (Alarm) intent.getSerializableExtra("alarm");
        Intent newIntent = new Intent(this, RunningAlarmActivity.class);
        newIntent.putExtra("alarm", alarm);

        startActivity(newIntent);

        return START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
