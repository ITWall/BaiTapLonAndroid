package ntt.thuy.com.btlmusicplayer.controller;
import java.util.List;

import ntt.thuy.com.btlmusicplayer.OnGetAllTracks;
import ntt.thuy.com.btlmusicplayer.entity.Track;
import ntt.thuy.com.btlmusicplayer.model.TrackModel;


public class OnlineController{
    private TrackModel trackModel;
    private OnGetAllTracks onGetAllTracks;

    public OnGetAllTracks getOnGetAllTracks() {
        return onGetAllTracks;
    }

    public void setOnGetAllTracks(OnGetAllTracks onGetAllTracks) {
        this.onGetAllTracks = onGetAllTracks;
    }

    public OnlineController() {
    }

    public TrackModel getTrackModel() {
        return trackModel;
    }

    public void setTrackModel(TrackModel trackModel) {
        this.trackModel = trackModel;
    }

    public List<Track> getAllTracks(){
        return trackModel.getAllTracks();
    }


}
