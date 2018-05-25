package ntt.thuy.com.btlmusicplayer.online;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import ntt.thuy.com.btlmusicplayer.R;
import ntt.thuy.com.btlmusicplayer.entity.OnlineSong;


/**
 * Created by nguyen.thi.thu.thuy on 5/11/18.
 */

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.TrackHolder> implements Filterable {

    List<OnlineSong> list,filterList;
    Context mContext;
    OnItemClick listener;
    int selectedPos = -1;

    CustomFilter filter;

    public TrackListAdapter(Context mContext, OnItemClick listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    public void setList(List<OnlineSong> list) {
        this.list = list;
        this.filterList = list;
        notifyDataSetChanged();
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

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new CustomFilter(this, (ArrayList<OnlineSong>) filterList);
        }
        return filter;
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

        public void bindData(int position, OnlineSong onlineSong){

            if (onlineSong.artworkUrl != null) {
                Picasso.with(mContext).load(onlineSong.artworkUrl).error(R.mipmap.music_image).into(trackImage);
            } else {
                trackImage.setImageResource(R.mipmap.music_image);
            }
            trackTitle.setText(onlineSong.getTitle());

            if(selectedPos == position) {
                itemView.setBackgroundColor(mContext.getResources().getColor(R.color.background_selected_item));
            } else itemView.setBackgroundColor(mContext.getResources().getColor(R.color.background_normal_item));
        }
    }

    interface OnItemClick{
        void onItemClick(int position);
    }

}
