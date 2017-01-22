package com.example.xiao.musicnow.LoginPage.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.xiao.musicnow.LoginPage.Fragments.LoginFragment;
import com.example.xiao.musicnow.LoginPage.Fragments.RegistrationFragment;

/**
 * Created by Ricky on 2017/1/21.
 */

public class Pager extends FragmentStatePagerAdapter {

    int tabCount;

    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount=tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                LoginFragment tab1 = new LoginFragment();
                return tab1;
            case 1:
                RegistrationFragment tab2 = new RegistrationFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
