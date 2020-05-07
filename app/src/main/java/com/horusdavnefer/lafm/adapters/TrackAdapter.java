package com.horusdavnefer.lafm.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.horusdavnefer.lafm.DetailArtistActivity;
import com.horusdavnefer.lafm.DetailTrackActivity;
import com.horusdavnefer.lafm.R;
import com.horusdavnefer.lafm.db.models.Artist;
import com.horusdavnefer.lafm.db.models.Track;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private List<Track> mData;
    private LayoutInflater mInflater;
    private Context mContext;

    // data is passed into the constructor
    public TrackAdapter(Context context, List<Track> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public TrackAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_track, parent, false);
        return new TrackAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(TrackAdapter.ViewHolder holder, int position) {
        Track track = mData.get(position);
        Picasso.get().load(track.getImgsmall()).into(holder.image);
        holder.mbid.setText(track.getMbid());
        holder.listeners.setText(track.getListeners());
        holder.name.setText(track.getName());
        Date date = new Date(track.getDuration()*1000);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        holder.duration.setText(formatter.format(date));
        holder.cell.setOnClickListener(v -> {
            Intent i = new Intent(mContext, DetailTrackActivity.class);
            i.putExtra("creationDate",track.getCreationDate());
            mContext.startActivity(i);
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // stores and recycles views as they are scrolled off screen
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView name;
        TextView listeners;
        TextView mbid;
        TextView duration;
        RelativeLayout cell;

        @SuppressLint("ShowToast")
        ViewHolder(View itemView) {
            super(itemView);

            cell = itemView.findViewById(R.id.cell);
            image = itemView.findViewById(R.id.imagenTrack);
            name = itemView.findViewById(R.id.name);
            listeners = itemView.findViewById(R.id.listeners);
            mbid = itemView.findViewById(R.id.mbid);
            duration = itemView.findViewById(R.id.duration);

        }

        @Override
        public void onClick(View view) {

        }
    }
}
