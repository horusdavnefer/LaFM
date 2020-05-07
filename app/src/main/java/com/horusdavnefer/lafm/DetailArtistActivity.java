package com.horusdavnefer.lafm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.horusdavnefer.lafm.db.AppDatabase;
import com.horusdavnefer.lafm.db.AppExecutors;
import com.horusdavnefer.lafm.db.models.Artist;
import com.squareup.picasso.Picasso;

public class DetailArtistActivity extends AppCompatActivity {

    private ImageView mImageArtist;
    private TextView mName;
    private TextView mListeners;
    private TextView mMbid;
    private TextView mUrl;
    private TextView mStreamable;
    long creationDate;
    Context mContext;
    private Button mGo;
    private Artist artist;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_artist);
        mContext = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detalle del Artista");

        mImageArtist = findViewById(R.id.imagenArtist);
        mName = findViewById(R.id.name);
        mListeners = findViewById(R.id.listeners);
        mMbid = findViewById(R.id.mbid);
        mUrl = findViewById(R.id.url);
        mStreamable = findViewById(R.id.stremeable);
        mGo = findViewById(R.id.goURL);

        if (getIntent() != null) {
            creationDate = getIntent().getLongExtra("creationDate",0);
        }

        if (creationDate != 0) {
            AppExecutors.getInstance().diskIO().execute(() -> {
                artist = AppDatabase.getInstance(mContext).getArtistDao().getArtist(String.valueOf(creationDate));
                AppExecutors.getInstance().mainThread().execute(() -> {
                    Picasso.get().load(artist.getImglarge()).into(mImageArtist);
                    mName.setText("Nombre: "+artist.getName());
                    mListeners.setText("Oyentes: "+artist.getListeners());
                    mMbid.setText("MBID: "+artist.getMbid());
                    mUrl.setText("URL: "+artist.getUrl());
                    if (artist.getStreamable().equals("0")) {
                        mStreamable.setText("Transmitible: No");
                    } else {
                        mStreamable.setText("Transmitible: Si");
                    }

                    mGo.setOnClickListener(v -> {
                        Uri uri = Uri.parse(artist.getUrl());
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
