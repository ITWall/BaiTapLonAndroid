package ntt.thuy.com.btlmusicplayer.model;

import java.util.ArrayList;
import java.util.List;

import ntt.thuy.com.btlmusicplayer.Config;
import ntt.thuy.com.btlmusicplayer.OnGetAllTracks;
import ntt.thuy.com.btlmusicplayer.data.APIUtils;
import ntt.thuy.com.btlmusicplayer.entity.OnlineSong;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackModel {
    private List<OnlineSong> listOnlineSong;
    private OnGetAllTracks onGetAllTracks;

    public OnGetAllTracks getOnGetAllTracks() {
        return onGetAllTracks;
    }

    public void setOnGetAllTracks(OnGetAllTracks onGetAllTracks) {
        this.onGetAllTracks = onGetAllTracks;
    }

    public List<OnlineSong> getAllTracks(){
        listOnlineSong = new ArrayList<>();
        APIUtils.getApiInterface().getRecentTracks("last_week", 20, Config.PUBLIC_FILTER).enqueue(new Callback<List<OnlineSong>>() {
            @Override
            public void onResponse(Call<List<OnlineSong>> call, Response<List<OnlineSong>> response) {
                listOnlineSong.addAll(response.body());
                onGetAllTracks.onSuccess(listOnlineSong);
            }

            @Override
            public void onFailure(Call<List<OnlineSong>> call, Throwable t) {
                System.out.println("error when get tracks: " + t.getMessage());
                onGetAllTracks.onFailed();
            }
        });
        return listOnlineSong;
    }
}
