package ntt.thuy.com.btlmusicplayer.offline;


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

import java.util.List;

import ntt.thuy.com.btlmusicplayer.IPlayer;
import ntt.thuy.com.btlmusicplayer.R;
import ntt.thuy.com.btlmusicplayer.controller.OfflineController;
import ntt.thuy.com.btlmusicplayer.entity.OfflineSong;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfflineFragment extends Fragment implements SongAdapter.OnItemClickListener, View.OnClickListener{
private View view;

    private RecyclerView rvSong;
    private SongAdapter songAdapter;
    private OfflineController controller;
    private List<OfflineSong> list;

    private int pos = -1;
    private IPlayer songPlayer;
    ImageView ivPause, ivBack, ivNext;

    public OfflineFragment() {
        // Required empty public constructor
    }

    public List<OfflineSong> getList() {
        return list;
    }

    public void setList(List<OfflineSong> list) {
        this.list = list;
        songAdapter.setListSong(this.list);
    }

    public SongAdapter getSongAdapter() {
        return songAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_offline, container, false);
        controller = new OfflineController();
        controller.setFragment(this);
        controller.setSongManager(new SongManager());
        controller.setSongPlayer(new OfflineSongPlayer(getContext()));

        songPlayer = controller.getSongPlayer();

        initView();
        return view;
    }


    private void initView() {
        ivPause = (ImageView) view.findViewById(R.id.bt_pause);
        ivBack = (ImageView) view.findViewById(R.id.bt_back);
        ivNext = (ImageView) view.findViewById(R.id.bt_next);
        rvSong = (RecyclerView) view.findViewById(R.id.rv_song);

        ivPause.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivNext.setOnClickListener(this);

        if(controller.isWriteExternalStoragePermissionGranted()){
            setupListSong();
        }else {
            controller.askPermissions();
        }
    }

    private void setupListSong(){
        list = controller.getSongManager().getAllSong(getContext());
        rvSong.setLayoutManager(new LinearLayoutManager(getContext()));
        songAdapter = new SongAdapter(getContext(), list);
        songAdapter.setOnItemListener(this);
        rvSong.setAdapter(songAdapter);
    }

    @Override
    public void onItemClicked(int position) {
        ivPause.setImageResource(R.mipmap.baseline_pause_white_36dp);
        pos = position;
songAdapter.setSelectedPos(pos);
        final OfflineSong song = songAdapter.getItem(position);
        int state = ((OfflineSongPlayer) songPlayer).getState();
        ((OfflineSongPlayer) songPlayer).setSong(song);
        if (state != OfflineSongPlayer.STATE_IDLE) {

            songPlayer.stopSong();
        }
        songPlayer.startSong();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == controller.MY_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE) {
            controller.setGrantResults(grantResults);
            if (controller.isPermissionAllowed()) {
                Toast.makeText(getContext(), "Write external storage permission granted!", Toast.LENGTH_SHORT).show();
                setupListSong();
            } else {
                Toast.makeText(getContext(), "Write external storage permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_pause:
                if(((OfflineSongPlayer) songPlayer).isPlaying()) {
                    songPlayer.pauseSong();
                    ivPause.setImageResource(R.mipmap.baseline_play_arrow_white_36dp);
                } else if (((OfflineSongPlayer) songPlayer).isPaused()){
                    songPlayer.resumeSong();
                    ivPause.setImageResource(R.mipmap.baseline_pause_white_36dp);
                }
                break;

            case R.id.bt_next:
                pos = pos+1;
                if(isLastSong()) pos=0;
                final OfflineSong songNext = songAdapter.getItem(pos);

                if (!((OfflineSongPlayer) songPlayer).isIDLE()) {
                    songPlayer.stopSong();
                }
                ((OfflineSongPlayer) songPlayer).setSong(songNext);
                songPlayer.startSong();
                break;

            case R.id.bt_back:
                if(isFirstSong()){
                    pos=songAdapter.getItemCount()-1;
                } else{
                    pos = pos-1;
                }
                final OfflineSong songBack = songAdapter.getItem(pos);

                if (!((OfflineSongPlayer) songPlayer).isIDLE()) {
                    songPlayer.stopSong();
                }
                ((OfflineSongPlayer) songPlayer).setSong(songBack);
                songPlayer.startSong();
                break;
        }
        songAdapter.setSelectedPos(pos);
    }

    private boolean isLastSong(){
        return pos >= songAdapter.getItemCount();
    }

    private boolean isFirstSong(){
        return pos == 0;
    }

}
