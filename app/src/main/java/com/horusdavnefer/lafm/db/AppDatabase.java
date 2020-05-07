package com.horusdavnefer.lafm.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.horusdavnefer.lafm.db.models.Artist;
import com.horusdavnefer.lafm.db.models.ArtistDao;
import com.horusdavnefer.lafm.db.models.Track;
import com.horusdavnefer.lafm.db.models.TrackDao;

@Database(entities = {Artist.class, Track.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "FMDatabase";
    private static AppDatabase mInstance;

    public static AppDatabase getInstance(Context context) {
        if (mInstance == null) {
            synchronized (LOCK) {

                mInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return mInstance;
    }

    public abstract ArtistDao getArtistDao();
    public abstract TrackDao getTrackDao();
}