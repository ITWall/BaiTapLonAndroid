package ntt.thuy.com.btlmusicplayer.offline;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import ntt.thuy.com.btlmusicplayer.R;
import ntt.thuy.com.btlmusicplayer.model.Song;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfflineFragment extends Fragment implements SongAdapter.OnItemClickListener, View.OnClickListener{
private View view;
    private static final int MY_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 100;
    private RecyclerView rvSong;
    private SongAdapter songAdapter;
    private SongManager songManager;

    private int pos = -1;

    private IPlayer songPlayer;

    ImageView ivPause, ivBack, ivNext;

    public OfflineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_offline, container, false);
        initView();
        checkPermission();
        return view;
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);
            } else {
                songAdapter = new SongAdapter(getContext(), songManager.getAllSong(getContext()));
                songAdapter.setOnItemListener(this);

                rvSong.setAdapter(songAdapter);
            }
        }
    }

    private void initView() {
        ivPause = (ImageView) view.findViewById(R.id.bt_pause);
        ivBack = (ImageView) view.findViewById(R.id.bt_back);
        ivNext = (ImageView) view.findViewById(R.id.bt_next);

        ivPause.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivNext.setOnClickListener(this);

        rvSong = (RecyclerView) view.findViewById(R.id.rv_song);
        rvSong.setLayoutManager(new LinearLayoutManager(getContext()));

        songManager = new SongManager();


        songPlayer = new SongPlayer(getContext());

    }

    @Override
    public void onItemClicked(int position) {
        ivPause.setImageResource(R.mipmap.pause);
        pos = position;
        final Song song = songAdapter.getItem(position);


        int state = ((SongPlayer) songPlayer).getState();
        if (state != SongPlayer.STATE_IDLE) {
            songPlayer.stopSong();
        }
        songPlayer.startSong(song.getId());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Write external storage permission granted!", Toast.LENGTH_SHORT).show();
                songAdapter = new SongAdapter(getContext(), songManager.getAllSong(getContext()));
                songAdapter.setOnItemListener(this);

                rvSong.setAdapter(songAdapter);
            } else {
                Toast.makeText(getContext(), "Write external storage permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        int state = ((SongPlayer) songPlayer).getState();
        switch (view.getId()){

            case R.id.bt_pause:
                if(state == SongPlayer.STATE_PLAYING) {
                    songPlayer.pauseSong();
                    ivPause.setImageResource(R.mipmap.play);
                } else if (state == SongPlayer.STATE_PAUSED){
                    songPlayer.resumeSong();
                    ivPause.setImageResource(R.mipmap.pause);
                }
                break;
            case R.id.bt_next:
                pos = pos+1;
                if(pos>=songAdapter.getItemCount()) pos=0;
                final Song songNext = songAdapter.getItem(pos);

                if (state != SongPlayer.STATE_IDLE) {
                    songPlayer.stopSong();
                }
                songPlayer.startSong(songNext.getId());
                break;
            case R.id.bt_back:
                pos = pos-1;
                if(pos<0) pos=songAdapter.getItemCount()-1;
                final Song songBack = songAdapter.getItem(pos);

                if (state != SongPlayer.STATE_IDLE) {
                    songPlayer.stopSong();
                }
                songPlayer.startSong(songBack.getId());
                break;
        }
    }

}
