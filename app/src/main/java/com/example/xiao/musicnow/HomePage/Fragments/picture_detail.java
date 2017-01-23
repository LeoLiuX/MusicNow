package com.example.xiao.musicnow.HomePage.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xiao.musicnow.Model.myPicture;
import com.example.xiao.musicnow.R;

import java.util.ArrayList;

/**
 * Created by Ricky on 2017/1/23.
 */

public class picture_detail extends Fragment {

    String TAG = "PICTURE_DETAIL";
    ArrayList<myPicture> pictures;
    int position;
    String title, desc;
    Bitmap image;
    ImageView imageView;
    TextView textTitle, textDesc;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pictures = HomePageFragment.getPictureList();
        Bundle bundle = getArguments();
        position = bundle.getInt("position");
        title = pictures.get(position).getTitle();
        desc = pictures.get(position).getDescription();
        image = pictures.get(position).getImage();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_picture_detail, container, false);
        imageView = (ImageView) v.findViewById(R.id.picture_detail_imageview);
        textTitle = (TextView) v.findViewById(R.id.picture_detail_title);
        textDesc = (TextView) v.findViewById(R.id.picture_detail_desc);
        imageView.setImageBitmap(image);
        textTitle.setText(title);
        textDesc.setText(desc);
        return v;
    }
}
