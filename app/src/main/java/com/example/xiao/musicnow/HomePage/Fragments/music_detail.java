package com.example.xiao.musicnow.HomePage.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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


/**
 * Created by Ricky on 2017/1/23.
 */

public class music_detail extends Fragment {
    final String TAG = "MUSIC_DETAIL";
    int musicId;
    String musicUrl;
    myMusic curMusic;
    View v;
    ImageView image;
    ClickImageView playBtn;
    VideoView video;
    TextView music_desc;

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
        video = (VideoView) v.findViewById(R.id.music_detail_video);
        music_desc = (TextView) v.findViewById(R.id.music_detail_desc);
        image.setImageBitmap(curMusic.getMusicImage());
        music_desc.setText(curMusic.getMusicDesc());
        Uri vidUri = Uri.parse(musicUrl);
        video.setVideoURI(vidUri);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playBtn.setVisibility(View.GONE);
                video.start();
            }
        });
        return v;
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
                    musicUrl = obj.getString("MusicFile");
                } catch (JSONException e) {
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

    @Override
    public void onPause() {
        super.onPause();
        playBtn.setVisibility(View.VISIBLE);
    }
}
