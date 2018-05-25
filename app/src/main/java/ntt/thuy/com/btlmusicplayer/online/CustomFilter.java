package ntt.thuy.com.btlmusicplayer.online;

import android.widget.Filter;

import java.util.ArrayList;

import ntt.thuy.com.btlmusicplayer.entity.OnlineSong;

/**
 * Created by thuy on 25/05/2018.
 */
public class CustomFilter extends Filter {
    TrackListAdapter adapter;
    ArrayList<OnlineSong> filterList;

    public CustomFilter(TrackListAdapter adapter, ArrayList<OnlineSong> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();
        if(charSequence!=null && charSequence.length()>0){
            charSequence = charSequence.toString().toUpperCase();
            ArrayList<OnlineSong> list = new ArrayList<>();
            for(OnlineSong song : filterList){
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
        adapter.setList((ArrayList<OnlineSong>)filterResults.values);
        adapter.notifyDataSetChanged();
    }
}
