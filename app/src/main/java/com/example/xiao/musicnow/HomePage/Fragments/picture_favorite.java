package com.example.xiao.musicnow.HomePage.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xiao.musicnow.HomePage.Adapter.PictureAdapter;
import com.example.xiao.musicnow.Model.myPicture;
import com.example.xiao.musicnow.R;

import java.util.ArrayList;

/**
 * Created by Ricky on 2017/1/23.
 */

public class picture_favorite extends Fragment {

    static ArrayList<myPicture> favoriteList = new ArrayList<>();
    RecyclerView recyclerView;
    static PictureAdapter adapter;

    public static void refreshFavorite(){
        setFavoriteList();
        adapter.notifyData(favoriteList);
    }

    private static void setFavoriteList(){
        favoriteList.clear();
        for (int i=0; i<picture_main.getPictureList().size(); i++){
            if (picture_main.getPictureList().get(i).getFavorite()){
                favoriteList.add(picture_main.getPictureList().get(i));
                adapter.notifyData(favoriteList);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_picture_favorite, container, false);
        setFavoriteList();
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_picture_favorite);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PictureAdapter(getActivity(),favoriteList);
        recyclerView.setAdapter(adapter);
        return v;
    }
}
