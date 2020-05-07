package com.horusdavnefer.lafm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.horusdavnefer.lafm.adapters.TabViewPagerAdapter;
import com.horusdavnefer.lafm.fragments.ArtistsFragment;
import com.horusdavnefer.lafm.fragments.TrackArtistsFragment;

public class MusicActivity extends AppCompatActivity {

    TabLayout mTabLayout;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        mTabLayout = findViewById(R.id.tabs);
        mViewPager = findViewById(R.id.viewPager);
        mTabLayout.setupWithViewPager(mViewPager);
        setUpViewPager(mViewPager);
    }

    private void setUpViewPager(ViewPager mViewPager) {
        TabViewPagerAdapter tabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        tabViewPagerAdapter.addFragment(new ArtistsFragment(),getString(R.string.artists_title));
        tabViewPagerAdapter.addFragment(new TrackArtistsFragment(),getString(R.string.tracks_title));
        mViewPager.setAdapter(tabViewPagerAdapter);
    }
}
