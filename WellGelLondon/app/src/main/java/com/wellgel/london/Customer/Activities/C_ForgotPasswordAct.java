package com.wellgel.london.Customer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wellgel.london.Customer.APIs;
import com.wellgel.london.Provider.Activities.P_LoginActivity;
import com.wellgel.london.Provider.ModelSerialized.ForgotPasswordSerial;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.Retrofit.RetrofitClientInstance;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class C_ForgotPasswordAct extends AppCompatActivity {

    private EditText forgotPass_email_ed;
    private Button forgotPass_submit_btn;
    private String st_forgotEmail;
    private ProgressDialog progressDoalog;
    private C_ForgotPasswordAct activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__forgot_password);

        init();
    }

    private void init() {

        activity = this;
        forgotPass_email_ed = findViewById(R.id.c_forgotPass_email_ed);
        forgotPass_submit_btn = findViewById(R.id.c_forgotPass_submit_btn);

        forgotPass_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                st_forgotEmail = forgotPass_email_ed.getText().toString();
                if (!isValidEmail(st_forgotEmail)) {
                    forgotPass_email_ed.setError(getString(R.string.inalidEmail));
                } else {
                    forgotAPI();
                }
            }
        });
    }


    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    //     forgot to server APi
    public void forgotAPI() {
        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        // initialize file here

        /*Create handle for the RetrofitInstance interface*/
        APIs service = RetrofitClientInstance.getRetrofitInstance().create(APIs.class);
        Call<ForgotPasswordSerial> call = service.forgotPass(st_forgotEmail);
        call.enqueue(new Callback<ForgotPasswordSerial>() {
            @Override
            public void onResponse(Call<ForgotPasswordSerial> call, Response<ForgotPasswordSerial> response) {
                progressDoalog.dismiss();


                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            Toast.makeText(C_ForgotPasswordAct.this, "" + response.body().getData().getMessage(), Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(activity, P_LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(C_ForgotPasswordAct.this, "" + response.body().getError().getErrorMessage().getMessage().get(0), Toast.LENGTH_SHORT).show();

                        }

                    }
                } else {

                    try {
                        JSONObject jObjError = null;
                        if (response.errorBody() != null) {
                            jObjError = new JSONObject(response.errorBody().string());

                        String errorMessage = jObjError.getJSONObject("error").getJSONObject("error_message").getJSONArray("message").getString(0);

                        Toast.makeText(activity, "" + errorMessage, Toast.LENGTH_SHORT).show();  }
                    } catch (Exception e) {

                    }
                }
            }


            @Override
            public void onFailure(Call<ForgotPasswordSerial> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
