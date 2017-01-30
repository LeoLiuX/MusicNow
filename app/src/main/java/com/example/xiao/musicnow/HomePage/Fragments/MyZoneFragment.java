package com.example.xiao.musicnow.HomePage.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.xiao.musicnow.Model.ClickImageView;
import com.example.xiao.musicnow.R;

/**
 * Created by liuxi on 2017/1/20.
 */

public class MyZoneFragment extends Fragment {
    private View view;
    private ClickImageView music, video, picture;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myzone_holder, container, false);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.myzone_linear_layout);
        linearLayout.getBackground().setAlpha(100);
        music = (ClickImageView) view.findViewById(R.id.myzone_music_btn);
        video = (ClickImageView) view.findViewById(R.id.myzone_video_btn);
        picture = (ClickImageView) view.findViewById(R.id.myzone_picture_btn);

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment detail = new music_favorite();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.home_fragment_container, detail)
                        .addToBackStack(MyZoneFragment.class.getName())
                        .commit();
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment detail = new video_favorite();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.home_fragment_container, detail)
                        .addToBackStack(MyZoneFragment.class.getName())
                        .commit();
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment detail = new picture_favorite();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.home_fragment_container, detail)
                        .addToBackStack(MyZoneFragment.class.getName())
                        .commit();
            }
        });
        return view;
    }
}
