package ntt.thuy.com.btlmusicplayer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by thuy on 29/04/2018.
 */
public class SongManager {
    public SongManager(){

    }
    public ArrayList<Song> getAllSong(Context context){
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.DATA,
                },
                // mảng String là các cột cần lấy, nếu để null là lấy tất cả các cột trong bảng <=> SELECT * FROM ..
                null,
                null,
                MediaStore.Audio.Media.TITLE + " ASC"
        );

        if(cursor == null){
            return new ArrayList<Song>();
        }
        if(cursor.getCount() == 0){
            cursor.close();
            return new ArrayList<Song>();
        }

        ArrayList<Song> songs = new ArrayList<Song>();
        cursor.moveToFirst();
        int indexTitle = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
        int indexAuthor = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
        int indexDuration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
        int indexID = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
        int indexData = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

        while (!cursor.isAfterLast()){
            String title = cursor.getString(indexTitle);
            String author = cursor.getString(indexAuthor);
            Long duration = cursor.getLong(indexDuration);
            Long id = cursor.getLong(indexID);
            Uri data = Uri.parse(cursor.getString(indexData));

            songs.add(new Song(title,author,duration,id,data));
            cursor.moveToNext();
        }
        cursor.close();
        return songs;
    }
}
