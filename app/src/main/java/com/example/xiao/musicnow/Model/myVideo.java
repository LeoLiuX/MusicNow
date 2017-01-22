package com.example.xiao.musicnow.Model;

import android.graphics.Bitmap;

/**
 * Created by liuxi on 2017/1/21.
 */

public class myVideo {
    private int videoId;
    private String videoName;
    private String videoDescription;
    private String videoImageUrl;
    private Bitmap videoImage;
    private String videoUrl;

    public myVideo(){}

    public myVideo(int id, String name, String desc, String image, String video){
        this.videoId = id;
        this.videoName = name;
        this.videoDescription = desc;
        this.videoImageUrl = image;
        this.videoUrl = video;
    }

    public void setImage(Bitmap image){
        this.videoImage = image;
    }
    public void setVideoId (int id){
        this.videoId = id;
    }
    public void setVideoName (String name){
        this.videoName = name;
    }
    public void setVideoDescription (String desc){
        this.videoDescription = desc;
    }
    public void setVideoImageUrl (String url){
        this.videoImageUrl = url;
    }
    public void setVideoUrl (String url){
        this. videoUrl = url;
    }

    public int getId(){
        return this.videoId;
    }
    public String getName(){
        return this.videoName;
    }
    public String getDescription(){
        return this.videoDescription;
    }
    public String getImageUrl(){
        return this.videoImageUrl;
    }
    public Bitmap getImage(){
        return this.videoImage;
    }
    public String getVideoUrl(){
        return this.videoUrl;
    }
}
