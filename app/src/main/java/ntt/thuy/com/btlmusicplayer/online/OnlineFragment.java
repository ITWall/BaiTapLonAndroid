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
import ntt.thuy.com.btlmusicplayer.data.APIUtils;
import ntt.thuy.com.btlmusicplayer.model.Track;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ntt.thuy.com.btlmusicplayer.R;
/**
 * Created by nguyen.thi.thu.thuy on 5/11/18.
 */

public class OnlineFragment extends Fragment {

    private View view;

    public OnlineFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_online, container, false);
        init();

        APIUtils.getApiInterface().getRecentTracks("last_week", 20, Config.PUBLIC_FILTER).enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                list.addAll(response.body());
                loadTracks();
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {
                Toast.makeText(getContext(), "Đã có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    List<Track> list;
    RecyclerView trackList;
    TrackListAdapter adapter;

    private View devide;
    private RelativeLayout relativeLayout;
    private TextView mSelectedTrackTitle;
    private ImageView mSelectedTrackImage;
    private ImageView previous, next, pause;

    private MediaPlayer mMediaPlayer;
    private ProgressDialog progress;
    private Animation anim;
    private Track selectedTrack;



    public void init() {
        trackList = (RecyclerView) view.findViewById(R.id.track_list);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        trackList.setLayoutManager(llm);
        adapter = new TrackListAdapter(getContext(), new TrackListAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position) {
                adapter.setSelectedPos(position);
                adapter.notifyDataSetChanged();

                progress = new ProgressDialog(getContext());
                progress.setMessage("Please wait while loading song...");
                progress.setCancelable(false);
                progress.show();

                Track track = list.get(position);
                selectedTrack = track;
                showBottomBar(track);

                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                    mMediaPlayer.reset();
                }

                try {
                    mMediaPlayer.setDataSource(track.streamUrl + "?client_id=" + Config.CLIENT_ID);
                    mMediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        trackList.setAdapter(adapter);
        list = new ArrayList<>();

        devide = (View) view.findViewById(R.id.view_devide);
        devide.setVisibility(View.GONE);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_layout);
        relativeLayout.setVisibility(View.GONE);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TEST", selectedTrack.title);
                ((MainActivity)getActivity()).showDetailTrackFragment(selectedTrack);
            }
        });

        mSelectedTrackTitle = (TextView) view.findViewById(R.id.selected_track_title);
        mSelectedTrackImage = (ImageView) view.findViewById(R.id.selected_track_image);

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

    private void showBottomBar(Track track) {
        devide.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);

        mSelectedTrackTitle.setText(track.title);

//        Glide v4:
//        Glide.with(context).load(url).apply(RequestOptions.circleCropTransform()).into(imageView);

//        Glide v3:
        if (track.artworkUrl != null) {
            Glide.with(getActivity()).load(track.artworkUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(mSelectedTrackImage) {
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
}
