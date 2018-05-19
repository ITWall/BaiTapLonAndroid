package ntt.thuy.com.btlmusicplayer.online;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import ntt.thuy.com.btlmusicplayer.R;
import ntt.thuy.com.btlmusicplayer.model.Track;

/**
 * Created by nguyen.thi.thu.thuy on 5/11/18.
 */

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.TrackHolder> {

    List<Track> list;
    Context mContext;
    OnItemClick listener;
    int selectedPos = -1;

    public TrackListAdapter(Context mContext, OnItemClick listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    public void setList(List<Track> list) {
        this.list = list;
    }

    @Override
    public TrackHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_list_row, parent, false);
        return new TrackHolder(view);
    }

    @Override
    public void onBindViewHolder(TrackHolder holder, int position) {
        holder.bindData(position, list.get(position));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class TrackHolder extends RecyclerView.ViewHolder {
        private ImageView trackImage;
        private TextView trackTitle;

        public TrackHolder(View itemView) {
            super(itemView);
            trackImage = (ImageView) itemView.findViewById(R.id.track_image);
            trackTitle = (TextView) itemView.findViewById(R.id.track_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }

        public void bindData(int position, Track track){

            if (track.artworkUrl != null) {
                Picasso.with(mContext).load(track.artworkUrl).error(R.mipmap.music_image).into(trackImage);
            } else {
                trackImage.setImageResource(R.mipmap.music_image);
            }
            trackTitle.setText(track.title);

            if(selectedPos == position) {
                itemView.setBackgroundColor(mContext.getResources().getColor(R.color.background_selected_item));
            }
        }
    }

    interface OnItemClick{
        void onItemClick(int position);
    }
}
