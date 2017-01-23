package com.example.xiao.musicnow.HomePage.Adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xiao.musicnow.HomePage.Fragments.video_favorite;
import com.example.xiao.musicnow.HomePage.Fragments.video_main;
import com.example.xiao.musicnow.Model.myVideo;
import com.example.xiao.musicnow.R;

import java.util.ArrayList;

/**
 * Created by liuxi on 2017/1/21.
 */

public class VideoAdapter extends  RecyclerView.Adapter<VideoHolder> implements View.OnClickListener {
    private Context mContext;
    ArrayList<myVideo> videos;
    public String TAG = "ALL_VIDEOS";

    public VideoAdapter(Context context, ArrayList<myVideo> videos) {
        mContext = context;
        this.videos = videos;
    }
    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_video_main, parent, false);
        VideoHolder allHolder = new VideoHolder(v);
        v.setOnClickListener(this);
        return allHolder;
    }

    @Override
    public void onBindViewHolder(final VideoHolder holder, final int position) {
        holder.mTextName.setText(videos.get(position).getName());
        holder.mTextDescription.setText(videos.get(position).getDescription());
        holder.mImageView.setImageBitmap(videos.get(position).getImage());
        holder.itemView.setTag(position);
        if (!videos.get(position).getFavorite()){
            holder.mFavoriteBtn.setImageResource(R.drawable.favorite_off);
        }
        else {
            holder.mFavoriteBtn.setImageResource(R.drawable.favorite_on);
        }

        holder.mFavoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videos.get(position).getFavorite()){
                    videos.get(position).removeFavorited();
                    holder.mFavoriteBtn.setImageResource(R.drawable.favorite_off);
                    video_favorite.refreshFavorite();
                    video_main.refreshFavorite();
                }
                else {
                    videos.get(position).setFavorited();
                    holder.mFavoriteBtn.setImageResource(R.drawable.favorite_on);
                    video_favorite.refreshFavorite();
                    video_main.refreshFavorite();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , int position);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(view, (Integer) view.getTag());
        }
        else{
            Log.e("CLICK", "ERROR");
        }
    }

    public void notifyData(ArrayList<myVideo> videos) {
//        Log.d("notifyData ", foods.size() + "");
        this.videos = videos;
        notifyDataSetChanged();
    }
}


class VideoHolder extends RecyclerView.ViewHolder {
    ImageView mImageView, mFavoriteBtn;
    TextView mTextId, mTextName, mTextDescription;

    public VideoHolder(View itemView) {
        super(itemView);
        mImageView = (ImageView) itemView.findViewById(R.id.video_main_image);
//        mTextId = (TextView) itemView.findViewById(R.id.video_main_name);
        mTextName = (TextView) itemView.findViewById(R.id.video_main_name);
        mTextDescription = (TextView) itemView.findViewById(R.id.video_main_description);
        mFavoriteBtn = (ImageView) itemView.findViewById(R.id.video_main_favorite);
    }
}
