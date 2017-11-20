package ch.zli.wrecker.Activities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import ch.zli.wrecker.Control.AlarmHandler;
import ch.zli.wrecker.Control.FileHandler;
import ch.zli.wrecker.Control.VisualizerView;
import ch.zli.wrecker.Model.Alarm;
import ch.zli.wrecker.R;

public class RunningAlarmActivity extends AppCompatActivity {
    private static final float VOLUME = 1f;
    private static final long[] VIBRATE_INTERVAL = {0, 1000, 500};
    private static final int REPEAT_INTERVAL = 100;
    public static final String DIRECTORY_NAME_TEMP = "AudioTemp";

    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;
    private Alarm alarm;
    private VisualizerView visualizerView;
    private MediaRecorder mediaRecorder;
    private Handler audioHandler;

    private SensorManager sensorManager;
    private float accel;
    private float accelCurrent;
    private float accelLast;

    private File audioDirTemp;
    private int shakeCount = 0;

    private boolean wasPhoneShaken = false;
    private boolean wasLoudEnough = false;
    private boolean isRecording = false;

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            accelLast = accelCurrent;
            accelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = accelCurrent - accelLast;
            accel = accel * 0.9f + delta; // perform low-cut filter

            if (accel > 12) {
                shakeCount++;
                handleShakeEvent(shakeCount);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private Runnable updateVisualizer = new Runnable() {
        @Override
        public void run() {
            if (isRecording) // if we are already recording
            {
                // get the current amplitude
                int x = mediaRecorder.getMaxAmplitude();

                visualizerView.addAmplitude(x); // update the VisualizeView
                visualizerView.invalidate(); // refresh the VisualizerView

                if(x > 20000){
                    wasLoudEnough = true;

                    if(wasPhoneShaken){
                        stopAlarm();
                    }
                }

                // update in 40 milliseconds
                audioHandler.postDelayed(this, REPEAT_INTERVAL);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_alarm);

        Bundle bundle = getIntent().getBundleExtra("bundle");

        if(bundle != null){
            alarm = (Alarm)bundle.getSerializable("alarm");
        }

        audioHandler = new Handler();

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        accel = 0.00f;
        accelCurrent = SensorManager.GRAVITY_EARTH;
        accelCurrent = SensorManager.GRAVITY_EARTH;

        initializeAlarm();

        startAlarm();

        AlarmHandler.getInstance().initialize(getApplicationContext());

        visualizerView = (VisualizerView)findViewById(R.id.visualizerView);

        audioDirTemp = new File(Environment.getExternalStorageDirectory(),
                DIRECTORY_NAME_TEMP);
        if (audioDirTemp.exists()) {
            deleteFilesInDir(audioDirTemp);
        } else {
            audioDirTemp.mkdirs();
        }
        audioHandler = new Handler();

        startRecording();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseRecorder();
    }

    public static boolean deleteFilesInDir(File path) {

        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for(int i=0; i<files.length; i++) {

                if(files[i].isDirectory()) {

                }
                else {
                    files[i].delete();
                }
            }
        }
        return true;
    }

    private void startRecording(){
        if(!isRecording){
            mediaRecorder = new MediaRecorder();

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(audioDirTemp + "/audio_file.mp3");

            MediaRecorder.OnErrorListener errorListener = null;
            mediaRecorder.setOnErrorListener(errorListener);
            MediaRecorder.OnInfoListener infoListener = null;
            mediaRecorder.setOnInfoListener(infoListener);

            try{
                mediaRecorder.prepare();
                mediaRecorder.start();

                isRecording = true;

            }
            catch (Exception ex){
                ex.printStackTrace();
            }

            audioHandler.post(updateVisualizer);
        }
    }

    private void stopRecording(){
        if(isRecording){
            releaseRecorder();
        }
    }

    private void releaseRecorder(){
        if (mediaRecorder != null) {
            isRecording = false; // stop recording
            audioHandler.removeCallbacks(updateVisualizer);
            visualizerView.clear();
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private void handleShakeEvent(int count){
        if(count >= 20 && !wasPhoneShaken){
            wasPhoneShaken = true;

            if(wasLoudEnough){
                stopAlarm();
            }

            sensorManager.unregisterListener(sensorListener);
        }
    }

    private void initializeAlarm(){
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        Uri uri = new Uri.Builder().appendPath(alarm.getSong().getPath()).build();
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(alarm.getSong().getPath());
            mediaPlayer.setVolume(VOLUME, VOLUME);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void startAlarm(){
        try {
            if(alarm.needsVibrate()){
                vibrator.vibrate(VIBRATE_INTERVAL, 0);
            }

            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopAlarm(){
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            mediaPlayer.setLooping(false);
        }

        if(vibrator != null && alarm.needsVibrate()){
            vibrator.cancel();
        }

        stopRecording();

        AlarmHandler.getInstance().resetAlarm(alarm);

        finishAndRemoveTask();
    }
}
