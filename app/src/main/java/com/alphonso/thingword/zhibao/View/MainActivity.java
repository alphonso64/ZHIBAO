package com.alphonso.thingword.zhibao.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.alphonso.thingword.zhibao.Fragment.MapFragment;
import com.alphonso.thingword.zhibao.Fragment.PickerFragment;
import com.alphonso.thingword.zhibao.Fragment.TextFragment;
import com.alphonso.thingword.zhibao.R;
import com.alphonso.thingword.zhibao.View.NoSrollViewPager;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{

    private NoSrollViewPager mViewPager;
    private ArrayList<Fragment> mFragments;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 0);

        }

        setContentView(R.layout.activity_main);
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.ic_launcher, R.string.tittle_news).setActiveColorResource(R.color.colorPrimaryDark))
                .addItem(new BottomNavigationItem(R.mipmap.ic_launcher, R.string.tittle_camera).setActiveColorResource(R.color.colorPrimaryDark))
                .addItem(new BottomNavigationItem(R.mipmap.ic_launcher, R.string.tittle_setting).setActiveColorResource(R.color.colorPrimaryDark))
                .setFirstSelectedPosition(0)
                .initialise();
        bottomNavigationBar.setFocusable(false);
        bottomNavigationBar.setTabSelectedListener(this);
        initFragments();

        mViewPager = (NoSrollViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(new MyFragmentPageAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(6);
    }

    public void initFragments() {
        mFragments = new ArrayList<>();

        int index = 0;
        mFragments.add(MapFragment.newInstance("Map"));
        mFragments.add(PickerFragment.newInstance("2"));
        mFragments.add(TextFragment.newInstance("3"));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("index", index);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onTabSelected(int position) {
        index = position;
        mViewPager.setCurrentItem(position, false);
    }

    @Override
    public void onTabUnselected(int position) {
    }

    @Override
    public void onTabReselected(int position) {
    }

    public class MyFragmentPageAdapter extends FragmentStatePagerAdapter {

        public MyFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

    }


}
