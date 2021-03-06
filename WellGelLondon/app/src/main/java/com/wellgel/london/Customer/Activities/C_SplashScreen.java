package com.wellgel.london.Customer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.wellgel.london.Provider.Activities.P_DashBoard;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.PreferencesShared;

public class C_SplashScreen extends AppCompatActivity {

    private PreferencesShared shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__splash_screen);

        shared = new PreferencesShared(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (shared.getBoolean(ConstantClass.isCustomerLoggeIn)) {
                    startActivity(new Intent(C_SplashScreen.this, C_DashboardAct.class));
                    finish();
                } else if (shared.getBoolean(ConstantClass.isProviderLoggeIn)) {
                    startActivity(new Intent(C_SplashScreen.this, P_DashBoard.class));
                    finish();
                } else {
                    startActivity(new Intent(C_SplashScreen.this, C_LoginAsActivity.class));
                    finish();
                }
            }
        }).start();
    }
}
