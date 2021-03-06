package ntt.thuy.com.btlmusicplayer.offline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ntt.thuy.com.btlmusicplayer.R;
import ntt.thuy.com.btlmusicplayer.entity.OfflineSong;

/**
 * Created by thuy on 29/04/2018.
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> implements Filterable{
    private List<OfflineSong> listSong,filterList;
    private Context mContext;

    private OnItemClickListener listener;

    int selectedPos = -1;


    CustomOfflineFilter filter;

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
        notifyDataSetChanged();
    }

    public void setOnItemListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public SongAdapter(Context context, List<OfflineSong> listSong){
        mContext = context;
        this.listSong = listSong;
        this.filterList = listSong;
    }

    public void setListSong(List<OfflineSong> listSong) {
        this.listSong = listSong;
        this.filterList = listSong;
        notifyDataSetChanged();
    }

    @Override
    public SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemSong = View.inflate(mContext, R.layout.item_song_offline,null);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.WRAP_CONTENT);
        params.setMargins(8,8,8,8);
        itemSong.setLayoutParams(params);
        return new SongHolder(itemSong);
    }

    @Override
    public void onBindViewHolder(SongHolder holder, int position) {
        final SongHolder songHolder = (SongHolder) holder;
        OfflineSong song = listSong.get(position);
        songHolder.tvTitle.setText(song.getTitle());
        songHolder.tvAuthor.setText(song.getAuthor());

        songHolder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                listener.onItemClicked(songHolder.getAdapterPosition());
            }
        });

        if(selectedPos == position) {
            songHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.background_selected_item));
        } else songHolder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.background_normal_item));
    }

    @Override
    public int getItemCount() {
        return listSong.size();
    }

    public OfflineSong getItem(int position){
        return listSong.get(position);
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new CustomOfflineFilter(this, (ArrayList<OfflineSong>) filterList);
        }
        return filter;
    }

    public class SongHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvAuthor;

        public SongHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
        }
    }
    public interface OnItemClickListener{
        void onItemClicked(int position);
    }
}
