package ntt.thuy.com.btlmusicplayer.offline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ntt.thuy.com.btlmusicplayer.R;
import ntt.thuy.com.btlmusicplayer.entity.OfflineSong;

/**
 * Created by thuy on 29/04/2018.
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder>{
    private List<OfflineSong> listSong;
    private Context mContext;

    private OnItemClickListener listener;

    public void setOnItemListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public SongAdapter(Context context, List<OfflineSong> listSong){
        mContext = context;
        this.listSong = listSong;
    }

    public void setListSong(List<OfflineSong> listSong) {
        this.listSong = listSong;
        notifyDataSetChanged();
    }

    @Override
    public SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemSong = View.inflate(mContext, R.layout.item_song,null);
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
    }

    @Override
    public int getItemCount() {
        return listSong.size();
    }

    public OfflineSong getItem(int position){
        return listSong.get(position);
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
