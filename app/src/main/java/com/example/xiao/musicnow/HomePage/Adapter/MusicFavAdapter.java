package com.example.xiao.musicnow.HomePage.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xiao.musicnow.Controller.SPController;
import com.example.xiao.musicnow.HomePage.Fragments.music_favorite;
import com.example.xiao.musicnow.HomePage.Fragments.music_main;
import com.example.xiao.musicnow.HomePage.HomeActivity;
import com.example.xiao.musicnow.Model.myMusic;
import com.example.xiao.musicnow.R;

import java.util.ArrayList;

/**
 * Created by Ricky on 2017/1/23.
 */

public class MusicFavAdapter extends RecyclerView.Adapter<MusicFavHolder> implements View.OnClickListener{

    Context context;
    ArrayList<myMusic> music;
    String TAG = "ALL_MUSICS";

    public MusicFavAdapter(Context context, ArrayList<myMusic> list){
        this.context = context;
        music = list;
    }

    @Override
    public MusicFavHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_music_favorite, parent, false);
        MusicFavHolder holder = new MusicFavHolder(v);
        v.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MusicFavHolder holder, final int position) {
        holder.name.setText(music.get(position).getMusicName());
        holder.desc.setText(music.get(position).getMusicDesc()+"...");
        holder.image.setImageBitmap(music.get(position).getMusicImage());
        holder.itemView.setTag(position);
        if(!music.get(position).getFavorited()){
            holder.favBtn.setImageResource(R.drawable.favorite_off);
        } else{
            holder.favBtn.setImageResource(R.drawable.favorite_on);
        }
        holder.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SPController.getInstance(context).hasUserLoggedIn()){
                    HomeActivity.showLoginAlert();
                } else {
                    if(music.get(position).getFavorited()) {
                        music.get(position).removeFavorited();
                        holder.favBtn.setImageResource(R.drawable.favorite_off);
                        music_favorite.refreshFavorite();
                        music_main.refreshFavorite();
                    } else {
                        music.get(position).setFavorited();
                        holder.favBtn.setImageResource(R.drawable.favorite_on);
                        music_favorite.refreshFavorite();
                        music_main.refreshFavorite();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return music.size();
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
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

    public void notifyData(ArrayList<myMusic> list) {
        this.music = list;
        notifyDataSetChanged();
    }
}

class MusicFavHolder extends RecyclerView.ViewHolder{

    TextView name, desc;
    ImageView image, favBtn;

    public MusicFavHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.music_fav_name);
        desc = (TextView) itemView.findViewById(R.id.music_fav_description);
        image = (ImageView) itemView.findViewById(R.id.music_fav_image);
        favBtn = (ImageView) itemView.findViewById(R.id.music_fav_favorite);
    }
}