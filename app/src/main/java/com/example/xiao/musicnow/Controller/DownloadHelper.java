package com.example.xiao.musicnow.Controller;

import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.widget.Toast;

import com.example.xiao.musicnow.HomePage.HomeActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by liuxi on 2017/1/23.
 */

public class DownloadHelper extends AsyncTask<String, Integer, String> {

    private Context context;
    private PowerManager.WakeLock mWakeLock;
    private Object lock;
    public DownloadHelper(Context context, Object lock) {
        this.context = context;
        this.lock = lock;
    }

    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }
            int fileLength = connection.getContentLength();
            input = connection.getInputStream();
            output = new FileOutputStream("/sdcard/video.mp4");
            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                if (fileLength > 0)
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (connection != null)
                connection.disconnect();
        }
        return null;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        HomeActivity.showmDownloadingDialog();
    }
    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        HomeActivity.getmDownloadingDialog().setIndeterminate(false);
        HomeActivity.getmDownloadingDialog().setMax(100);
        HomeActivity.getmDownloadingDialog().setProgress(progress[0]);
    }
    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        HomeActivity.dismDownloadingDialog();
        if (result != null)
            Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
        else{
            Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
            synchronized (lock){
                lock.notify();
            }
        }
    }
}
