package com.example.xiao.musicnow.Model;

import android.graphics.Bitmap;

/**
 * Created by liuxi on 2017/1/22.
 */

public class myPicture {
    private int PicId;
    private String PicTitle;
    private String PicDesc;
    private String PicUrl;
    private Bitmap PicImage;

    private boolean favorited;
    private boolean downloaded;

    public myPicture(){}

    public myPicture(int id, String title, String desc, String image){
        this.PicId = id;
        this.PicTitle = title;
        this.PicDesc = desc;
        this.PicUrl = image;
        favorited = false;
        downloaded = false;
    }

    public void setImage(Bitmap image){
        this.PicImage = image;
    }
    public void setVideoId (int id){
        this.PicId = id;
    }
    public void setVideoName (String name){
        this.PicTitle = name;
    }
    public void setVideoDescription (String desc){
        this.PicDesc = desc;
    }
    public void setVideoImageUrl (String url){
        this.PicUrl = url;
    }
    public void setFavorited(){
        favorited = true;
    }
    public void removeFavorited(){
        favorited = false;
    }
    public boolean getFavorite(){
        return favorited;
    }

    public void setDownloaded(){
        downloaded = true;
    }
    public void removeDownloaded(){
        downloaded = false;
    }
    public boolean getDownload(){
        return downloaded;
    }

    public int getId(){
        return this.PicId;
    }
    public String getTitle(){
        return this.PicTitle;
    }
    public String getDescription(){
        return this.PicDesc;
    }
    public String getUrl(){
        return this.PicUrl;
    }
    public Bitmap getImage(){
        return this.PicImage;
    }
}
