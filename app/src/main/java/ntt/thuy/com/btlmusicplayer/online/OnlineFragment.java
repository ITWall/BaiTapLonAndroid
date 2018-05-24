package ntt.thuy.com.btlmusicplayer.online;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ntt.thuy.com.btlmusicplayer.Config;
import ntt.thuy.com.btlmusicplayer.MainActivity;
import ntt.thuy.com.btlmusicplayer.OnGetAllTracks;
import ntt.thuy.com.btlmusicplayer.R;
import ntt.thuy.com.btlmusicplayer.controller.OnlineController;
import ntt.thuy.com.btlmusicplayer.entity.OnlineSong;
import ntt.thuy.com.btlmusicplayer.model.TrackModel;

/**
 * Created by nguyen.thi.thu.thuy on 5/11/18.
 */

public class OnlineFragment extends Fragment implements OnGetAllTracks, TrackListAdapter.OnItemClick, View.OnClickListener{

    private View view;
    private List<OnlineSong> list;
    private RecyclerView trackList;
    private TrackListAdapter adapter;

    private View devide;
    private RelativeLayout relativeLayout;
    private TextView mSelectedTrackTitle;
    private ImageView mSelectedTrackImage;
    private ImageView previous, next, pause;

    private MediaPlayer mMediaPlayer;
    private ProgressDialog progress;
    private Animation anim;
    private OnlineSong selectedOnlineSong;

    private OnlineController onlineController;
    public OnlineFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_online, container, false);

        onlineController = new OnlineController();
        TrackModel trackModel = new TrackModel();
        trackModel.setOnGetAllTracks(this);
        onlineController.setTrackModel(trackModel);

        onlineController.getAllTracks();
        init();

        return view;
    }

    public void init() {
        trackList = (RecyclerView) view.findViewById(R.id.track_list);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        trackList.setLayoutManager(llm);

        adapter = new TrackListAdapter(getContext(), this);
        trackList.setAdapter(adapter);
        list = new ArrayList<>();

        devide = (View) view.findViewById(R.id.view_devide);
        devide.setVisibility(View.GONE);

        relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_layout);
        relativeLayout.setVisibility(View.GONE);
        relativeLayout.setOnClickListener(this);

        mSelectedTrackTitle = (TextView) view.findViewById(R.id.selected_track_title);
        mSelectedTrackImage = (ImageView) view.findViewById(R.id.selected_track_image);

        setupMediaPlayer();
    }

    private void setupMediaPlayer(){
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progress.dismiss();

                anim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
                mSelectedTrackImage.startAnimation(anim);

                togglePlayPause();
            }
        });
    }

    private void togglePlayPause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        } else {
            mMediaPlayer.start();
        }
    }

    private void showBottomBar(OnlineSong onlineSong) {
        devide.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);

        mSelectedTrackTitle.setText(onlineSong.getTitle());

//        Glide v4:
//        Glide.with(context).load(url).apply(RequestOptions.circleCropTransform()).into(imageView);

//        Glide v3:
        if (onlineSong.artworkUrl != null) {
            Glide.with(getActivity()).load(onlineSong.artworkUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(mSelectedTrackImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    super.setResource(resource);
                    RoundedBitmapDrawable bitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                    bitmapDrawable.setCircular(true);
                    mSelectedTrackImage.setImageDrawable(bitmapDrawable);
                }
            });
        } else {
            Glide.with(getActivity()).load(R.mipmap.music_image).asBitmap().centerCrop().into(new BitmapImageViewTarget(mSelectedTrackImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    super.setResource(resource);
                    RoundedBitmapDrawable bitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                    bitmapDrawable.setCircular(true);
                    mSelectedTrackImage.setImageDrawable(bitmapDrawable);
                }
            });
        }
    }

    public void loadTracks() {
        adapter.setList(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onSuccess(List<OnlineSong> listOnlineSong) {
        this.list = listOnlineSong;
        loadTracks();
    }

    @Override
    public void onFailed() {
        Toast.makeText(getContext(), "Đã có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position) {
        adapter.setSelectedPos(position);
        adapter.notifyDataSetChanged();

        progress = new ProgressDialog(getContext());
        progress.setMessage("Please wait while loading song...");
        progress.setCancelable(false);
        progress.show();

        OnlineSong onlineSong = list.get(position);
        selectedOnlineSong = onlineSong;
        showBottomBar(onlineSong);

        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }

        try {
            mMediaPlayer.setDataSource(onlineSong.streamUrl + "?client_id=" + Config.CLIENT_ID);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.relative_layout:
                Log.d("TEST", selectedOnlineSong.getTitle());
                ((MainActivity)getActivity()).showDetailTrackFragment(selectedOnlineSong);
                break;
        }
    }
}
