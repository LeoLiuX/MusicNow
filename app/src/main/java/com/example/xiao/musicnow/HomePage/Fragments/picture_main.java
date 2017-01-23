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
import com.example.xiao.musicnow.HomePage.Adapter.VideoAdapter;
import com.example.xiao.musicnow.Model.myPicture;
import com.example.xiao.musicnow.R;

import java.util.ArrayList;

/**
 * Created by Ricky on 2017/1/23.
 */

public class picture_main extends Fragment {
    static ArrayList<myPicture> pictures;
    RecyclerView recyclerView;
    static PictureAdapter adapter;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_picture_main, container,false);
        pictures = HomePageFragment.getPictureList();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_picture_main);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PictureAdapter(getActivity(), pictures);
        adapter.setOnItemClickListener(new VideoAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("position",position);
                Fragment detail = new picture_detail();
                detail.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.home_fragment_container, detail)
                        .addToBackStack(picture_main.class.getName())
                        .commit();
            }
        });
        recyclerView.setAdapter(adapter);
        return view;
    }

    public static void refreshFavorite(){
        adapter.notifyData(pictures);
    }

    public static ArrayList<myPicture> getPictureList(){
        return pictures;
    }
}
