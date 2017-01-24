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

import com.example.xiao.musicnow.R;

/**
 * Created by liuxi on 2017/1/20.
 */



public class MusicFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager pager;
    MusicPagerAdapter adapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_music_holder, container, false);
        adapter = new MusicPagerAdapter(getChildFragmentManager());
        tabLayout = (TabLayout) view.findViewById(R.id.music_holder_tab);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        pager = (ViewPager) view.findViewById(R.id.music_holder_pager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabLayout.setScrollPosition(position, 0, true);
                tabLayout.setSelected(true);
                pager.getParent().requestDisallowInterceptTouchEvent(true);
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

class MusicPagerAdapter extends FragmentPagerAdapter{

    public MusicPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                music_main tab1 = new music_main();
                return tab1;
            case 1:
                music_favorite tab2 = new music_favorite();
                return tab2;
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Musics";
            case 1:
                return "Favorites";
            default:
                break;
        }
        return null;
    }
}