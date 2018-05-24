package ntt.thuy.com.btlmusicplayer.online;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ntt.thuy.com.btlmusicplayer.R;
import ntt.thuy.com.btlmusicplayer.entity.Track;

/**
 * Created by nguyen.thi.thu.thuy on 5/11/18.
 */

public class DetailTrackFragment extends Fragment {

    private Track track;
    private TextView title, createdAt, userName, lastModified;
    private View view;

    public DetailTrackFragment setTrack(Track track) {
        this.track = track;
        return this;
    }

    public static DetailTrackFragment newInstance(Track track){
        return new DetailTrackFragment().setTrack(track);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_track, container, false);
        init();
        return view;
    }

    public void init() {
        title = (TextView) view.findViewById(R.id.track_title);
        createdAt = (TextView) view.findViewById(R.id.created_at);
        userName = (TextView) view.findViewById(R.id.user_name);
        lastModified = (TextView) view.findViewById(R.id.last_modified);

//        title.setText(track.title);
        createdAt.setText(track.createdAt);
        userName.setText(track.user.username);
        lastModified.setText(track.user.lastModified);
    }
}
