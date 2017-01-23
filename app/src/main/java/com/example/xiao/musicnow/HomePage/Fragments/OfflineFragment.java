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

public class OfflineFragment extends Fragment {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private DownloadPagerAdapter mSectionsPagerAdapter;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_offline_holder, container, false);
        mSectionsPagerAdapter =  new DownloadPagerAdapter(getChildFragmentManager());
        mTabLayout = (TabLayout) view.findViewById(R.id.offline_holder_tab);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mViewPager = (ViewPager) view.findViewById(R.id.offline_holder_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
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


class DownloadPagerAdapter extends FragmentPagerAdapter {

    public DownloadPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch (position) {
            case 0:
                offline_music tab1 = new offline_music();
                return tab1;
            case 1:
                offline_video tab2 = new offline_video();
                return tab2;
            case 2:
                offline_picture tab3 = new offline_picture();
                return tab3;
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "MUSIC";
            case 1:
                return "VIDEO";
            case 2:
                return "PICTURE";
            default:
                break;
        }
        return null;
    }
}

