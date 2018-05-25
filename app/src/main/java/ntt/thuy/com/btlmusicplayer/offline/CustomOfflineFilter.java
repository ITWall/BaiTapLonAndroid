package ntt.thuy.com.btlmusicplayer.offline;

import android.widget.Filter;

import java.util.ArrayList;

import ntt.thuy.com.btlmusicplayer.entity.OfflineSong;

/**
 * Created by thuy on 26/05/2018.
 */
public class CustomOfflineFilter extends Filter {
    SongAdapter adapter;
    ArrayList<OfflineSong> filterList;

    public CustomOfflineFilter(SongAdapter adapter, ArrayList<OfflineSong> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();
        if(charSequence!=null && charSequence.length()>0){
            charSequence = charSequence.toString().toUpperCase();
            ArrayList<OfflineSong> list = new ArrayList<>();
            for(OfflineSong song : filterList){
                if(song.getTitle().toUpperCase().contains(charSequence)){
                    list.add(song);
                }
            }
            results.count = list.size();
            results.values = list;
        } else {
            results.count = filterList.size();
            results.values= filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.setListSong((ArrayList<OfflineSong>)filterResults.values);
        adapter.notifyDataSetChanged();
    }
}
