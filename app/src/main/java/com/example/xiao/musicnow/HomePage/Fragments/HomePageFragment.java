package com.example.xiao.musicnow.HomePage.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.xiao.musicnow.Controller.AppController;
import com.example.xiao.musicnow.Controller.SPController;
import com.example.xiao.musicnow.HomePage.HomeActivity;
import com.example.xiao.musicnow.LoginPage.LoginActivity;
import com.example.xiao.musicnow.Model.ClickImageView;
import com.example.xiao.musicnow.Model.myPicture;
import com.example.xiao.musicnow.R;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by liuxi on 2017/1/22.
 */


public class HomePageFragment extends Fragment implements OnPageChangeListener{
    private View view;

    private final String URL_PIC = "http://rjtmobile.com/ansari/rjt_music/music_app/pics_list.php?";
    private final String TAG = "HOME";

    private ClickImageView btn_music, btn_video, btn_pic, btn_offline, btn_zone;

    private ViewPager viewPager;
    private ImageView[] tips;
    private ImageView[] mImageViews;
    private ImageAdapter imageAdapter;

    private static ArrayList<myPicture> pictures = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initIconBtn();
        if (pictures.isEmpty()){
            requestData();
        }
        else {
            initImages(pictures.size());
        }


        return view;
    }

    private void requestData(){
        HomeActivity.showPDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, URL_PIC, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    JSONArray videoJsonArray = response.getJSONArray("Photo Story");
                    initImages(videoJsonArray.length());
                    for (int i = 0; i < videoJsonArray.length(); i++) {
                        JSONObject c = videoJsonArray.getJSONObject(i);
                        int id = c.getInt("PicId");
                        String title = c.getString("PicTitle");
                        String description = c.getString("PicDesc");
                        String imageUrl = c.getString("PicUrl");
                        final myPicture curPic = new myPicture(id, title, description, imageUrl);
                        pictures.add(curPic);
                        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                        imageLoader.get(imageUrl, new ImageLoader.ImageListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, "Image Load Error: " + error.getMessage());
                                Resources res = getActivity().getResources();
                                Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.video_default_image);
                                curPic.setImage(bmp);
                            }

                            @Override
                            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                                if (response.getBitmap() != null) {
                                    curPic.setImage(response.getBitmap());
                                    imageAdapter.notifyDataChange();

                                }
                            }
                        });
                        pictures.get(i).setImage(curPic.getImage());
                        if (i == videoJsonArray.length()){
                            notify();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.toString());
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
    private void initImages(int size){
        ViewGroup group = (ViewGroup) view.findViewById(R.id.home_viewGroup);
        viewPager = (ViewPager) view.findViewById(R.id.home_viewPager);
        tips = new ImageView[size];
        for(int i=0; i<tips.length; i++){
            ImageView imageView = new ImageView(view.getContext());
            imageView.setLayoutParams(new LayoutParams(10,10));
            tips[i] = imageView;
            if(i == 0){
                tips[i].setBackgroundResource(R.drawable.page_indecator_focused);
            }else{
                tips[i].setBackgroundResource(R.drawable.page_indecator_unfocused);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            group.addView(imageView, layoutParams);
        }

        mImageViews = new ImageView[size];
        for(int i=0; i<mImageViews.length; i++){
            ImageView imageView = new ImageView(view.getContext());
            mImageViews[i] = imageView;
            if (i >= pictures.size()){
                imageView.setBackgroundResource(R.drawable.video_default_image);
            }
            else {
                Drawable drawablePic = new BitmapDrawable(view.getResources(), pictures.get(i).getImage());
                imageView.setBackground(drawablePic);
            }
        }
        imageAdapter = new ImageAdapter();
        viewPager.setAdapter(imageAdapter);
        viewPager.setOnPageChangeListener(this);
        viewPager.setCurrentItem((mImageViews.length) * 100);
    }
    private void initIconBtn(){
        btn_music = (ClickImageView) view.findViewById(R.id.home_btn_music);
        btn_video = (ClickImageView) view.findViewById(R.id.home_btn_video);
        btn_pic = (ClickImageView) view.findViewById(R.id.home_btn_picture);
        btn_offline = (ClickImageView) view.findViewById(R.id.home_btn_download);
        btn_zone = (ClickImageView) view.findViewById(R.id.home_btn_myzone);

        btn_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicFragment musicFragment = new MusicFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.home_fragment_container, musicFragment)
                        .addToBackStack(HomePageFragment.class.getName())
                        .commit();
        }
        });

        btn_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoFragment videoFragment = new VideoFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.home_fragment_container, videoFragment)
                        .addToBackStack(HomePageFragment.class.getName())
                        .commit();
            }
        });

        btn_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureFragment pictureFragment = new PictureFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.home_fragment_container, pictureFragment)
                        .addToBackStack(HomePageFragment.class.getName())
                        .commit();
            }
        });

        btn_offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OfflineFragment offlineFragment = new OfflineFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.home_fragment_container, offlineFragment)
                        .addToBackStack(HomePageFragment.class.getName())
                        .commit();
            }
        });

        btn_zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("CLICK", "BTN_5");
                if (SPController.getInstance(getActivity()).hasUserLoggedIn()) {
                    MyZoneFragment myZoneFragment = new MyZoneFragment();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                            .replace(R.id.home_fragment_container, myZoneFragment)
                            .addToBackStack(HomePageFragment.class.getName())
                            .commit();

                } else {
                    HomeActivity.showLoginAlert();
                }
            }
        });
    }



    public class ImageAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager)container).removeView(mImageViews[position % mImageViews.length]);

        }
        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager)container).addView(mImageViews[position % mImageViews.length], 0);
            return mImageViews[position % mImageViews.length];
        }

        public void notifyDataChange(){
            for(int i=0; i<mImageViews.length; i++){
                ImageView imageView = new ImageView(view.getContext());
                mImageViews[i] = imageView;
                if (i >= pictures.size()){
                    imageView.setBackgroundResource(R.drawable.video_default_image);
                }
                else {
                    Drawable drawablePic = new BitmapDrawable(view.getResources(), pictures.get(i).getImage());
                    imageView.setBackground(drawablePic);
                }
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }
    @Override
    public void onPageSelected(int arg0) {
        setImageBackground(arg0 % mImageViews.length);
    }
    private void setImageBackground(int selectItems){
        for(int i=0; i<tips.length; i++){
            if(i == selectItems){
                tips[i].setBackgroundResource(R.drawable.page_indecator_focused);
            }else{
                tips[i].setBackgroundResource(R.drawable.page_indecator_unfocused);
            }
        }
    }

    public static ArrayList<myPicture> getPictureList(){
        return pictures;
    }
}
