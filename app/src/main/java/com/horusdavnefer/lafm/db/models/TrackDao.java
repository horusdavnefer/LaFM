package com.horusdavnefer.lafm.db.models;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TrackDao {

    @Query("SELECT * FROM " + "track")
    List<Track> getAllTrack();

    @Query("SELECT * FROM " + "track" + " WHERE creationDate = :artist")
    Track getTrack(String artist);

    @Query("SELECT * FROM " + "track" +" WHERE name = :txtSearch")
    List<Track> getSearchTrack(String txtSearch);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addTrack(Track track);

    @Delete
    void deleteTrack(Track track);

    @Update
    void updateTrack(Track track);
}
