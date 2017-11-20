package ch.zli.wrecker.Control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by admin on 14.11.2017.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            AlarmHandler.getInstance().initialize(context.getApplicationContext());
            AlarmHandler.getInstance().setAllAlarmsAfterBoot();
        }
    }
}
