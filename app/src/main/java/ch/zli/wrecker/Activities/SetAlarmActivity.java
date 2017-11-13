package ch.zli.wrecker.Activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import ch.zli.wrecker.Control.FileHandler;
import ch.zli.wrecker.Control.SongListAdapter;
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

        openSelectMusicDialog();
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
