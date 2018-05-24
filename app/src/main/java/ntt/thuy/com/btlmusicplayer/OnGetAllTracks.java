package ntt.thuy.com.btlmusicplayer;

import java.util.List;

import ntt.thuy.com.btlmusicplayer.entity.Track;

public interface OnGetAllTracks {
    void onSuccess(List<Track> listTrack);
    void onFailed();
}
