package com.wellgel.london.Customer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.wellgel.london.APIs.Customer_APIs;
import com.wellgel.london.Customer.C_ConstantClass;
import com.wellgel.london.R;
import com.wellgel.london.Customer.SerializeModelClasses.LoginSerialized;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.PreferencesShared;
import com.wellgel.london.UtilClasses.Retrofit.RetrofitClientInstance;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class C_LoginActivity extends AppCompatActivity {

    private TextView c_login_forgot_tv, showHidePass, setErrorTv;
    private Button c_login_register_btn;
    private EditText c_login_pass_ed, c_login_email_ed;
    private Button c_login_btn;
    private String st_pass, st_email;
    private ProgressDialog progressDoalog;
    private C_LoginActivity activity;
    private PreferencesShared shared;
    private String imageURL = C_ConstantClass.IMAGE_BASE_URL + "customer/profile_image/";
    private ImageView c_login_with_fb_iv, googleSignInButton;
    private GoogleSignInClient googleSignInClient;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    public static String loginType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__login);
        init();
    }

    private void init() {
        activity = this;
        shared = new PreferencesShared(activity);
        c_login_forgot_tv = findViewById(R.id.c_login_forgot_tv);
        showHidePass = findViewById(R.id.showHidePass);
        c_login_with_fb_iv = findViewById(R.id.c_login_with_fb_iv);
        c_login_pass_ed = findViewById(R.id.c_login_pass_ed);
        c_login_register_btn = findViewById(R.id.c_login_register_btn);
        c_login_btn = findViewById(R.id.c_login_btn);
        googleSignInButton = findViewById(R.id.sign_in_button);
        c_login_email_ed = findViewById(R.id.c_login_email_ed);
        setErrorTv = findViewById(R.id.setErrorTv);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        // Creating CallbackManager
        // Defining the AccessTokenTracker
        accessTokenTracker = new AccessTokenTracker() {
            // This method is invoked everytime access token changes
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                // currentAccessToken is null if the user is logged out
                if (currentAccessToken != null) {
                    // AccessToken is not null implies user is logged in and hence we sen the GraphRequest
                    useLoginInformation(currentAccessToken);
                } else {
//                    setErrorTv.setText("Not Logged In");
                }
            }
        };
        haskey();
        OnClickHandle();

    }

    private void OnClickHandle() {


        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginType = "google";
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });

        c_login_forgot_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(C_LoginActivity.this, C_ForgotPasswordAct.class));
            }
        });
        c_login_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(C_LoginActivity.this, C_RegisterActivity.class));
            }
        });

        showHidePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String showtext = showHidePass.getText().toString();

                if (showtext.equalsIgnoreCase("Show")) {
                    showHidePass.setText("Hide");
                    c_login_pass_ed.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    showHidePass.setText("Show");
                    c_login_pass_ed.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

            }
        });


        c_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginType = "user";

                st_email = c_login_email_ed.getText().toString().trim();
                st_pass = c_login_pass_ed.getText().toString();

                if (!isValidEmail(st_email)) {
                    setErrorTv.setText(getString(R.string.inalidEmail));
                } else if (st_pass.length() < 5) {
                    setErrorTv.setText(getString(R.string.inalidPassWord));
                } else {

                    loginApi(st_email, st_pass);
                }
            }
        });
    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    //     login from server APi
    public void loginApi(String st_email, String st_pass) {
        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage(getString(R.string.checkUser));
        progressDoalog.show();

        // initialize file here

        /*Create handle for the RetrofitInstance interface*/
        Customer_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Customer_APIs.class);
        Call<LoginSerialized> call = service.login(st_email, st_pass, "android", shared.getString(ConstantClass.DEVICE_TOKEN));
        call.enqueue(new Callback<LoginSerialized>() {
            @Override
            public void onResponse(Call<LoginSerialized> call, Response<LoginSerialized> response) {
                progressDoalog.dismiss();


                if (response.body() != null) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                            shared.setBoolean(ConstantClass.isProviderLoggeIn, false);
                            shared.setBoolean(ConstantClass.isCustomerLoggeIn, true);

                            shared.setString("profile", imageURL + response.body().getData().getUser().getProfileImage());
                            shared.setString("token", response.body().getData().getToken());
                            shared.setString(ConstantClass.USER_NAME, response.body().getData().getUser().getName());
                            shared.setString(ConstantClass.USER_Address, response.body().getData().getUser().getAddress());
                            shared.setString(ConstantClass.USER_Country, response.body().getData().getUser().getCountry());
                            shared.setString(ConstantClass.USER_Number, response.body().getData().getUser().getPhone());
                            shared.setString(ConstantClass.USER_Email, response.body().getData().getUser().getEmail());
                            shared.setString(ConstantClass.ROLL_PLAY, ConstantClass.ROLL_CUSTOMER);
                            setErrorTv.setText(response.body().getData().getMessage());
                            startActivity(new Intent(activity, C_DashboardAct.class));
                            finish();
                        } else {
                            setErrorTv.setText(response.body().getError().getErrorMessage().getMessage().get(0));
                        }

                    } else {
                    }
                } else {


                    try {

                        if (loginType.equalsIgnoreCase("facebook") || loginType.equalsIgnoreCase("google")) {
                            startActivity(new Intent(activity, C_RegisterActivity.class));
                        }
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String errorMessage = jObjError.getJSONObject("error").getJSONObject("error_message").getJSONArray("message").getString(0);
                        setErrorTv.setText(errorMessage);

                    } catch (Exception e) {

                    }
                }
            }


            @Override
            public void onFailure(Call<LoginSerialized> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (loginType.equalsIgnoreCase("facebook"))
            callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 101:
                    try {
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);

                        if (account != null) {
                            ConstantClass.SOCIAL_EMAIL = account.getEmail();
                            ConstantClass.SOCIAL_PASSWORD = account.getId();
                            ConstantClass.SOCIAL_NAME = account.getDisplayName();
                            if (null != account.getPhotoUrl())
                                ConstantClass.SOCIAL_PROFILE = account.getPhotoUrl().toString();
                            else
                                ConstantClass.SOCIAL_PROFILE = "";
                            loginApi(ConstantClass.SOCIAL_EMAIL, ConstantClass.SOCIAL_PASSWORD);
                        }

                    } catch (ApiException e) {
                        // The ApiException status code indicates the detailed failure reason.
                        Log.w("Google", "signInResult:failed code=" + e.getStatusCode());
                    }
                    break;
            }
    }

    @Override
    public void onStart() {
        super.onStart();
//        GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);
//        if (alreadyloggedAccount != null) {
//            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
//
//        } else {
//            Log.d("Google", "Not logged in");
//        }
//
//        accessTokenTracker.startTracking();
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        if (accessToken != null)
//            useLoginInformation(accessToken);
    }

    public void onClick(View v) {
        if (v == c_login_with_fb_iv) {
            loginType = "facebook";
            callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("email", "public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // Retrieving access token using the LoginResult
                    AccessToken accessToken = loginResult.getAccessToken();
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(FacebookException error) {
                }
            });
        }
    }

    private void haskey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    private void useLoginInformation(AccessToken accessToken) {
        /**
         Creating the GraphRequest to fetch user details
         1st Param - AccessToken
         2nd Param - Callback (which will be invoked once the request is successful)
         **/
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            //OnCompleted is invoked once the GraphRequest is successful
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String name = object.getString("name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    ConstantClass.SOCIAL_EMAIL = email;
                    ConstantClass.SOCIAL_PASSWORD = id;
                    ConstantClass.SOCIAL_NAME = name;
                    String image = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    ConstantClass.SOCIAL_PROFILE = image;
                    loginApi(ConstantClass.SOCIAL_EMAIL, ConstantClass.SOCIAL_PASSWORD);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        // Initiate the GraphRequest
        request.executeAsync();
    }

    public void onDestroy() {
        super.onDestroy();
        // We stop the tracking before destroying the activity
        accessTokenTracker.stopTracking();
    }
}
