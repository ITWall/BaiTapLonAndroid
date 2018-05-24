package ntt.thuy.com.btlmusicplayer.model;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ntt.thuy.com.btlmusicplayer.Config;
import ntt.thuy.com.btlmusicplayer.OnGetAllTracks;
import ntt.thuy.com.btlmusicplayer.data.APIUtils;
import ntt.thuy.com.btlmusicplayer.entity.Track;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackModel {
    private List<Track> listTrack;
    private OnGetAllTracks onGetAllTracks;

    public OnGetAllTracks getOnGetAllTracks() {
        return onGetAllTracks;
    }

    public void setOnGetAllTracks(OnGetAllTracks onGetAllTracks) {
        this.onGetAllTracks = onGetAllTracks;
    }

    public List<Track> getAllTracks(){
        listTrack = new ArrayList<>();
        APIUtils.getApiInterface().getRecentTracks("last_week", 20, Config.PUBLIC_FILTER).enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                listTrack.addAll(response.body());
                onGetAllTracks.onSuccess(listTrack);
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                System.out.println("error when get tracks: " + t.getMessage());
                onGetAllTracks.onFailed();
            }
        });
        return listTrack;
    }
}
