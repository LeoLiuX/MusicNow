package com.example.xiao.musicnow.Controller;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.SeekBar;
import android.widget.Toast;

/**
 * Created by liuxi on 2017/1/21.
 */

public class VideoPlayer implements MediaPlayer.OnPreparedListener,
        SurfaceHolder.Callback {
    private int videoWidth;
    private int videoHeight;
    public MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private SeekBar skbProgress;
    private Timer mTimer=new Timer();
    private String url;

    public VideoPlayer(SurfaceView surfaceView, SeekBar skbProgress, String url)
    {
        this.url = url;
        this.skbProgress=skbProgress;
        surfaceHolder=surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mTimer.schedule(mTimerTask, 0, 1000);
    }


    TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if(mediaPlayer==null)
                return;
            if (mediaPlayer.isPlaying() && !skbProgress.isPressed()) {
                handleProgress.sendEmptyMessage(0);
            }
        }
    };

    Handler handleProgress = new Handler() {
        public void handleMessage(Message msg) {

            int position = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();

            if (duration > 0) {
                long pos = skbProgress.getMax() * position / duration;
                skbProgress.setProgress((int) pos);
            }
        }
    };

    public void play()
    {
        mediaPlayer.start();
    }

    public void playUrl(String videoUrl)
    {

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(videoUrl);
            mediaPlayer.prepare();
            mediaPlayer.pause();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void pause()
    {
        mediaPlayer.pause();
    }

    public void stop()
    {
        if(mediaPlayer != null){
            mediaPlayer.stop();
        }
    }

    public void destroy()
    {
        if(mediaPlayer != null){
            mediaPlayer.release();
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        Log.e("mediaPlayer", "surface changed");
    }




    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            Log.e("URL", url);
            new Thread(){
                public void run(){
                    try {
                        mediaPlayer.setDataSource(url);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                    Log.e("VideoPlayer", "Error --> what : " + what + " , extra : " + extra);
                    return false;
                }
            });
            mediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
                    Log.d("VideoPlayer", "Buffering: " + percent + " %");
                }
            });
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Log.d("VideoPlayer", "Complete!");
                }
            });
            mediaPlayer.setOnPreparedListener(this);
        } catch (Exception e) {
            Log.e("mediaPlayer", "error", e);
        }
        Log.e("mediaPlayer", "surface created");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        Log.e("mediaPlayer", "surface destroyed");
    }


    @Override
    public void onPrepared(MediaPlayer arg0) {
        Log.e("VideoPlayer", "Preparing...");
    }
}

