package ch.zli.wrecker.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import ch.zli.wrecker.Control.AlarmHandler;
import ch.zli.wrecker.Control.AlarmListAdapter;
import ch.zli.wrecker.Model.Alarm;
import ch.zli.wrecker.R;

public class MainActivity extends AppCompatActivity {
    private ListView listAlarms;
    private AlarmListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeList();

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSetAlarmActivity();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_alarm, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuDelete:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Alarm alarm = (Alarm) listAdapter.getItem(info.position);
                AlarmHandler.getInstance().deleteAlarm(alarm);
                listAdapter.notifyDataSetChanged();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void initializeList(){
        if(listAlarms == null){
            listAlarms = findViewById(R.id.listAlarms);
        }

        if(listAdapter == null){
            listAdapter = new AlarmListAdapter(MainActivity.this, AlarmHandler.getInstance().getAlarms());
        }

        listAlarms.setAdapter(listAdapter);

        registerForContextMenu(listAlarms);
    }

    private void startSetAlarmActivity(){
        Intent intent = new Intent(MainActivity.this, SetAlarmActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}
