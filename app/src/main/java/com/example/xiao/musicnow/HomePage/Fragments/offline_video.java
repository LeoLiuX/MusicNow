package com.example.xiao.musicnow.HomePage.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xiao.musicnow.HomePage.Adapter.PictureAdapter;
import com.example.xiao.musicnow.HomePage.Adapter.VideoAdapter;
import com.example.xiao.musicnow.Model.myPicture;
import com.example.xiao.musicnow.Model.myVideo;
import com.example.xiao.musicnow.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by liuxi on 2017/1/23.
 */

public class offline_video extends Fragment{

    static ArrayList<myVideo> downloadList = new ArrayList<>();
    RecyclerView recyclerView;
    static VideoAdapter adapter;
    View v;
    public static void refreshDownload(){
        setDownloadList();
        adapter.notifyData(downloadList);
    }
    private static void setDownloadList(){
        downloadList.clear();
        for (int i=0; i<video_main.getVideoList().size(); i++){
            if (video_main.getVideoList().get(i).getDownload()){
                downloadList.add(video_main.getVideoList().get(i));
            }
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_offline_video, container, false);
        setDownloadList();
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_video_download);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VideoAdapter(getActivity(),downloadList);
        adapter.setOnItemClickListener(new VideoAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                File file = new File("/sdcard/" + downloadList.get(position).getName());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "video/*");
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        return v;
    }
}
