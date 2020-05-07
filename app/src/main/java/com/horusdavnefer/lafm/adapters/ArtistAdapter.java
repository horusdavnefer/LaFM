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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.horusdavnefer.lafm.DetailArtistActivity;
import com.horusdavnefer.lafm.R;
import com.horusdavnefer.lafm.db.models.Artist;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    private List<Artist> mData;
    private LayoutInflater mInflater;
    private Context mContext;

    // data is passed into the constructor
    public ArtistAdapter(Context context, List<Artist> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_artist, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ArtistAdapter.ViewHolder holder, int position) {
        Artist artist = mData.get(position);
        Picasso.get().load(artist.getImgsmall()).into(holder.image);
        holder.mbid.setText(artist.getMbid());
        holder.listeners.setText(artist.getListeners());
        holder.name.setText(artist.getName());
        holder.cell.setOnClickListener(v -> {
            Intent i = new Intent(mContext, DetailArtistActivity.class);
            i.putExtra("creationDate",artist.getCreationDate());
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
        RelativeLayout cell;

        @SuppressLint("ShowToast")
        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imagenArtist);
            name = itemView.findViewById(R.id.name);
            listeners = itemView.findViewById(R.id.listeners);
            mbid = itemView.findViewById(R.id.mbid);
            cell = itemView.findViewById(R.id.cell);

        }

        @Override
        public void onClick(View view) {

        }
    }
}
