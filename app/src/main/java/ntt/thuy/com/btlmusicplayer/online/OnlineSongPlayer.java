package ntt.thuy.com.btlmusicplayer.online;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

import ntt.thuy.com.btlmusicplayer.Config;
import ntt.thuy.com.btlmusicplayer.IPlayer;

/**
 * Created by thuy on 25/05/2018.
 */
public class OnlineSongPlayer implements IPlayer {
    public static final int STATE_IDLE = 1;
    public static final int STATE_PLAYING = 2;
    public static final int STATE_PAUSED = 3;

    public int state = STATE_IDLE;

    private MediaPlayer mediaPlayer;
    private Context mContext;

    private MediaPlayerMonitoring listener;

    public OnlineSongPlayer(Context context, MediaPlayerMonitoring listener) {
        this.mContext = context;
        this.listener = listener;
        state = STATE_IDLE;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public int getState() {
        return state;
    }

    public void startSongForeground(String streamUrl) {
        if (state != STATE_IDLE) {
            stopSong();
        }
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {

            try {
                final ProgressDialog progress = new ProgressDialog(mContext);
                progress.setMessage("Please wait while loading song...");
                progress.setCancelable(false);
                progress.show();

                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                mediaPlayer.setDataSource(streamUrl + "?client_id=" + Config.CLIENT_ID);
                mediaPlayer.prepareAsync();

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        listener.startNewThread();
                        progress.dismiss();
                        mediaPlayer.start();
                        state = STATE_PLAYING;
                    }
                });
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        Log.d("TEST", "on completion");
                        state = STATE_IDLE;
                        listener.onCompletion();
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mediaPlayer.pause();
        }
    }

    @Override
    public void startSong(String streamUrl) {
        Log.d("TEST", "start song");
        startSongForeground(streamUrl);
    }

    @Override
    public void startSong() {
        //nothing to do..
    }

    @Override
    public void pauseSong() {
        if (state == STATE_PLAYING) {
            Log.d("TEST", "pause song");
            mediaPlayer.pause();
            state = STATE_PAUSED;
        } else return;
    }

    @Override
    public void resumeSong() {
        if (state == STATE_PAUSED) {
            Log.d("TEST", "resume song");
            mediaPlayer.start();
            state = STATE_PLAYING;
        } else return;
    }

    @Override
    public void stopSong() {
        if (state != STATE_IDLE) {
            Log.d("TEST", "stop song");
            mediaPlayer.stop();
            mediaPlayer = null;
            state = STATE_IDLE;
        } else return;
    }

    @Override
    public void releaseMediaPlayer(){
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public interface MediaPlayerMonitoring{
        void onCompletion();
        void startNewThread();
    }
}
