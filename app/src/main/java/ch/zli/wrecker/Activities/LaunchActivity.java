package ch.zli.wrecker.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ch.zli.wrecker.Control.AlarmHandler;
import ch.zli.wrecker.R;

public class LaunchActivity extends AppCompatActivity {
    private static final int PERMISSION_ALL = 2323;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        getPermissions();

        AlarmHandler.getInstance().initialize(getApplicationContext());
        AlarmHandler.getInstance().setAllAlarmsAfterBoot();
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void getPermissions(){
        String[] PERMISSIONS = {Manifest.permission.RECEIVE_BOOT_COMPLETED, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.VIBRATE};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        else{
            Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
            startActivity(intent);

            overridePendingTransition(0, 0);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allPermissionGranted = true;

        if (requestCode == PERMISSION_ALL){
            for (int i : grantResults){
                if(i == PackageManager.PERMISSION_DENIED){
                    allPermissionGranted = false;
                }
            }

            if(allPermissionGranted){
                Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                startActivity(intent);

                overridePendingTransition(0, 0);
                finish();
            }
            else {
                finish();
            }
        }
    }
}
