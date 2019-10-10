package com.wellgel.london.Customer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wellgel.london.Customer.Adapters.MyCustomPagerAdapter;
import com.wellgel.london.Customer.C_ConstantClass;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.PreferencesShared;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


public class C_SalonDetailAct extends AppCompatActivity {


    TextView salonName, textnew, salonAddress, salonTiming;
    AppCompatRatingBar ratingBar;
    ImageView cart_detail, c_product_back;
    private PreferencesShared shared;
    ViewPager viewPager;
    List<String> images = new ArrayList<>();
    MyCustomPagerAdapter myCustomPagerAdapter;

    private C_SalonDetailAct activity;
    private TextView cart_count, c_select_date_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__salon_detail);

        activity = this;
        shared = new PreferencesShared(activity);
        salonName = findViewById(R.id.salonNAme);
        salonAddress = findViewById(R.id.near_salon_address);
        salonTiming = findViewById(R.id.near_salon_timing);
        ratingBar = findViewById(R.id.near_salon_rating);
        textnew = findViewById(R.id.textnew);
        cart_count = findViewById(R.id.cart_count);
        c_product_back = findViewById(R.id.c_salon_back);

        cart_detail = findViewById(R.id.cartValue);
        c_select_date_time = findViewById(R.id.c_select_date_time);
        viewPager = findViewById(R.id.pager);


        salonName.setText(getIntent().getStringExtra("salon_name"));
        salonAddress.setText(getIntent().getStringExtra("salon_address"));
        if (getIntent().getIntExtra("salon_rating", 0) == 0) {
            ratingBar.setVisibility(View.GONE);
            textnew.setVisibility(View.VISIBLE);
        } else {
            ratingBar.setVisibility(View.VISIBLE);
            textnew.setVisibility(View.GONE);
            ratingBar.setRating(getIntent().getIntExtra("salon_rating", 0));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat requestime = new SimpleDateFormat("hh:mm aa");
        Date dt = null, dt2 = null;

        try {
            dt = sdf.parse(getIntent().getStringExtra("salon_startTime"));
            dt2 = sdf.parse(getIntent().getStringExtra("salon_endTime"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        salonTiming.setText("Timings: " + requestime.format(dt) + "-" + requestime.format(dt2));

        c_product_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        c_select_date_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, C_SelectDateAct.class);
                intent.putExtra("salon_name", getIntent().getStringExtra("salon_name"));
                intent.putExtra("salon_address", getIntent().getStringExtra("salon_address"));
                intent.putExtra("salon_rating", getIntent().getIntExtra("salon_rating", 0));

                startActivity(intent);
            }
        });

        cart_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, C_CartDetailAct.class));
            }
        });
        images.add(getIntent().getStringExtra("salon_Image"));
        if (getIntent().getStringExtra("salon_Image1") != null)
            images.add(getIntent().getStringExtra("salon_Image1"));
        if (getIntent().getStringExtra("salon_Image2") != null)
            images.add(getIntent().getStringExtra("salon_Image2"));
        if (getIntent().getStringExtra("salon_Image3") != null)
            images.add(getIntent().getStringExtra("salon_Image3"));
        myCustomPagerAdapter = new MyCustomPagerAdapter(activity, images);

        viewPager.setAdapter(myCustomPagerAdapter);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shared.getString("cartsize").equalsIgnoreCase("")) {
            cart_count.setVisibility(View.GONE);
        } else {
            cart_count.setVisibility(View.VISIBLE);
            cart_count.setText(shared.getString("cartsize"));
        }
    }
}
