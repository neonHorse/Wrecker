package ch.zli.wrecker.Model;

import android.app.PendingIntent;
import android.content.Intent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by admin on 07.11.2017.
 */

public class Alarm implements Serializable {
    private int hour;
    private int minute;
    private boolean isActive;
    private boolean needsVibrate;
    private ArrayList<Integer> recurrences;
    private Song song;
    private transient PendingIntent pendingIntent;

    public Alarm(){
        needsVibrate = false;
        isActive = false;
        recurrences = new ArrayList<>();
    }

    public PendingIntent getPendingIntent() {
        return pendingIntent;
    }

    public void setPendingIntent(PendingIntent pendingIntent) {
        this.pendingIntent = pendingIntent;
    }

    public Song getSong() {
        return song;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public ArrayList<Integer> getRecurrences() {
        return recurrences;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean needsVibrate() {
        return needsVibrate;
    }

    public void setRecurrences(ArrayList<Integer> recurrences) {
        this.recurrences = recurrences;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public void setNeedsVibrate(boolean needsVibrate) {
        this.needsVibrate = needsVibrate;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
