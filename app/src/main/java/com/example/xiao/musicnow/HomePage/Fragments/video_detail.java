package com.example.xiao.musicnow.HomePage.Fragments;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.xiao.musicnow.Controller.video_player;
import com.example.xiao.musicnow.R;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by liuxi on 2017/1/21.
 */

public class video_detail extends Fragment {
    private View view;
    private String videoUrl;

    private SurfaceView surfaceView;
    private Button btnPause, btnPlayUrl, btnStop;
    private SeekBar skbProgress;
    private video_player player;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video_detail, container, false);
        videoUrl = getArguments().getString("VIDEO_URL");
        Log.e("URL", videoUrl);

        surfaceView = (SurfaceView) view.findViewById(R.id.video_detail_surfaceview);

        btnPlayUrl = (Button) view.findViewById(R.id.video_detail_play);
        btnPlayUrl.setOnClickListener(new ClickEvent());

        btnPause = (Button) view.findViewById(R.id.video_detail_pause);
        btnPause.setOnClickListener(new ClickEvent());

        btnStop = (Button) view.findViewById(R.id.video_detail_stop);
        btnStop.setOnClickListener(new ClickEvent());

        skbProgress = (SeekBar) view.findViewById(R.id.video_detail_seekbar);
        skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        player = new video_player(surfaceView, skbProgress);
        return view;
    }

    class ClickEvent implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {
            if (arg0 == btnPause) {
                player.pause();
            } else if (arg0 == btnPlayUrl) {
                player.playUrl(videoUrl);
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
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
            this.progress = progress * player.mediaPlayer.getDuration()
                    / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
            player.mediaPlayer.seekTo(progress);
        }
    }

}
