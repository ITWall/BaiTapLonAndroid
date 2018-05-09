package ntt.thuy.com.btlmusicplayer;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements SongAdapter.OnItemClickListener, View.OnClickListener {

    private static final int MY_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 100;
    private RecyclerView rvSong;
    private SongAdapter songAdapter;
    private SongManager songManager;

    private int pos = -1;

    private IPlayer songPlayer;

    ImageView ivPause, ivBack, ivNext;

//    private ServiceConnection serviceConnection;
//    private boolean isConnected;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        checkPermission();
//        connectService();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);
            } else {
                songAdapter = new SongAdapter(this, songManager.getAllSong(this));
                songAdapter.setOnItemListener(this);

                rvSong.setAdapter(songAdapter);
            }
        }
    }

//    private void connectService() {
//        serviceConnection = new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//                songPlayer = ((SongPlayer.ServiceBinder) service).getService();
//                Log.i("CONNECTED", "TRUE");
//                isConnected = true;
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//                isConnected = false;
//            }
//        };
//
//        Intent intent = new Intent(this, SongPlayer.class);
//        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
//    }

    private void disconnectService() {
//        unbindService(serviceConnection);
    }

    @Override
    protected void onDestroy() {
//        disconnectService();
        super.onDestroy();
    }

    private void initView() {
        ivPause = (ImageView) findViewById(R.id.bt_pause);
        ivBack = (ImageView) findViewById(R.id.bt_back);
        ivNext = (ImageView) findViewById(R.id.bt_next);

        ivPause.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivNext.setOnClickListener(this);

        rvSong = (RecyclerView) findViewById(R.id.rv_song);
        rvSong.setLayoutManager(new LinearLayoutManager(this));

        songManager = new SongManager();


        songPlayer = new SongPlayer(this);

    }

    @Override
    public void onItemClicked(int position) {
//        if (!isConnected) {
//            Log.i("CONNECTED", "FALSE");
//            return;
//        }
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
                Toast.makeText(this, "Write external storage permission granted!", Toast.LENGTH_SHORT).show();
                songAdapter = new SongAdapter(this, songManager.getAllSong(this));
                songAdapter.setOnItemListener(this);

                rvSong.setAdapter(songAdapter);
            } else {
                Toast.makeText(this, "Write external storage permission denied!", Toast.LENGTH_SHORT).show();
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
