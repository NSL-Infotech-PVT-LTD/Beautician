package com.wellgel.london.Provider.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wellgel.london.Customer.Activities.C_LoginAsActivity;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.PreferencesShared;

import de.hdodenhof.circleimageview.CircleImageView;

public class P_DashBoard extends AppCompatActivity {

    CircleImageView p_dash_user_image;
    private TextView p_providerName;
    private PreferencesShared shared;
    private TextView provider_logout;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p__dash_board);
        shared = new PreferencesShared(this);

        p_dash_user_image = findViewById(R.id.p_dash_user_image);
        p_providerName = findViewById(R.id.p_providerName);
        provider_logout = findViewById(R.id.provider_logout);

        provider_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(P_DashBoard.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("Are you sure you want to Logout")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                shared.setBoolean(ConstantClass.isCustomerLoggeIn, false);
                                shared.setBoolean(ConstantClass.isProviderLoggeIn, false);
                                startActivity(new Intent(P_DashBoard.this, C_LoginAsActivity.class));
                                finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        if (!shared.getString(ConstantClass.PROVIDE_NAME).equalsIgnoreCase("")) {
            p_providerName.setText(shared.getString(ConstantClass.PROVIDE_NAME));

        }
        if (!shared.getString(ConstantClass.PROVIDE_PROFILE).equalsIgnoreCase("")) {
            Picasso.with(this).load(shared.getString(ConstantClass.PROVIDE_PROFILE)).into(p_dash_user_image);

        }

    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(this);
        super.onBackPressed();

    }
}
