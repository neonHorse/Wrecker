package ch.zli.wrecker.Control;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Calendar;

import ch.zli.wrecker.Activities.RunningAlarmActivity;
import ch.zli.wrecker.Model.Alarm;

/**
 * Created by admin on 07.11.2017.
 */

public class AlarmHandler {
    private static final AlarmHandler ourInstance = new AlarmHandler();
    private ArrayList<Alarm> alarms = new ArrayList<>();
    private Context context;
    private FileHandler fileHandler;
    private AlarmManager alarmManager;


    public static AlarmHandler getInstance() {
        return ourInstance;
    }

    private AlarmHandler() {
    }

    public void initialize(Context context){
        this.context = context;
        fileHandler = new FileHandler(this.context);
        alarmManager = (AlarmManager)this.context.getSystemService(Context.ALARM_SERVICE);

    }

    public void setAllAlarmsAfterBoot(){
        alarms = fileHandler.getAlarms();

        for (Alarm a : alarms){
            setAlarmClock(a);
        }
    }

    public ArrayList<Alarm> getAlarms() {
        return alarms;
    }

    public void addAlarm(Alarm alarm){
        alarms.add(alarm);
        setAlarmClock(alarm);

        fileHandler.saveAlarms(alarms);
    }

    public void resetAlarm(Alarm alarm){
        if(!alarm.getRecurrences().isEmpty()){
            setAlarmClock(alarm);
        }
        else {
            int index = alarms.indexOf(alarm);
            alarm.setActive(false);
            if(index >= 0) {
                alarms.set(index, alarm);

                fileHandler.saveAlarms(alarms);
            }
        }
    }

    public void setActive(Alarm alarm, boolean isActive){
        int index = alarms.indexOf(alarm);

        if(!isActive){
            cancelAlarm(alarm);
        }

        alarm.setActive(isActive);
        if(index >= 0) {
            alarms.set(index, alarm);

            fileHandler.saveAlarms(alarms);
        }
    }

    public void changeAlarm(Alarm oldAlarm, Alarm newAlarm){
        int index = alarms.indexOf(oldAlarm);

        if(index >= 0){
            cancelAlarm(oldAlarm);
            alarms.set(index, newAlarm);
            setAlarmClock(newAlarm);
        }

        fileHandler.saveAlarms(alarms);
    }

    public void deleteAlarm(Alarm alarm){
        int index = alarms.indexOf(alarm);

        if(index >= 0){
            cancelAlarm(alarm);
            alarms.remove(index);
        }

        fileHandler.saveAlarms(alarms);
    }

    private void cancelAlarm(Alarm alarm){
        if(alarm.getPendingIntent() != null){
            alarmManager.cancel(alarm.getPendingIntent());
        }
    }

    private void setAlarmClock(Alarm alarm) {
        if (alarm.isActive()) {
            if (alarm.getPendingIntent() == null) {
                Intent intent = new Intent(context, RunningAlarmActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("alarm", alarm);
                intent.putExtra("bundle", bundle);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarm.setPendingIntent(pendingIntent);
            } else {
                alarmManager.cancel(alarm.getPendingIntent());
            }

            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            int daysToAdd = 0;

            if (!alarm.getRecurrences().isEmpty()) {
                daysToAdd = getDaysToClosestDay(day, alarm.getRecurrences());
            }

            if(daysToAdd == 0){
                if (hour > alarm.getHour()) {
                    daysToAdd++;
                } else if (hour == alarm.getHour()) {
                    if (minute >= alarm.getMinute()) {
                        daysToAdd++;
                    }
                }
            }

            calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
            calendar.set(Calendar.MINUTE, alarm.getMinute());
            calendar.set(Calendar.SECOND, 0);
            calendar.add(Calendar.DATE, daysToAdd);

            AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), null);
            alarmManager.setAlarmClock(alarmClockInfo, alarm.getPendingIntent());
        }
    }

    private int getDaysToClosestDay(int current, ArrayList<Integer> all){
      int tmp = current;
      int daysToClosesDay = 0;

      while (!all.contains(tmp)){
          tmp++;
          daysToClosesDay++;

          if(tmp > 7){
              tmp = 1;
          }
      }

      return daysToClosesDay;
    }
}
