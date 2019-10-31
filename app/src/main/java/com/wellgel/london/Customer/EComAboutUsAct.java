package com.wellgel.london.Customer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.wellgel.london.R;

public class EComAboutUsAct extends AppCompatActivity {

    private ImageView c_dash_navi;
    private EComAboutUsAct activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecom_about_us);

        activity = this;
        c_dash_navi = findViewById(R.id.c_dash_navi);
        c_dash_navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
