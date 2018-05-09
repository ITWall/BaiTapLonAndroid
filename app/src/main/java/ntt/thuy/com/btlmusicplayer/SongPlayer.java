package ntt.thuy.com.btlmusicplayer;

import android.content.ContentUris;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;

import java.io.IOException;

/**
 * Created by thuy on 29/04/2018.
 */
public class SongPlayer implements IPlayer{
    public static final int STATE_IDLE = 1;
    public static final int STATE_PLAYING = 2;
    public static final int STATE_PAUSED = 3;

    public int state;
    private Context context;

    private MediaPlayer mediaPlayer;

    public SongPlayer(Context context) {
        state = STATE_IDLE;
        this.context = context;
    }

//    @Override
//    public IBinder onBind(Intent intent) {
//        return new ServiceBinder();
//    }

    public class ServiceBinder extends Binder {
        public SongPlayer getService() {
            return SongPlayer.this;
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

    public void startSongForeground(long id) {
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            //mediaPlayer = MediaPlayer.create(this, uri);
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                Uri contentUri = ContentUris.withAppendedId(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
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
    public void startSong(long id) {

//        Intent intent = new Intent(context, SongPlayer.class);
//        intent.putExtra("song", id);
//        intent.setAction("action_play");
//        Log.i("SONG_ID",String.valueOf(id));
//        startService(intent);

        startSongForeground(id);
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
}
