package ntt.thuy.com.btlmusicplayer.online;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import java.util.ArrayList;
import java.util.List;
import ntt.thuy.com.btlmusicplayer.IPlayer;
import ntt.thuy.com.btlmusicplayer.OnGetAllTracks;
import ntt.thuy.com.btlmusicplayer.R;
import ntt.thuy.com.btlmusicplayer.controller.OnlineController;
import ntt.thuy.com.btlmusicplayer.entity.Track;
import ntt.thuy.com.btlmusicplayer.model.TrackModel;

/**
 * Created by nguyen.thi.thu.thuy on 5/11/18.
 */

public class OnlineFragment extends Fragment implements OnGetAllTracks, TrackListAdapter.OnItemClick, View.OnClickListener{

    private View view;
    private List<Track> list;
    private RecyclerView trackList;
    private TrackListAdapter adapter;

    private RelativeLayout relativeLayout;
    private TextView mSelectedTrackTitle;
    private ImageView mSelectedTrackImage;
    private ImageView pause;

    private IPlayer songPlayer;
    private MediaPlayer mMediaPlayer;

    private Animation anim;
    private SeekBar seekBar;
    private Handler mHandler = new Handler();
    private Runnable runnable;

    private TextView duration;
    private TextView current;
    private TableRow tableRow;

    private int pos = -1;

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

        seekBar = (SeekBar) view.findViewById(R.id.seek_bar);
        seekBar.setVisibility(View.GONE);

        tableRow = (TableRow) view.findViewById(R.id.table_row);
        tableRow.setVisibility(View.GONE);

        relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_layout);
        relativeLayout.setVisibility(View.GONE);
//        relativeLayout.setOnClickListener(this);

        mSelectedTrackTitle = (TextView) view.findViewById(R.id.selected_track_title);
        mSelectedTrackImage = (ImageView) view.findViewById(R.id.selected_track_image);

        pause = (ImageView) view.findViewById(R.id.pause);
        ImageView next = (ImageView) view.findViewById(R.id.next);
        ImageView previous = (ImageView) view.findViewById(R.id.previous);

        duration = (TextView) view.findViewById(R.id.text_duration);
        current = (TextView) view.findViewById(R.id.text_current);

        seekBar.setMax(0);
        current.setText(secondsToString(0));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (mMediaPlayer != null && fromUser) {
                    mMediaPlayer.seekTo(progress * 1000);
                }
            }
        });

        pause.setOnClickListener(this);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);

        songPlayer = new OnlineSongPlayer(getContext(), new OnlineSongPlayer.MediaPlayerMonitoring() {
            @Override
            public void onCompletion() {
                Log.d("TEST", "on completion");

                pos++;
                adapter.setSelectedPos(pos);
                adapter.notifyDataSetChanged();

                Track track = list.get(pos);
                showBottomBar(track);

                songPlayer.startSong(track.streamUrl);
                anim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
                mSelectedTrackImage.startAnimation(anim);

            }

            @Override
            public void startNewThread() {
                final Track track = list.get(pos);
                seekBar.setMax(track.duration/1000);
                seekBar.setProgress(0);
                duration.setText(secondsToString(track.duration/1000));
                current.setText(secondsToString(0));

                runnable = new Runnable() {
                    @Override
                    public void run() {
                        mMediaPlayer = ((OnlineSongPlayer) songPlayer).getMediaPlayer();
                        if (mMediaPlayer != null) {
                            if (seekBar.getProgress() == seekBar.getMax()) {
                                pause.setImageResource(R.mipmap.baseline_play_arrow_white_36dp);
                                return;
                            }
                            int mCurrentPosition = mMediaPlayer.getCurrentPosition()/1000;
                            Log.d("TEST", mCurrentPosition + "s");
                            current.setText(secondsToString(mCurrentPosition));
                            seekBar.setProgress(mCurrentPosition);
                        }
                        mHandler.postDelayed(runnable, 1000);
                    }
                };
                mHandler.postDelayed(runnable, 1000);
            }
        });

