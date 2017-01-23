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
import com.example.xiao.musicnow.HomePage.Fragments.picture_favorite;
import com.example.xiao.musicnow.HomePage.Fragments.picture_main;
import com.example.xiao.musicnow.HomePage.HomeActivity;
import com.example.xiao.musicnow.Model.myPicture;
import com.example.xiao.musicnow.R;

import java.util.ArrayList;

/**
 * Created by Ricky on 2017/1/23.
 */

public class PictureAdapter extends RecyclerView.Adapter<PictureHolder> implements View.OnClickListener{

    String TAG = "ALL_PICTURES";
    Context context;
    ArrayList<myPicture> picture;

    public PictureAdapter(Context context, ArrayList<myPicture> list)
    {
        this.context = context;
        picture = list;
    }

    @Override
    public PictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_picture_main, parent, false);
        PictureHolder holder = new PictureHolder(v);
        v.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(final PictureHolder holder, final int position) {
        holder.title.setText(picture.get(position).getTitle());
        holder.desc.setText(picture.get(position).getDescription());
        holder.pic.setImageBitmap(picture.get(position).getImage());
        holder.itemView.setTag(position);
        if(!picture.get(position).getFavorite()) {
            holder.favBtn.setImageResource(R.drawable.favorite_off);
        }
        else {
            holder.favBtn.setImageResource(R.drawable.favorite_on);
        }
        holder.favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SPController.getInstance(context).hasUserLoggedIn()){
                    HomeActivity.showLoginAlert();
                }
                else {
                    if(picture.get(position).getFavorite()) {
                        // dislike
                        picture.get(position).removeFavorited();
                        holder.favBtn.setImageResource(R.drawable.favorite_off);
                        picture_favorite.refreshFavorite();
                        picture_main.refreshFavorite();
                    }
                    else {
                        // like
                        picture.get(position).setFavorited();
                        holder.favBtn.setImageResource(R.drawable.favorite_on);
                        picture_favorite.refreshFavorite();
                        picture_main.refreshFavorite();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return picture.size();
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , int position);
    }

    private PictureAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(PictureAdapter.OnRecyclerViewItemClickListener listener) {
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

    public void notifyData(ArrayList<myPicture> list) {
        this.picture = list;
        notifyDataSetChanged();
    }
}

class PictureHolder extends RecyclerView.ViewHolder{

    ImageView pic, favBtn;
    TextView title, desc;

    public PictureHolder(View v) {
        super(v);
        pic = (ImageView) v.findViewById(R.id.picture_main_image);
        favBtn = (ImageView) v.findViewById(R.id.picture_main_favorite);
        title = (TextView) v.findViewById(R.id.picture_main_name);
        desc = (TextView) v.findViewById(R.id.picture_main_description);
    }
}