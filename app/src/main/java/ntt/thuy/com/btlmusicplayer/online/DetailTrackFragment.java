package ntt.thuy.com.btlmusicplayer.online;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ntt.thuy.com.btlmusicplayer.R;
import ntt.thuy.com.btlmusicplayer.entity.OnlineSong;

/**
 * Created by nguyen.thi.thu.thuy on 5/11/18.
 */

public class DetailTrackFragment extends Fragment {

    private OnlineSong onlineSong;
    private TextView title, createdAt, userName, lastModified;
    private View view;

    public DetailTrackFragment setOnlineSong(OnlineSong onlineSong) {
        this.onlineSong = onlineSong;
        return this;
    }

    public static DetailTrackFragment newInstance(OnlineSong onlineSong){
        return new DetailTrackFragment().setOnlineSong(onlineSong);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_track, container, false);
        init();
        return view;
    }

    public void init() {
        title = (TextView) view.findViewById(R.id.title);
        createdAt = (TextView) view.findViewById(R.id.created_at);
        userName = (TextView) view.findViewById(R.id.user_name);
        lastModified = (TextView) view.findViewById(R.id.last_modified);

        Log.d("TEST", onlineSong.getTitle());

        title.setText(onlineSong.getTitle());
        createdAt.setText(onlineSong.createdAt);
        userName.setText(onlineSong.user.username);
        lastModified.setText(onlineSong.user.lastModified);
    }
}
