package com.horusdavnefer.lafm.db.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "artist")
public class Artist {
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

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public void setImgextralarge(String imgextralarge) {
        this.imgextralarge = imgextralarge;
    }

    public void setImglarge(String imglarge) {
        this.imglarge = imglarge;
    }

    public void setImgmega(String imgmega) {
        this.imgmega = imgmega;
    }

    public void setImgmedium(String imgmedium) {
        this.imgmedium = imgmedium;
    }

    public void setImgsmall(String imgsmall) {
        this.imgsmall = imgsmall;
    }

    public void setListeners(String listeners) {
        this.listeners = listeners;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStreamable(String streamable) {
        this.streamable = streamable;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public String getImgmedium() {
        return imgmedium;
    }

    public String getImgsmall() {
        return imgsmall;
    }

    public String getListeners() {
        return listeners;
    }

    public String getMbid() {
        return mbid;
    }

    public String getName() {
        return name;
    }

    public String getStreamable() {
        return streamable;
    }

    public String getUrl() {
        return url;
    }

    public String getImgextralarge() {
        return imgextralarge;
    }

    public String getImglarge() {
        return imglarge;
    }

    public String getImgmega() {
        return imgmega;
    }
}
