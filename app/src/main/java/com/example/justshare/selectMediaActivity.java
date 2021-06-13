package com.example.justshare;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.justshare.fragments.ApksFragment;
import com.example.justshare.fragments.ContactsFragment;
import com.example.justshare.fragments.FilesFragment;
import com.example.justshare.fragments.VideosFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import adapters.ViewPagerAdapter;

public class selectMediaActivity extends AppCompatActivity implements ImagesFragment.OnFragmentInteractionListener, FilesFragment.OnFragmentInteractionListener,
        VideosFragment.OnFragmentInteractionListener, ApksFragment.OnFragmentInteractionListener, ContactsFragment.OnFragmentInteractionListener {

    TabLayout tabLayout;
    ViewPager2 viewPager;

    private String[] titles = new String[]{"Images", "Files", "Videos", "APKs","Contacts" };

    private static final int NUM_PAGES = 3;
    private FragmentStateAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_media);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new ViewPagerAdapter(this, titles.length);

        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(titles[position]);
                    }
                }).attach();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
