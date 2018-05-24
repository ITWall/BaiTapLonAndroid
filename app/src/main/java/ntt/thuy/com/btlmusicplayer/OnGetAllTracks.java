package ntt.thuy.com.btlmusicplayer;

import java.util.List;

import ntt.thuy.com.btlmusicplayer.entity.OnlineSong;

public interface OnGetAllTracks {
    void onSuccess(List<OnlineSong> listOnlineSong);
    void onFailed();
}