//        setupMediaPlayer();
    }

//    private void setupMediaPlayer(){
//        mMediaPlayer = new MediaPlayer();
//        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                progress.dismiss();
//
//                anim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
//                mSelectedTrackImage.startAnimation(anim);
//
//                togglePlayPause();
//            }
//        });
//    }
//
//    private void togglePlayPause() {
//        if (mMediaPlayer.isPlaying()) {
//            mMediaPlayer.pause();
//        } else {
//            mMediaPlayer.start();
//        }
//    }

    private void showBottomBar(Track track) {
        seekBar.setVisibility(View.VISIBLE);
        tableRow.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);
        mSelectedTrackTitle.setText(track.title);

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
        songPlayer.releaseMediaPlayer();
        mHandler.removeCallbacks(runnable);
    }

    @Override
    public void onSuccess(List<Track> listTrack) {
        this.list = listTrack;
        loadTracks();
    }

    @Override
    public void onFailed() {
        Toast.makeText(getContext(), "Đã có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position) {
        pause.setImageResource(R.mipmap.baseline_pause_white_36dp);
        pos = position;
        adapter.setSelectedPos(position);
        adapter.notifyDataSetChanged();

        Track track = list.get(position);
        showBottomBar(track);

        songPlayer.startSong(track.streamUrl);
        anim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
        mSelectedTrackImage.startAnimation(anim);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.selected_track_image:
//                ((MainActivity)getActivity()).showDetailTrackFragment(selectedTrack);
                break;
            case R.id.pause:
                if (pos == -1 || ((OnlineSongPlayer) songPlayer).getState() == OnlineSongPlayer.STATE_IDLE) {
                    pause.setImageResource(R.mipmap.baseline_pause_white_36dp);
                    songPlayer.startSong(list.get(0).streamUrl);

                    pos++;
                } else {
                    if (((OnlineSongPlayer) songPlayer).getState() == OnlineSongPlayer.STATE_PLAYING) {
                        pause.setImageResource(R.mipmap.baseline_play_arrow_white_36dp);
                        songPlayer.pauseSong();
//                        mSelectedTrackImage.clearAnimation();
                    } else {
                        if (((OnlineSongPlayer) songPlayer).getState() == OnlineSongPlayer.STATE_PAUSED) {
                            pause.setImageResource(R.mipmap.baseline_pause_white_36dp);
                            songPlayer.resumeSong();
                        }
                    }
                }

                break;
            case R.id.next:
                pause.setImageResource(R.mipmap.baseline_pause_white_36dp);
                if (pos == -1) {
                    songPlayer.startSong(list.get(0).streamUrl);
                    pos++;
                } else {
                    if (((OnlineSongPlayer) songPlayer).getState() != OnlineSongPlayer.STATE_IDLE) {
                        songPlayer.stopSong();
                    }
                    pos++;
                    if (pos >= list.size()) {
                        pos = 0;
                    }
                    songPlayer.startSong(list.get(pos).streamUrl);
                }
                break;
            case R.id.previous:
                pause.setImageResource(R.mipmap.baseline_pause_white_36dp);
                int lastIndex = list.size() - 1;
                if (pos == -1) {
                    songPlayer.startSong(list.get(lastIndex).streamUrl);
                    pos = lastIndex;
                } else {
                    if (((OnlineSongPlayer) songPlayer).getState() != OnlineSongPlayer.STATE_IDLE) {
                        songPlayer.stopSong();
                    }
                    pos--;
                    if (pos < 0) {
                        pos = lastIndex;
                    }

                    songPlayer.startSong(list.get(pos).streamUrl);
                }
                break;
            default:
        }

        adapter.setSelectedPos(pos);
        adapter.notifyDataSetChanged();

        Track track = list.get(pos);
        showBottomBar(track);

        anim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
        mSelectedTrackImage.startAnimation(anim);
    }

    private String secondsToString(int pTime) {
        return String.format("%02d:%02d", pTime / 60, pTime % 60);
    }
}
