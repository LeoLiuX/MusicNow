package com.example.xiao.musicnow.HomePage.Fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.xiao.musicnow.Controller.AppController;
import com.example.xiao.musicnow.HomePage.HomeActivity;
import com.example.xiao.musicnow.Model.ClickImageView;
import com.example.xiao.musicnow.Model.myMusic;
import com.example.xiao.musicnow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.util.Locale;


/**
 * Created by Ricky on 2017/1/23.
 */

public class music_detail extends Fragment implements View.OnClickListener, View.OnTouchListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {
    final String TAG = "MUSIC_DETAIL";
    int musicId, duration;
    String musicUrl;
    myMusic curMusic;
    View v;
    ImageView image;
    ClickImageView playBtn, pauseBtn, stopBtn, downloadBtn;
    MediaPlayer player;
    SeekBar bar;
    TextView music_title, music_desc, song_duration, song_timer;
    private final Handler handler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        musicId = bundle.getInt("MUSIC_ID");
        for(int i=0; i<music_main.getMusicList().size(); i++) {
            if(music_main.getMusicList().get(i).getMusicId()==musicId) {
                curMusic = music_main.getMusicList().get(i);
                break;
            }
        }
        String url = "http://rjtmobile.com/ansari/rjt_music/music_app/music_play.php?&id="+musicId;
        sendRequest(url);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_music_detail, container, false);
        image = (ImageView) v.findViewById(R.id.music_detail_image);
        playBtn = (ClickImageView) v.findViewById(R.id.music_detail_play);
        pauseBtn = (ClickImageView) v.findViewById(R.id.music_detail_pause);
        stopBtn = (ClickImageView) v.findViewById(R.id.music_detail_stop);
        downloadBtn = (ClickImageView) v.findViewById(R.id.music_detail_download);
        music_title = (TextView) v.findViewById(R.id.music_detail_title);
        music_desc = (TextView) v.findViewById(R.id.music_detail_desc);
        song_timer = (TextView) v.findViewById(R.id.songCurrentDurationLabel);
        song_duration = (TextView) v.findViewById(R.id.songTotalDurationLabel);
        bar = (SeekBar) v.findViewById(R.id.seekBar);

        player = new MediaPlayer();

        String initTime = "00:00:00";
        song_timer.setText(initTime);
        song_duration.setText(initTime);
        image.setImageBitmap(curMusic.getMusicImage());
        music_title.setText(curMusic.getMusicName());
        music_desc.setText(curMusic.getMusicDesc());

        playBtn.setOnClickListener(this);
        pauseBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        bar.setOnTouchListener(this);
        player.setOnBufferingUpdateListener(this);
        player.setOnCompletionListener(this);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });

        return v;
    }

    private void download() {
        // download music
    }

    private void sendRequest(String url){
        HomeActivity.showPDialog();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    JSONArray array = response.getJSONArray("Albums");
                    JSONObject obj = array.getJSONObject(0);
                    musicUrl = obj.getString("MusicFile").replaceAll("\\s", "%20");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Log.d(TAG, musicUrl);
                    player.setDataSource(musicUrl);
                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            duration = player.getDuration();
                            song_duration.setText(millisecondToTime(duration));
                            HomeActivity.disPDialog();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //HomeActivity.disPDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "ERROR" + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                HomeActivity.disPDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(req, TAG);
    }

    private String millisecondToTime(int length) {
        final int HOUR = 60*60*1000;
        final int MINUTE = 60*1000;
        final int SECOND = 1000;
        int durationHour = length/HOUR;
        int durationMint = (length%HOUR)/MINUTE;
        int durationSec = (length%MINUTE)/SECOND;
        return String.format(Locale.getDefault(), "%02d:%02d:%02d",durationHour,durationMint,durationSec);
    }

    private void primarySeekBarProgressUpdater() {
        bar.setProgress((int) (((float) player.getCurrentPosition() / duration) * 100));
        song_timer.setText(millisecondToTime(player.getCurrentPosition()));
        if (player.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1000);
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        bar.setSecondaryProgress(percent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.music_detail_play) {
            if(!player.isPlaying()) {
                player.start();
            }
            primarySeekBarProgressUpdater();
        }
        if(v.getId() == R.id.music_detail_pause) {
            if(player.isPlaying()) {
                player.pause();
            }
            primarySeekBarProgressUpdater();
        }
        if(v.getId() == R.id.music_detail_stop) {
            if(player.isPlaying()) {
                player.stop();
            }
            primarySeekBarProgressUpdater();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.seekBar) {
            if (player.isPlaying()) {
                int playPosition = (duration / 100) * bar.getProgress();
                player.seekTo(playPosition);
            }
        }
        return false;
    }
}
