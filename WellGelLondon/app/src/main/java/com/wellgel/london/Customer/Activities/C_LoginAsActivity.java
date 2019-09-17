package com.wellgel.london.Customer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wellgel.london.Provider.Activities.P_LoginActivity;
import com.wellgel.london.R;

public class C_LoginAsActivity extends AppCompatActivity {

    private Button c_login_btn, p_login_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__login_as);

        init();
    }


    private void init() {
        p_login_btn = findViewById(R.id.p_login_btn);
        c_login_btn = findViewById(R.id.c_login_btn);

        OnClickHandler();
    }

    private void OnClickHandler() {
        p_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(C_LoginAsActivity.this, P_LoginActivity.class));

            }
        });

        c_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(C_LoginAsActivity.this, C_LoginActivity.class));
            }
        });
    }
}
