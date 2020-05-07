package com.horusdavnefer.lafm.db.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface ArtistDao {

    @Query("SELECT * FROM " + "artist")
    List<Artist> getAllArtist();

    @Query("SELECT * FROM " + "artist" + " WHERE creationDate = :artist")
    Artist getArtist(String artist);

    @Query("SELECT * FROM " + "artist" +" WHERE name = :txtSearch")
    List<Artist> getSearchArtist(String txtSearch);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addArtist(Artist artist);

    @Delete
    void deleteArtist(Artist artist);

    @Update
    void updateArtist(Artist artist);
}
