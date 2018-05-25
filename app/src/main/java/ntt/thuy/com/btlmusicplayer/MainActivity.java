package ntt.thuy.com.btlmusicplayer;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ntt.thuy.com.btlmusicplayer.entity.OfflineSong;
import ntt.thuy.com.btlmusicplayer.entity.OnlineSong;
import ntt.thuy.com.btlmusicplayer.entity.Song;
import ntt.thuy.com.btlmusicplayer.offline.OfflineFragment;
import ntt.thuy.com.btlmusicplayer.online.DetailTrackFragment;
import ntt.thuy.com.btlmusicplayer.online.OnlineFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView icSearch, icSort;
    private EditText searchView;
    private OnlineFragment onlineFragment;
    private OfflineFragment offlineFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        icSearch = (ImageView) findViewById(R.id.ic_search);
        icSearch.setOnClickListener(this);
        icSort = (ImageView) findViewById(R.id.ic_sort);
        icSort.setOnClickListener(this);
        searchView = (EditText) findViewById(R.id.search_view);
        searchView.setVisibility(View.GONE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        onlineFragment = new OnlineFragment();
        offlineFragment = new OfflineFragment();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

        public void setupViewPager(ViewPager viewPager) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(onlineFragment, "ONlINE");
            adapter.addFragment(offlineFragment, "OFFLINE");
            viewPager.setAdapter(adapter);
        }

        public void showDetailTrackFragment(OnlineSong onlineSong){
            getSupportFragmentManager().beginTransaction()
                .add(R.id.viewpager, DetailTrackFragment.newInstance(onlineSong))
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    // sort by A-Z
    private boolean isAscending = false;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic_search:
                searchView.setVisibility(View.VISIBLE);
                TextView toolBarHeader = (TextView) findViewById(R.id.toolbar_header);
                toolBarHeader.setVisibility(View.GONE);
                searchView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (tabLayout.getSelectedTabPosition() == 0) {
                            onlineFragment.getAdapter().getFilter().filter(charSequence);
                        } else {

                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                break;
            case R.id.ic_sort:
                Log.d("TEST", "sort");
                if(!isAscending) {
                    Log.d("TEST", "isAscending false");
                    icSort.setImageResource(R.mipmap.sort_by_alphabet_za);
                    isAscending = true;
                    if (tabLayout.getSelectedTabPosition() == 0) {
                        Log.d("TEST", "isAscending false online");
                        List<OnlineSong> list = onlineFragment.getList();
                        Collections.sort(list, new Comparator<OnlineSong>() {
                            @Override
                            public int compare(OnlineSong track1, OnlineSong track2) {
                                return track1.getTitle().compareToIgnoreCase(track2.getTitle());
                            }
                        });
                        onlineFragment.setList(list);
                    } else {
                        Log.d("TEST", "isAscending false offline");
                        List<OfflineSong> list = offlineFragment.getList();
                        Collections.sort(list, new Comparator<OfflineSong>() {
                            @Override
                            public int compare(OfflineSong track1, OfflineSong track2) {
                                return track1.getTitle().compareToIgnoreCase(track2.getTitle());
                            }
                        });
                        offlineFragment.setList(list);
                    }
                } else {
                    Log.d("TEST", "isAscending true");
                    icSort.setImageResource(R.mipmap.sort_by_alphabet_az);
                    isAscending = false;
                    if (tabLayout.getSelectedTabPosition() == 0) {
                        Log.d("TEST", "isAscending true online");
                        List<OnlineSong> list = onlineFragment.getList();
                        Collections.sort(list, new Comparator<OnlineSong>() {
                            @Override
                            public int compare(OnlineSong track1, OnlineSong track2) {
                                return -(track1.getTitle().compareToIgnoreCase(track2.getTitle()));
                            }
                        });
                        onlineFragment.setList(list);
                    } else {
                        Log.d("TEST", "isAscending true offline");
                        List<OfflineSong> list = offlineFragment.getList();
                        Collections.sort(list, new Comparator<OfflineSong>() {
                            @Override
                            public int compare(OfflineSong track1, OfflineSong track2) {
                                return -(track1.getTitle().compareToIgnoreCase(track2.getTitle()));
                            }
                        });
                        offlineFragment.setList(list);
                    }
                }
                break;
            default:
                break;

        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
