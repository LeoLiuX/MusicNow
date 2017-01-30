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

import com.example.xiao.musicnow.HomePage.Adapter.MusicFavAdapter;
import com.example.xiao.musicnow.Model.myMusic;
import com.example.xiao.musicnow.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by liuxi on 2017/1/23.
 */

public class offline_music extends Fragment {
    static ArrayList<myMusic> downloadList = new ArrayList<>();
    RecyclerView recyclerView;
    static MusicFavAdapter adapter;
    View view;

    public static void refreshDownload(){
        setDownloadList();
        adapter.notifyData(downloadList);
    }
    private static void setDownloadList(){
        downloadList.clear();
        for (int i=0; i<music_main.getMusicList().size(); i++){
            if (music_main.getMusicList().get(i).getDownloaded()){
                downloadList.add(music_main.getMusicList().get(i));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_offline_music, container, false);
        setDownloadList();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_music_download);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MusicFavAdapter(getActivity(),downloadList);
        adapter.setOnItemClickListener(new MusicFavAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("MUSIC_ID", downloadList.get(position).getMusicId());
                Fragment detail = new music_detail();
                detail.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.home_fragment_container, detail)
                        .addToBackStack(offline_music.class.getName())
                        .commit();

            }
        });
        recyclerView.setAdapter(adapter);
        return view;
    }
}
