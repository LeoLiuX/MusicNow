package com.example.xiao.musicnow.Model;

import android.graphics.Bitmap;

/**
 * Created by Ricky on 2017/1/23.
 */

public class myMusic {
    private int MusicId;
    private String MusicName;
    private String MusicDesc;
    private String MusicImageUrl;
    private Bitmap MusicImage;

    private boolean favorited;
    private boolean downloaded;

    public myMusic(){ }

    public myMusic(int id, String name, String desc, String imageUrl) {
        MusicId = id;
        MusicName = name;
        MusicDesc = desc;
        MusicImageUrl = imageUrl;
        favorited = false;
        downloaded = false;
    }

    public void setMusicId(int id){
        MusicId = id;
    }
    public void setMusicName(String name) { MusicName = name; }
    public void setMusicDesc(String desc) { MusicDesc = desc; }
    public void setMusicImageUrl(String url) { MusicImageUrl = url; }
    public void setMusicImage(Bitmap map) { MusicImage = map; }

    public int getMusicId(){
        return MusicId;
    }
    public String getMusicName() { return MusicName; }
    public String getMusicDesc() { return MusicDesc; }
    public String getMusicImageUrl() { return MusicImageUrl; }
    public Bitmap getMusicImage() { return MusicImage; }

    public void setFavorited() { favorited = true; }
    public void removeFavorited() { favorited = false; }
    public boolean getFavorited() { return favorited; }

    public void setDownloaded() { downloaded = true; }
    public void removeDownloaded() { downloaded = false; }
    public boolean getDownloaded() { return downloaded; }
}
