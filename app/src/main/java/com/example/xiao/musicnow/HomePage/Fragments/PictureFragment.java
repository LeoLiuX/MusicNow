package com.example.xiao.musicnow.HomePage.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xiao.musicnow.HomePage.Adapter.PictureAdapter;
import com.example.xiao.musicnow.Model.myPicture;
import com.example.xiao.musicnow.R;

import java.util.ArrayList;

/**
 * Created by liuxi on 2017/1/20.
 */

public class PictureFragment extends Fragment{
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private PicturePagerAdapter mPicturePagerAdapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_picture_holder, container, false);
        mPicturePagerAdapter = new PicturePagerAdapter(getChildFragmentManager());
        mTabLayout = (TabLayout) view.findViewById(R.id.picture_holder_tab);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mViewPager = (ViewPager) view.findViewById(R.id.picture_holder_pager);
        mViewPager.setAdapter(mPicturePagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mTabLayout.setScrollPosition(position, 0, true);
                mTabLayout.setSelected(true);
                mViewPager.getParent().requestDisallowInterceptTouchEvent(true);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }
}

class PicturePagerAdapter extends FragmentPagerAdapter{

    public PicturePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                picture_main tab1 = new picture_main();
                return tab1;
            case 1:
                picture_favorite tab2 = new picture_favorite();
                return tab2;
            default:
                break;
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Pictures";
            case 1:
                return "Favorites";
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}