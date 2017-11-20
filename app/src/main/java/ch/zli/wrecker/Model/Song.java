package ch.zli.wrecker.Model;

import java.io.Serializable;

/**
 * Created by admin on 13.11.2017.
 */

public class Song implements Serializable {
    private String artist;
    private String name;
    private String album;
    private String duration;
    private String path;
    private String _id;

    public String get_id() {
        return _id;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
