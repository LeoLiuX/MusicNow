package com.example.xiao.musicnow.HomePage.Fragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.xiao.musicnow.HomePage.Adapter.MusicAdapter;
import com.example.xiao.musicnow.HomePage.HomeActivity;
import com.example.xiao.musicnow.Model.myMusic;
import com.example.xiao.musicnow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ricky on 2017/1/23.
 */

public class music_main extends Fragment {
    static ArrayList<myMusic> music = new ArrayList<>();
    static ArrayList<myMusic> music_new = new ArrayList<>();
    static ArrayList<myMusic> music_top = new ArrayList<>();
    static ArrayList<myMusic> music_comp = new ArrayList<>();
    private final String url_new = "http://rjtmobile.com/ansari/rjt_music/music_app/music_album_category.php?&album_type=new";
    private final String url_top_played = "http://rjtmobile.com/ansari/rjt_music/music_app/music_album_category.php?&album_type=top-played";
    private final String url_top_comp = "http://rjtmobile.com/ansari/rjt_music/music_app/music_album_category.php?&album_type=top-comp";
    private final String TAG = "MUSIC_MAIN";
    private RecyclerView recyclerView_new, recyclerView_top, recyclerView_comp;
    private static MusicAdapter adapter_new, adapter_top, adapter_comp;
    private View view;

    public static void refreshFavorite(){
        adapter_new.notifyData(music_new);
        adapter_top.notifyData(music_top);
        adapter_comp.notifyData(music_comp);
    }

    public static ArrayList<myMusic> getMusicList(){
        music.clear();
        music.addAll(music_new);
        music.addAll(music_top);
        music.addAll(music_comp);
        return music;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_music_main, container, false);
        if (music_new.size()==0){
            objRequestMethod(url_new, 0);
        }
        if (music_top.size()==0){
            objRequestMethod(url_top_played, 1);
        }
        if (music_comp.size()==0){
            objRequestMethod(url_top_comp, 2);
        }
        // new release
        recyclerView_new = (RecyclerView) view.findViewById(R.id.recyclerview_music_new_release);
        recyclerView_new.setHasFixedSize(false);
        recyclerView_new.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        adapter_new = new MusicAdapter(getActivity(), music_new);
        adapter_new.setOnItemClickListener(new MusicAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("MUSIC_ID", music_new.get(position).getMusicId());
                Fragment detail = new music_detail();
                detail.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.home_fragment_container, detail)
                        .addToBackStack(music_main.class.getName())
                        .commit();
            }
        });
        recyclerView_new.setAdapter(adapter_new);
        // top played
        recyclerView_top = (RecyclerView) view.findViewById(R.id.recyclerview_music_top_played);
        recyclerView_top.setHasFixedSize(false);
        recyclerView_top.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        adapter_top = new MusicAdapter(getActivity(), music_top);
        adapter_top.setOnItemClickListener(new MusicAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("MUSIC_ID", music_top.get(position).getMusicId());
                Fragment detail = new music_detail();
                detail.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.home_fragment_container, detail)
                        .addToBackStack(music_main.class.getName())
                        .commit();
            }
        });
        recyclerView_top.setAdapter(adapter_top);
        // top compilation
        recyclerView_comp = (RecyclerView) view.findViewById(R.id.recyclerview_music_top_comp);
        recyclerView_comp.setHasFixedSize(false);
        recyclerView_comp.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
        adapter_comp = new MusicAdapter(getActivity(), music_comp);
        adapter_comp.setOnItemClickListener(new MusicAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("MUSIC_ID", music_comp.get(position).getMusicId());
                Fragment detail = new music_detail();
                detail.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.home_fragment_container, detail)
                        .addToBackStack(music_main.class.getName())
                        .commit();
            }
        });
        recyclerView_comp.setAdapter(adapter_comp);
        return view;
    }

    private void objRequestMethod(String  url, final int source) {
        HomeActivity.showPDialog();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    JSONArray array = response.getJSONArray("Albums");
                    for(int i=0; i<array.length(); i++) {
                        JSONObject c = array.getJSONObject(i);
                        int id = c.getInt("AlbumId");
                        String name = c.getString("AlbumName");
                        String description = c.getString("AlbumDesc");
                        String imageUrl = c.getString("AlbumThumb");
                        final myMusic curMusic = new myMusic(id, name, description, imageUrl);
                        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                        switch(source) {
                            case 0:
                                music_new.add(curMusic);
                                imageLoader.get(imageUrl, new ImageLoader.ImageListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e(TAG, "Image Load Error: " + error.getMessage());
                                        Resources res = getActivity().getResources();
                                        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.video_default_image);
                                        curMusic.setMusicImage(bmp);
                                        adapter_new.notifyData(music_new);
                                    }

                                    @Override
                                    public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                                        if (response.getBitmap() != null) {
                                            curMusic.setMusicImage(response.getBitmap());
                                            adapter_new.notifyData(music_new);
                                        }
                                    }
                                });
                                music_new.get(i).setMusicImage(curMusic.getMusicImage());
                                break;
                            case 1:
                                music_top.add(curMusic);
                                imageLoader.get(imageUrl, new ImageLoader.ImageListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e(TAG, "Image Load Error: " + error.getMessage());
                                        Resources res = getActivity().getResources();
                                        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.video_default_image);
                                        curMusic.setMusicImage(bmp);
                                        adapter_top.notifyData(music_top);
                                    }

                                    @Override
                                    public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                                        if (response.getBitmap() != null) {
                                            curMusic.setMusicImage(response.getBitmap());
                                            adapter_top.notifyData(music_top);
                                        }
                                    }
                                });
                                music_top.get(i).setMusicImage(curMusic.getMusicImage());
                                break;
                            case 2:
                                music_comp.add(curMusic);
                                imageLoader.get(imageUrl, new ImageLoader.ImageListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e(TAG, "Image Load Error: " + error.getMessage());
                                        Resources res = getActivity().getResources();
                                        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.video_default_image);
                                        curMusic.setMusicImage(bmp);
                                        adapter_comp.notifyData(music_comp);
                                    }

                                    @Override
                                    public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                                        if (response.getBitmap() != null) {
                                            curMusic.setMusicImage(response.getBitmap());
                                            adapter_comp.notifyData(music_comp);
                                        }
                                    }
                                });
                                music_comp.get(i).setMusicImage(curMusic.getMusicImage());
                                break;
                            default:
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
        AppController.getInstance().addToRequestQueue(req, TAG);
    }
}
