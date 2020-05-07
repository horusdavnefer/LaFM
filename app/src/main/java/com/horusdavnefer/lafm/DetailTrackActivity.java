package com.horusdavnefer.lafm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.horusdavnefer.lafm.db.AppDatabase;
import com.horusdavnefer.lafm.db.AppExecutors;
import com.horusdavnefer.lafm.db.models.Artist;
import com.horusdavnefer.lafm.db.models.Track;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailTrackActivity extends AppCompatActivity {


    private ImageView mImageArtist;
    private TextView mName;
    private TextView mNameArtist;
    private TextView mListeners;
    private TextView mMbid;
    private TextView mMbidArtist;
    private TextView mUrl;
    private TextView mUrlArtist;
    private TextView mDuration;

    long creationDate;
    Context mContext;
    private Button mGoTrack;
    private Button mGoArtist;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_track);
        mContext = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detalle del Artista");

        mImageArtist = findViewById(R.id.imagenTrack);
        mName = findViewById(R.id.name);
        mListeners = findViewById(R.id.listeners);
        mMbid = findViewById(R.id.mbid);
        mUrl = findViewById(R.id.url);
        mNameArtist = findViewById(R.id.nameArtist);
        mMbidArtist = findViewById(R.id.mbidartist);
        mUrlArtist = findViewById(R.id.urlartis);
        mDuration = findViewById(R.id.duration);
        mGoTrack = findViewById(R.id.goURLTrack);
        mGoArtist = findViewById(R.id.goURLArtist);

        if (getIntent() != null) {
            creationDate = getIntent().getLongExtra("creationDate",0);
        }

            if (creationDate != 0) {
            AppExecutors.getInstance().diskIO().execute(() -> {
                Track track = AppDatabase.getInstance(mContext).getTrackDao().getTrack(String.valueOf(creationDate));
                AppExecutors.getInstance().mainThread().execute(() -> {
                    Picasso.get().load(track.getImglarge()).into(mImageArtist);
                    mName.setText("Nombre: "+track.getName());
                    mListeners.setText("Oyentes: "+track.getListeners());
                    mMbid.setText("MBID: "+track.getMbid());
                    mUrl.setText("URL: "+track.getUrl());
                    mNameArtist.setText("Artista: "+track.getNameartist());
                    mUrlArtist.setText("Url Artista: "+track.getUrlartist());
                    mMbidArtist.setText("Mbid Artista: "+track.getMbidartist());
                    Date date = new Date(track.getDuration()*1000);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
                    mDuration.setText(formatter.format(date));
                    mGoTrack.setOnClickListener(v -> {
                        Uri uri = Uri.parse(track.getUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    });
                    mGoArtist.setOnClickListener(v -> {
                        Uri uri = Uri.parse(track.getUrlartist());
                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                    });
                });


            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
