package com.horusdavnefer.lafm.db.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "track")
public class Track {

    @PrimaryKey
    @ColumnInfo(name = "creationDate")
    private Long creationDate;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "listeners")
    private String listeners;
    @ColumnInfo(name = "mbid")
    private String mbid;
    @ColumnInfo(name = "url")
    private String url;
    @ColumnInfo(name = "streamable")
    private String streamable;
    @ColumnInfo(name = "imgsmall")
    private String imgsmall;
    @ColumnInfo(name = "imgmedium")
    private String imgmedium;
    @ColumnInfo(name = "imglarge")
    private String imglarge;
    @ColumnInfo(name = "imgextralarge")
    private String imgextralarge;
    @ColumnInfo(name = "imgmega")
    private String imgmega;
    @ColumnInfo(name = "rank")
    private String rank;
    @ColumnInfo(name = "nameartist")
    private String nameartist;
    @ColumnInfo(name = "mbidartist")
    private String mbidartist;
    @ColumnInfo(name = "urlartist")
    private String urlartist;
    @ColumnInfo(name = "duration")
    private long duration;

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setImgmega(String imgmega) {
        this.imgmega = imgmega;
    }

    public void setImglarge(String imglarge) {
        this.imglarge = imglarge;
    }

    public void setImgextralarge(String imgextralarge) {
        this.imgextralarge = imgextralarge;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStreamable(String streamable) {
        this.streamable = streamable;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public void setListeners(String listeners) {
        this.listeners = listeners;
    }

    public void setImgsmall(String imgsmall) {
        this.imgsmall = imgsmall;
    }

    public void setImgmedium(String imgmedium) {
        this.imgmedium = imgmedium;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public void setMbidartist(String mbidartist) {
        this.mbidartist = mbidartist;
    }

    public void setNameartist(String nameartist) {
        this.nameartist = nameartist;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setUrlartist(String urlartist) {
        this.urlartist = urlartist;
    }

    public String getImgmega() {
        return imgmega;
    }

    public String getImglarge() {
        return imglarge;
    }

    public String getImgextralarge() {
        return imgextralarge;
    }

    public String getUrl() {
        return url;
    }

    public String getStreamable() {
        return streamable;
    }

    public String getName() {
        return name;
    }

    public String getMbid() {
        return mbid;
    }

    public String getListeners() {
        return listeners;
    }

    public String getImgsmall() {
        return imgsmall;
    }

    public String getImgmedium() {
        return imgmedium;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public String getMbidartist() {
        return mbidartist;
    }

    public String getNameartist() {
        return nameartist;
    }

    public String getRank() {
        return rank;
    }

    public String getUrlartist() {
        return urlartist;
    }

    public long getDuration() {
        return duration;
    }
}
