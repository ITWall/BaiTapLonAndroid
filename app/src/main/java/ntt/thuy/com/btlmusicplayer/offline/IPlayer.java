package ntt.thuy.com.btlmusicplayer.offline;

import ntt.thuy.com.btlmusicplayer.model.Song;

/**
 * Created by thuy on 29/04/2018.
 */
public interface IPlayer {
    void startSong();
    void pauseSong();
    void resumeSong();
    void stopSong();
}
