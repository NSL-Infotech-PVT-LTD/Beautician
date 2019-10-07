package com.wellgel.london.Customer.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.wellgel.london.Provider.Activities.P_LoginActivity;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.PreferencesShared;

public class C_LoginAsActivity extends AppCompatActivity {

    private Button c_login_btn, p_login_btn;
    private PreferencesShared shared;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__login_as);

        init();
    }


    private void init() {
        p_login_btn = findViewById(R.id.p_login_btn);
        c_login_btn = findViewById(R.id.c_login_btn);

        shared = new PreferencesShared(this);
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

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Device", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        shared.setString(ConstantClass.DEVICE_TOKEN, token);

                        Log.d("DeviceToken", "onComplete: " + token);
                    }
                });
    }
}
