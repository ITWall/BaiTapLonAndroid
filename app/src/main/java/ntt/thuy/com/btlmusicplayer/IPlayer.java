package ntt.thuy.com.btlmusicplayer;

/**
 * Created by thuy on 29/04/2018.
 */
public interface IPlayer {
    void startSong();
    void pauseSong();
    void resumeSong();
    void stopSong();

    void startSong(String streamUrl);
    void releaseMediaPlayer();
}
