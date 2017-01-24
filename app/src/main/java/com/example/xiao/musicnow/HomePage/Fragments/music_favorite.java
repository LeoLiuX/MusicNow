package com.example.xiao.musicnow.HomePage.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.xiao.musicnow.Controller.SPController;
import com.example.xiao.musicnow.HomePage.Adapter.MusicAdapter;
import com.example.xiao.musicnow.LoginPage.LoginActivity;
import com.example.xiao.musicnow.Model.myMusic;
import com.example.xiao.musicnow.R;

import java.util.ArrayList;

/**
 * Created by Ricky on 2017/1/23.
 */

public class music_favorite extends Fragment {
    private static ArrayList<myMusic> favoriteList = new ArrayList<>();
    private View view;
    private RecyclerView mRecyclerView;
    private static MusicAdapter adapter;

    public static void refreshFavorite(){
        setFavoriteList();
        adapter.notifyData(favoriteList);
    }

    private static void setFavoriteList(){
        favoriteList.clear();
        for (int i=0; i<music_main.getMusicList().size(); i++){
            if (music_main.getMusicList().get(i).getFavorited()){
                favoriteList.add(music_main.getMusicList().get(i));
                adapter.notifyData(favoriteList);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_music_favorite, container, false);
        if (!SPController.getInstance(getActivity()).hasUserLoggedIn()){
            view.findViewById(R.id.music_favorite_notLogin).setVisibility(View.VISIBLE);
            Button loginBtn = (Button) view.findViewById(R.id.music_favorite_btn_login);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginIntent);
                    getActivity().finish();
                }
            });
            return view;
        }
        view.findViewById(R.id.music_favorite_notLogin).setVisibility(View.INVISIBLE);
        setFavoriteList();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_music_favorite);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MusicAdapter(getActivity(), favoriteList);
        adapter.setOnItemClickListener(new MusicAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("MUSIC_ID", favoriteList.get(position).getMusicId());
                Fragment detail = new music_detail();
                detail.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.home_fragment_container, detail)
                        .addToBackStack(music_favorite.class.getName())
                        .commit();
            }
        });
        mRecyclerView.setAdapter(adapter);
        return view;
    }
}
