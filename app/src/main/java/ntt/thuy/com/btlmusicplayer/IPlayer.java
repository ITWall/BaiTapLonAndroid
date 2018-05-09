package ntt.thuy.com.btlmusicplayer;

/**
 * Created by thuy on 29/04/2018.
 */
public interface IPlayer {
    void startSong(long id);
    void pauseSong();
    void resumeSong();
    void stopSong();
}
