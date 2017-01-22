package com.example.xiao.musicnow.HomePage.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xiao.musicnow.HomePage.Adapter.VideoAdapter;
import com.example.xiao.musicnow.Model.myVideo;
import com.example.xiao.musicnow.R;

import java.util.ArrayList;

/**
 * Created by liuxi on 2017/1/21.
 */

public class video_favorite extends Fragment {

    private static ArrayList<myVideo> favoriteList = new ArrayList<>();
    private View view;
    private final String TAG = "VIDEO_FAVORITE";
    private RecyclerView mRecyclerView;
    private static VideoAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public video_favorite() {
        // Required empty public constructor
    }

    public static void refreshFavorite(){
        setFavoriteList();
        adapter.notifyData(favoriteList);
    }

    private static void setFavoriteList(){
        favoriteList.clear();
        for (int i=0; i<video_main.getVideoList().size(); i++){
            if (video_main.getVideoList().get(i).getFavorite()){
                favoriteList.add(video_main.getVideoList().get(i));
                adapter.notifyData(favoriteList);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video_favorite, container, false);
        setFavoriteList();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_video_favorite);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VideoAdapter(getActivity(), favoriteList);
        mRecyclerView.setAdapter(adapter);
        return view;
    }
}
