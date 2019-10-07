package com.wellgel.london.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.wellgel.london.Customer.Adapters.TabAdapter;
import com.wellgel.london.Customer.Fragment.ApprovedOrders;
import com.wellgel.london.Customer.Fragment.CancelledOrders;
import com.wellgel.london.Customer.Fragment.UpcomingOrder;
import com.wellgel.london.R;

public class Ecom_BookingActivity extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView c_booking_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecom__booking);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        c_booking_back = findViewById(R.id.c_booking_back);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new UpcomingOrder(), "Upcoming/Pending");
        adapter.addFragment(new ApprovedOrders(), "Approved");
        adapter.addFragment(new CancelledOrders(), "Cancelled");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        c_booking_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        tabLayout.setTabTextColors(Color.parseColor("#7b7b7b"), Color.parseColor("#626e88"));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                TabLayout.Tab tab = tabLayout.getTabAt(position);
//                tab.select();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
//        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}