package ntt.thuy.com.btlmusicplayer.controller;

import android.Manifest;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;

import ntt.thuy.com.btlmusicplayer.offline.IPlayer;
import ntt.thuy.com.btlmusicplayer.offline.SongManager;

public class OfflineController {

    public static final int MY_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 100;
    private Fragment fragment;
    private SongManager songManager;
    private IPlayer songPlayer;

    public IPlayer getSongPlayer() {
        return songPlayer;
    }

    public void setSongPlayer(IPlayer songPlayer) {
        this.songPlayer = songPlayer;
    }

    public SongManager getSongManager() {
        return songManager;
    }

    public void setSongManager(SongManager songManager) {
        this.songManager = songManager;
    }

    public int[] getGrantResults() {
        return grantResults;
    }

    public void setGrantResults(int[] grantResults) {
        this.grantResults = grantResults;
    }

    private int[] grantResults;

    public OfflineController() {
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public OfflineController(Fragment fragment) {

        this.fragment = fragment;
    }

    public void askPermissions() {
        fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);
    }

    public boolean isWriteExternalStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return fragment.getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public boolean isPermissionAllowed(){
        return grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }
}
