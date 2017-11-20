package ch.zli.wrecker.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import ch.zli.wrecker.Control.AlarmHandler;
import ch.zli.wrecker.Control.FileHandler;
import ch.zli.wrecker.Control.SongListAdapter;
import ch.zli.wrecker.Model.Alarm;
import ch.zli.wrecker.Model.Song;
import ch.zli.wrecker.R;

public class SetAlarmActivity extends AppCompatActivity {
    private FileHandler fileHandler;
    private Song chosenSong;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        fileHandler = new FileHandler(getApplicationContext());

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBtnSaveClick();
            }
        });

        findViewById(R.id.song_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSelectMusicDialog();
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainActivity();
            }
        });

        ((TimePicker)findViewById(R.id.timepicker)).setIs24HourView(true);


    }

    private void onBtnSaveClick(){
        if(chosenSong != null){
            boolean needsVibrate;
            int hour, minute;
            ArrayList<Integer> days = new ArrayList<>();

            TimePicker timePicker = findViewById(R.id.timepicker);
            CheckBox chkVibrate = findViewById(R.id.vibration);
            Switch mon = findViewById(R.id.monday_bar);
            Switch tue = findViewById(R.id.tuesday_bar);
            Switch wed = findViewById(R.id.wednesday_bar);
            Switch thu = findViewById(R.id.thuesday_bar);
            Switch fri = findViewById(R.id.friday_bar);
            Switch sat = findViewById(R.id.saturday_bar);
            Switch sun = findViewById(R.id.sunday_bar);

            if(mon.isChecked()){
                days.add(Calendar.MONDAY);
            }

            if(tue.isChecked()){
                days.add(Calendar.TUESDAY);
            }

            if(wed.isChecked()){
                days.add(Calendar.WEDNESDAY);
            }

            if(thu.isChecked()){
                days.add(Calendar.THURSDAY);
            }

            if(fri.isChecked()){
                days.add(Calendar.FRIDAY);
            }

            if(sat.isChecked()){
                days.add(Calendar.SATURDAY);
            }

            if(sun.isChecked()){
                days.add(Calendar.SUNDAY);
            }

            needsVibrate = chkVibrate.isChecked();


            hour = timePicker.getHour();
            minute = timePicker.getMinute();
            Alarm alarm = new Alarm();

            alarm.setActive(true);
            alarm.setHour(hour);
            alarm.setMinute(minute);
            alarm.setNeedsVibrate(needsVibrate);
            alarm.setRecurrences(days);
            alarm.setSong(chosenSong);

            AlarmHandler.getInstance().addAlarm(alarm);

            openMainActivity();
        }
        else {
            Toast.makeText(this, "Bitte ein Lied auswählen", Toast.LENGTH_SHORT).show();
        }
    }

    private void openMainActivity(){
        Intent intent = new Intent(SetAlarmActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private void openSelectMusicDialog(){
        LayoutInflater inflater = LayoutInflater.from(SetAlarmActivity.this);

        View view = inflater.inflate(R.layout.dialog_select_music, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(SetAlarmActivity.this);

        builder.setView(view);

        ListView lvSongs = view.findViewById(R.id.lvSongs);
        final SongListAdapter listAdapter = new SongListAdapter(SetAlarmActivity.this, fileHandler.getSongs());

        lvSongs.setAdapter(listAdapter);

        lvSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Song s = (Song)listAdapter.getItem(i);
                chosenSong = s;
                ((Button)findViewById(R.id.song_change)).setText(s.getName());
                alertDialog.dismiss();
            }
        });

        EditText txtSearchSongs = view.findViewById(R.id.txtSearchSongs);

        txtSearchSongs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(listAdapter != null){
                    listAdapter.getFilter().filter(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        builder.setCancelable(false);
        builder.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }
}
