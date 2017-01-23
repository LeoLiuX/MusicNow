package com.example.xiao.musicnow.HomePage.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xiao.musicnow.Controller.DownloadHelper;
import com.example.xiao.musicnow.HomePage.HomeActivity;
import com.example.xiao.musicnow.Model.ClickImageView;
import com.example.xiao.musicnow.Model.myVideo;
import com.example.xiao.musicnow.R;

import java.io.File;

/**
 * Created by liuxi on 2017/1/21.
 */

public class video_detail extends Fragment {
    private View view;
    private myVideo video;
    private int videoId;

    private ImageView iv_poster;
    private ClickImageView btn_play;
    private TextView tv_title, tv_desc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video_detail, container, false);

        videoId = getArguments().getInt("VIDEO_ID");

        for (int i=0; i<video_main.getVideoList().size(); i++){
            if (video_main.getVideoList().get(i).getId() == videoId){
                video = video_main.getVideoList().get(i);
                break;
            }
        }
        init();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init(){
        iv_poster = (ImageView) view.findViewById(R.id.video_detail_poster);
        btn_play = (ClickImageView) view.findViewById(R.id.video_detail_play);
        tv_title = (TextView) view.findViewById(R.id.video_detail_title);
        tv_desc = (TextView) view.findViewById(R.id.video_detail_desc);
        iv_poster.setImageBitmap(video.getImage());
        tv_title.setText(video.getName());
        tv_desc.setText(video.getDescription());

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadVideo();
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            synchronized (lock){
                                lock.wait();
                                File file = new File("/sdcard/video.mp4");
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(file), "video/*");
                                startActivity(intent);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }

    private Object lock = new Object();

    private void downloadVideo(){
        final DownloadHelper downloadTask = new DownloadHelper(getActivity(), lock);
        final String videoUrl = video.getVideoUrl();
        Log.e("DOWNLOAD",videoUrl);
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<videoUrl.length(); i++){
            if (videoUrl.charAt(i) == ' '){
                sb.append("%20");
            }else {
                sb.append(videoUrl.charAt(i));
            }
        }
        HomeActivity.getmDownloadingDialog().setCancelable(true);
        HomeActivity.getmDownloadingDialog().setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });
        downloadTask.execute(sb.toString());


    }
}
