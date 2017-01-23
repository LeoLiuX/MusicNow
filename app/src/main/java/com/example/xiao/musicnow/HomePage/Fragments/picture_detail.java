package com.example.xiao.musicnow.HomePage.Fragments;

import android.content.DialogInterface;
import android.graphics.Bitmap;
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

import com.example.xiao.musicnow.Controller.DownloadHelper;
import com.example.xiao.musicnow.HomePage.HomeActivity;
import com.example.xiao.musicnow.Model.ClickImageView;
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
    ClickImageView downloadBtn;

    myPicture curPic;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pictures = HomePageFragment.getPictureList();
        Bundle bundle = getArguments();
        position = bundle.getInt("position");
        curPic = pictures.get(position);
        title = curPic.getTitle();
        desc = curPic.getDescription();
        image = curPic.getImage();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_picture_detail, container, false);
        imageView = (ImageView) v.findViewById(R.id.picture_detail_imageview);
        textTitle = (TextView) v.findViewById(R.id.picture_detail_title);
        textDesc = (TextView) v.findViewById(R.id.picture_detail_desc);
        downloadBtn = (ClickImageView) v.findViewById(R.id.picture_detail_download);
        imageView.setImageBitmap(image);
        textTitle.setText(title);
        textDesc.setText(desc);
        if (curPic.getDownload()){
            downloadBtn.setAlpha(100);
        }
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPicture();
            }
        });
        return v;
    }

    private void downloadPicture() {
        if (curPic.getDownload()){
            Toast.makeText(getActivity(), "Picture Already Been Downloaded!", Toast.LENGTH_SHORT).show();
            return;
        }
        // download image
        curPic.setDownloaded();
        final DownloadHelper downloadTask = new DownloadHelper(getActivity(), null, curPic.getTitle());
        final String picUrl = curPic.getUrl();
        Log.e("DOWNLOAD",picUrl);
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<picUrl.length(); i++){
            if (picUrl.charAt(i) == ' '){
                sb.append("%20");
            }else {
                sb.append(picUrl.charAt(i));
            }
        }
        downloadTask.execute(sb.toString());
    }
}
