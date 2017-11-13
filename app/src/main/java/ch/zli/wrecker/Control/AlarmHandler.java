package ch.zli.wrecker.Control;

import java.util.ArrayList;
import java.util.Calendar;

import ch.zli.wrecker.Model.Alarm;

/**
 * Created by admin on 07.11.2017.
 */

public class AlarmHandler {
    private static final AlarmHandler ourInstance = new AlarmHandler();

    private ArrayList<Alarm> alarms;

    public static AlarmHandler getInstance() {
        return ourInstance;
    }

    private AlarmHandler() {
    }

    public void setAlarm(Alarm alarm){

    }

    public void changeTime(Alarm alarm, Calendar newTime){

    }

    public void deleteALarm(Alarm alarm){

    }
}
