package ntt.thuy.com.btlmusicplayer.entity;

import android.net.Uri;

/**
 * Created by thuy on 29/04/2018.
 */
public class OfflineSong extends Song {

    private String author;

    public OfflineSong(String title, String author, long duration, long id, String data) {
        this.title = title;
        this.author = author;
        this.duration = duration;
        this.id = id;
        this.uri = data;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public long getDuration() {
        return (int)(duration/1000);
    }

    public long getId() {
        return id;
    }

    public Uri getData() {
        return Uri.parse(this.uri);
    }
}
