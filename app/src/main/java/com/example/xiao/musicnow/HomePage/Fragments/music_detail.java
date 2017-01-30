package com.example.xiao.musicnow.HomePage.Fragments;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.xiao.musicnow.Controller.AppController;
import com.example.xiao.musicnow.Controller.DownloadHelper;
import com.example.xiao.musicnow.HomePage.HomeActivity;
import com.example.xiao.musicnow.Model.ClickImageView;
import com.example.xiao.musicnow.Model.myMusic;
import com.example.xiao.musicnow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Locale;


/**
 * Created by Ricky on 2017/1/23.
 */

public class music_detail extends Fragment implements View.OnClickListener,SeekBar.OnSeekBarChangeListener,
        MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {

    final String TAG = "MUSIC_DETAIL";
    int musicId, duration=0;
    String musicUrl;
    myMusic curMusic;
    View v;
    ImageView image;
    ClickImageView playBtn, stopBtn, forwardBtn, backwardBtn, downloadBtn;
    MediaPlayer mp;
    LinearLayout timezone;
    SeekBar bar;
    TextView music_title, music_desc, song_duration, song_timer;
    private final Handler handler = new Handler();
    final String initTime = "00:00:00";
    boolean paused = false;

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
        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);// Set the type of the streaming media
        mp.setOnPreparedListener(this);
        mp.setOnBufferingUpdateListener(this);
        mp.setOnCompletionListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_music_detail, container, false);
        image = (ImageView) v.findViewById(R.id.music_detail_image);
        playBtn = (ClickImageView) v.findViewById(R.id.music_detail_play);
        stopBtn = (ClickImageView) v.findViewById(R.id.music_detail_stop);
        forwardBtn = (ClickImageView) v.findViewById(R.id.music_detail_forward);
        backwardBtn = (ClickImageView) v.findViewById(R.id.music_detail_backward);
        downloadBtn = (ClickImageView) v.findViewById(R.id.music_detail_download);
        music_title = (TextView) v.findViewById(R.id.music_detail_title);
        music_desc = (TextView) v.findViewById(R.id.music_detail_desc);
        timezone = (LinearLayout) v.findViewById(R.id.timezone);
        song_timer = (TextView) v.findViewById(R.id.songCurrentDurationLabel);
        song_duration = (TextView) v.findViewById(R.id.songTotalDurationLabel);
        bar = (SeekBar) v.findViewById(R.id.seekBar);

        song_timer.setText(initTime);
        song_duration.setText(initTime);
        image.setImageBitmap(curMusic.getMusicImage());
        music_title.setText(curMusic.getMusicName());
        music_desc.setText(curMusic.getMusicDesc());

        backwardBtn.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        forwardBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        bar.setOnSeekBarChangeListener(this);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });

        if(!curMusic.getDownloaded()){
            timezone.setVisibility(View.GONE);
            String url = "http://rjtmobile.com/ansari/rjt_music/music_app/music_play.php?&id="+musicId;
            sendRequest(url);
        } else{
            // get from file
            timezone.setVisibility(View.VISIBLE);
            try {
                mp.setDataSource("/sdcard/" + curMusic.getMusicName());
                HomeActivity.showPDialog();
                mp.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return v;
    }

    private void download() {
        if (curMusic.getDownloaded()){
            Toast.makeText(getActivity(), "Music Already Been Downloaded!", Toast.LENGTH_SHORT).show();
            return;
        }
        // download music
        curMusic.setDownloaded();
        final DownloadHelper downloadTask = new DownloadHelper(getActivity(), null, curMusic.getMusicName());
        Log.e("DOWNLOAD",musicUrl);
        downloadTask.execute(musicUrl);
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
                    musicUrl = obj.getString("MusicFile").trim().replaceAll("\\s", "%20");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Log.d(TAG, musicUrl);
                    mp.setDataSource(musicUrl);
                    mp.prepareAsync();
                    // always return 5832704 ms
//                    duration = mp.getDuration();
//                    song_duration.setText(millisecondToTime(duration));
//                    bar.setMax(duration);
                } catch (IOException e) {
                    HomeActivity.disPDialog();
                    e.printStackTrace();
                }
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

    private String millisecondToTime(long length) {
        final int HOUR = 60*60*1000;
        final int MINUTE = 60*1000;
        final int SECOND = 1000;
        long durationHour = length/HOUR;
        long durationMint = (length%HOUR)/MINUTE;
        long durationSec = (length%MINUTE)/SECOND;
        if(durationHour>0)
            return String.format(Locale.getDefault(), "%02d:%02d:%02d",durationHour,durationMint,durationSec);
        else
            return String.format(Locale.getDefault(), "%02d:%02d:%02d",0,durationMint,durationSec);
    }

    private void primarySeekBarProgressUpdater() {
        bar.setMax(mp.getDuration());
        bar.setProgress(mp.getCurrentPosition());
        song_timer.setText(millisecondToTime(mp.getCurrentPosition()));
        if (mp.isPlaying()) {
            handler.postDelayed(notification, 1000);
        }
    }

    public Runnable notification = new Runnable() {
        public void run() {
            primarySeekBarProgressUpdater();
        }
    };

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if(curMusic.getDownloaded()){
            bar.setSecondaryProgress(percent);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(curMusic.getDownloaded()){
            song_timer.setText(initTime);
            bar.setMax(mp.getDuration());
            bar.setProgress(0);
            handler.removeCallbacks(notification);
        }
        mp.start();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.music_detail_play) {
            if(!mp.isPlaying()) {
                // mediaplayer is paused or stopped, want to play
                playBtn.setImageResource(R.drawable.music_pause_icon);
                if(paused)
                {
                    paused = false;
                    mp.start();
                    if(curMusic.getDownloaded()){
                        primarySeekBarProgressUpdater();
                    }
                }
                else {
                    mp.prepareAsync();
                }
            } else {
                // mediaplayer is playing, want to pause
                mp.pause();
                if(curMusic.getDownloaded()){
                    handler.removeCallbacks(notification);
                }
                playBtn.setImageResource(R.drawable.music_play_icon);
                paused = true;
            }
        }
        if(v.getId() == R.id.music_detail_stop) {
            if(mp.isPlaying() || (!mp.isPlaying() && paused) ) {
                mp.stop();
                playBtn.setImageResource(R.drawable.music_play_icon);
                paused = false;
                if(curMusic.getDownloaded()){
                    song_timer.setText(initTime);
                    bar.setMax(mp.getDuration());
                    bar.setProgress(0);
                    handler.removeCallbacks(notification);
                }
            }
        }
        if(v.getId() == R.id.music_detail_backward) {
            if(!curMusic.getDownloaded()){
                Toast.makeText(getActivity(), "Function not available for streaming live content", Toast.LENGTH_SHORT).show();
            }
            else{
                mp.pause();
                playBtn.setImageResource(R.drawable.music_play_icon);
                // backward for 5 seconds
                if(mp.getCurrentPosition()-5000>0){
                    mp.seekTo(mp.getCurrentPosition()-5000);
                }
                else {
                    mp.seekTo(0);
                }
                mp.start();
                primarySeekBarProgressUpdater();
                playBtn.setImageResource(R.drawable.music_pause_icon);
            }
        }
        if(v.getId() == R.id.music_detail_forward) {
            if(!curMusic.getDownloaded()){
                Toast.makeText(getActivity(), "Function not available for streaming live content", Toast.LENGTH_SHORT).show();
            }
            else {
                mp.pause();
                playBtn.setImageResource(R.drawable.music_play_icon);
                // forward for 5 seconds
                if(mp.getCurrentPosition() + 5000 < mp.getDuration()){
                    mp.seekTo(mp.getCurrentPosition() + 5000);
                }
                else{
                    mp.seekTo(mp.getDuration());
                }
                mp.start();
                primarySeekBarProgressUpdater();
                playBtn.setImageResource(R.drawable.music_pause_icon);
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        if(curMusic.getDownloaded()){
            duration = mp.getDuration();  // if streaming live content, -1 is returned.
            song_duration.setText(millisecondToTime(duration));
            bar.setMax(duration);
            bar.setProgress(0);
            primarySeekBarProgressUpdater();
        }
        playBtn.setImageResource(R.drawable.music_pause_icon);
        HomeActivity.disPDialog();
    }

    @Override
    public void onStop() {
        super.onStop();
        mp.release();
        if(curMusic.getDownloaded()){
            handler.removeCallbacks(notification);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser){
            mp.seekTo(progress);
            bar.setProgress(progress);
            song_timer.setText(millisecondToTime(progress));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        handler.removeCallbacks(notification);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        primarySeekBarProgressUpdater();
    }
}
