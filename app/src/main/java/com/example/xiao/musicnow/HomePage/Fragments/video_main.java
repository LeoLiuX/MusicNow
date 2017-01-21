package com.example.xiao.musicnow.HomePage.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.xiao.musicnow.Controller.AppController;
import com.example.xiao.musicnow.HomePage.Adapter.VideoAdapter;
import com.example.xiao.musicnow.HomePage.HomeActivity;
import com.example.xiao.musicnow.Model.myVideo;
import com.example.xiao.musicnow.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by liuxi on 2017/1/21.
 */


public class video_main extends Fragment {
    ArrayList<myVideo> videos = new ArrayList<>();
    private final String URL_VIDEO = "http://rjtmobile.com/ansari/rjt_music/music_app/video_list.php?";
    private final String TAG = "VIDEO_MAIN";
    private RecyclerView mRecyclerView;
    private VideoAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private View view;
    public video_main() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video_main, container, false);

        if (videos.size()==0){
            objRequestMethod();
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_video_main);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VideoAdapter(getActivity(), videos);
        mRecyclerView.setAdapter(adapter);
        return view;
    }

    private void objRequestMethod(){
        HomeActivity.showPDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, URL_VIDEO, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    JSONArray videoJsonArray = response.getJSONArray("Videos");
                    for (int i = 0; i < videoJsonArray.length(); i++) {
                        JSONObject c = videoJsonArray.getJSONObject(i);
                        int id = c.getInt("VideoId");
                        String name = c.getString("VideoName");
                        String description = c.getString("VideoDesc");
                        String imageUrl = c.getString("VideoThumb");
                        String videoUrl = c.getString("VideoFile");

                        final myVideo curVideo = new myVideo(id, name, description, imageUrl, videoUrl);
                        videos.add(curVideo);
//                        Log.e("Current Food", curFood.getName());

                        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                        imageLoader.get(imageUrl, new ImageLoader.ImageListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, "Image Load Error: " + error.getMessage());
                            }

                            @Override
                            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                                if (response.getBitmap() != null) {
                                    curVideo.setImage(response.getBitmap());
                                    adapter.notifyData(videos);
                                }
                            }
                        });

                        //test
                        videos.get(i).setImage(curVideo.getImage());
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                HomeActivity.disPDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "ERROR" + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                HomeActivity.disPDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
}
