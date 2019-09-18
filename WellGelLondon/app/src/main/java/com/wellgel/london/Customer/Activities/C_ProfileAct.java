package com.wellgel.london.Customer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.PreferencesShared;


public class C_ProfileAct extends AppCompatActivity {

    private ImageView user_profile;
    private TextView user_name, user_number, user_address, user_county, user_email;
    private PreferencesShared shared;
    private C_ProfileAct activity;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private FloatingActionButton update_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__profile);
        init();
    }

    private void init() {
        activity = this;
        shared = new PreferencesShared(activity);
        user_profile = findViewById(R.id.user_profile);
        update_btn = findViewById(R.id.update_profile);
        user_name = findViewById(R.id.user_first_name);
        user_number = findViewById(R.id.user_number);
        user_address = findViewById(R.id.user_address);
        user_county = findViewById(R.id.user_country);
        user_email = findViewById(R.id.user_email);


        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, C_UpdateProfileFra.class));
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        user_name.setText(shared.getString(ConstantClass.USER_NAME));
        user_email.setText(shared.getString(ConstantClass.USER_Email));
        user_number.setText(shared.getString(ConstantClass.USER_Number));
        user_address.setText(shared.getString(ConstantClass.USER_Address));
        user_county.setText(shared.getString(ConstantClass.USER_Country));
        Picasso.with(activity).load(shared.getString("profile")).into(user_profile);
    }

    protected void onResume() {
        super.onResume();

    }
}
