package ch.zli.wrecker.Control;

import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import java.util.ArrayList;

import ch.zli.wrecker.Model.Song;

/**
 * Created by admin on 13.11.2017.
 */

public class FileHandler {
    private Context context;

    public FileHandler(Context context){
        this.context = context;
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
}
