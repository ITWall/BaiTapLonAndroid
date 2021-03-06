package ntt.thuy.com.btlmusicplayer.offline;

import android.content.ContentUris;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;

import java.io.IOException;

import ntt.thuy.com.btlmusicplayer.entity.OfflineSong;
import ntt.thuy.com.btlmusicplayer.IPlayer;


/**
 * Created by thuy on 29/04/2018.
 */
public class OfflineSongPlayer implements IPlayer {
    public static final int STATE_IDLE = 1;
    public static final int STATE_PLAYING = 2;
    public static final int STATE_PAUSED = 3;

    public int state;
    private Context context;

    private MediaPlayer mediaPlayer;
    private OfflineSong song;

    public OfflineSongPlayer(Context context) {
        state = STATE_IDLE;
        this.context = context;
    }

    public void setSong(OfflineSong song) {
        this.song = song;
    }
//    @Override
//    public IBinder onBind(Intent intent) {
//        return new ServiceBinder();
//    }

    public class ServiceBinder extends Binder {
        public OfflineSongPlayer getService() {
            return OfflineSongPlayer.this;
        }
    }

    public int getState() {
        return state;
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        switch (intent.getAction()) {
//            case "action_play":
//                long id = intent.getLongExtra("song",0);
//                startSongForeground(id);
//                return Service.START_STICKY;
//
//            case "action_stop":
//                stopSong();
//                stopForeground(true);
//                stopSelf();
//                return Service.START_NOT_STICKY;
//
//            default:
//                return super.onStartCommand(intent, flags, startId);
//        }
//    }

    public void startSongForeground() {
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            //mediaPlayer = MediaPlayer.create(this, uri);
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                Uri contentUri = ContentUris.withAppendedId(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, song.getId());
                mediaPlayer.setDataSource(context, contentUri);
                mediaPlayer.prepare();
//                mediaPlayer.prepareAsync(); // bất đồng bộ, đọc audio trên mạng, connect bài hát trên luồng ngoài

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                        state = STATE_PLAYING;
                    }
                });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        state = STATE_IDLE;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void startSong() {

//        Intent intent = new Intent(context, OfflineSongPlayer.class);
//        intent.putExtra("song", id);
//        intent.setAction("action_play");
//        Log.i("SONG_ID",String.valueOf(id));
//        startService(intent);

        startSongForeground();
    }

    @Override
    public void pauseSong() {
        if (state == STATE_PLAYING) {
            mediaPlayer.pause();
            state = STATE_PAUSED;
        }
    }

    @Override
    public void resumeSong() {
        if (state == STATE_PAUSED) {
            mediaPlayer.start();
            state = STATE_PLAYING;
        }
    }

    @Override
    public void stopSong() {
        if (state != STATE_IDLE) {
            mediaPlayer.stop();
//            mediaPlayer.release();
            mediaPlayer = null;
            state = STATE_IDLE;
        }
    }

    @Override
    public void startSong(String streamUrl) {
        //nothing to do..
    }

    @Override
    public void releaseMediaPlayer() {
        //nothing to do..
    }

    public boolean isPlaying(){
        return this.state == STATE_PLAYING;
    }

    public boolean isPaused(){
        return this.state == STATE_PAUSED;
    }

    public boolean isIDLE(){
        return this.state == STATE_IDLE;
    }
}
