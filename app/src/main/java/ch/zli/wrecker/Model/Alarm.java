package ch.zli.wrecker.Model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by admin on 07.11.2017.
 */

public class Alarm {
    private Calendar alarmTime;
    private boolean isActive;
    private boolean needsVibrate;
    private ArrayList<Day> recurrences;
    private Song song;

    public Alarm(){
        needsVibrate = false;
        isActive = false;
        recurrences = new ArrayList<>();
    }

    public Alarm(Calendar alarmTime, Song song, boolean needsVibrate, ArrayList<Day> recurrences){
        isActive = true;
        this.alarmTime = alarmTime;
        this.needsVibrate = needsVibrate;
        this.recurrences = recurrences;
        this.song = song;
    }

    public Song getSong() {
        return song;
    }

    public ArrayList<Day> getRecurrences() {
        return recurrences;
    }

    public Calendar getAlarmTime() {
        return alarmTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean needsVibrate() {
        return needsVibrate;
    }

    public void setRecurrences(ArrayList<Day> recurrences) {
        this.recurrences = recurrences;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public void setAlarmTime(Calendar alarmTime) {
        this.alarmTime = alarmTime;
    }

    public void setNeedsVibrate(boolean needsVibrate) {
        this.needsVibrate = needsVibrate;
    }
}
