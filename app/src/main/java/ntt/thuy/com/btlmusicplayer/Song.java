package ntt.thuy.com.btlmusicplayer;

import android.net.Uri;

/**
 * Created by thuy on 29/04/2018.
 */
public class Song {
    private String title;
    private String author;
    private long duration;
    private long id;
    private Uri data;

    public Song(String title, String author, long duration, long id, Uri data) {
        this.title = title;
        this.author = author;
        this.duration = duration;
        this.id = id;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getDuration() {
        return (int)(duration/1000);
    }

    public long getId() {
        return id;
    }

    public Uri getData() {
        return data;
    }
}
