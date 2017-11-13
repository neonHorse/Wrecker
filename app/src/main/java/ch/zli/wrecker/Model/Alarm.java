package ch.zli.wrecker.Model;

import java.util.Calendar;
import java.util.List;

/**
 * Created by admin on 07.11.2017.
 */

public class Alarm {
    private Calendar alarmTime;
    private boolean isActive;
    private List<Day> recurrences;
}
