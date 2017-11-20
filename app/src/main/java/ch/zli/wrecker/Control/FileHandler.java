package ch.zli.wrecker.Control;

import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import ch.zli.wrecker.Model.Alarm;
import ch.zli.wrecker.Model.Song;

/**
 * Created by admin on 13.11.2017.
 */

public class FileHandler {
    private static final String ALARMS_FILENAME = "AllAlarms.ser";
    private String alarmsFileName;

    private Context context;

    public FileHandler(Context context){
        this.context = context;
        alarmsFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + ALARMS_FILENAME;
    }

    public ArrayList<Song> getSongs(){
        ArrayList<Song> songs = new ArrayList<>();

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media._ID
        };

        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, selection, null, MediaStore.Audio.Media.TITLE);

        if(cursor != null){
            cursor.moveToFirst();

            while (cursor.moveToNext()){
                Song s = new Song();

                s.setName(cursor.getString(0));
                s.setArtist(cursor.getString(1));
                s.setAlbum(cursor.getString(2));
                s.setDuration(cursor.getString(3));
                s.setPath(cursor.getString(4));
                s.set_id(cursor.getString(5));

                songs.add(s);
            }

            cursor.close();
        }

        return songs;
    }

    public ArrayList<Alarm> getAlarms(){
        ArrayList<Alarm> alarms = new ArrayList<>();

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(alarmsFileName));

            alarms = (ArrayList<Alarm>)ois.readObject();

            ois.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return alarms;
    }

    public void saveAlarms(ArrayList<Alarm> alarms){
        if(!alarms.isEmpty()) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(alarmsFileName));

                oos.writeObject(alarms);
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
