package com.example.xiao.musicnow.HomePage.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.example.xiao.musicnow.Controller.VideoPlayer;
import com.example.xiao.musicnow.R;

/**
 * Created by liuxi on 2017/1/21.
 */

public class video_detail extends Fragment {
    private View view;
    private String videoUrl;

    private SurfaceView surfaceView;
    private Button btnPause, btnPlayUrl, btnStop;
    private SeekBar skbProgress;
    private VideoPlayer player;
    private boolean isChanging=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video_detail, container, false);
        videoUrl = getArguments().getString("VIDEO_URL");

        init();

        player = new VideoPlayer(surfaceView, skbProgress, videoUrl);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.destroy();
    }

    private void init(){
        surfaceView = (SurfaceView) view.findViewById(R.id.video_detail_surfaceview);

        btnPlayUrl = (Button) view.findViewById(R.id.video_detail_play);
        btnPlayUrl.setOnClickListener(new ClickEvent());

        btnPause = (Button) view.findViewById(R.id.video_detail_pause);
        btnPause.setOnClickListener(new ClickEvent());

        btnStop = (Button) view.findViewById(R.id.video_detail_stop);
        btnStop.setOnClickListener(new ClickEvent());

        skbProgress = (SeekBar) view.findViewById(R.id.video_detail_seekbar);
        skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
    }

    class ClickEvent implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {
            if (arg0 == btnPause) {
                player.pause();
            } else if (arg0 == btnPlayUrl) {
                player.play();
            } else if (arg0 == btnStop) {
                player.stop();
            }

        }
    }

    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            this.progress = progress * player.mediaPlayer.getDuration()
                    / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            player.mediaPlayer.seekTo(progress);
        }
    }

}
