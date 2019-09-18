package com.wellgel.london.Customer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.wellgel.london.Customer.APIs;
import com.wellgel.london.Customer.C_ConstantClass;
import com.wellgel.london.Customer.SerializeModelClasses.RegistrationSerial;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.CameraUtility;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.PreferencesShared;
import com.wellgel.london.UtilClasses.Retrofit.RetrofitClientInstance;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wellgel.london.Customer.Activities.C_LoginActivity.loginType;

public class C_RegisterActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    private LinearLayout first_lay, second_lay, third_lay;
    private Button next, c_registr_back_btn;
    private C_RegisterActivity activity;
    private int REQUEST_CAMERA = 777, SELECT_FILE = 7777;
    private String IMAGE_DIRECTORY = "/WellgelLondon/";
    private String valueOfLayout = "";

    private String picturePath = "";
    private File file;
    private String userChoosenTask;
    private CircleImageView profileImage;
    private ProgressDialog progressDoalog;
    private EditText c_registr_name_ed, c_registr_email_ed, c_registr_country_ed;
    private EditText c_registr_address_ed, c_registr_pass_ed, c_registr_numer_ed;
    private String st_name, st_email, st_number, st_country, st_address, st_pass;
    private String address = "";
    private double latitude = 0.0, longitude = 0.0;
    private String imageURL = C_ConstantClass.IMAGE_BASE_URL + "customer/profile_image/";

    private PreferencesShared shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__register);

        init();
    }

    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }


    private void init() {
        activity = this;


        shared = new PreferencesShared(activity);
        first_lay = findViewById(R.id.first_layout);
        second_lay = findViewById(R.id.second_layout);
        third_lay = findViewById(R.id.third_layout);
        next = findViewById(R.id.c_registr_next_btn);
        c_registr_back_btn = findViewById(R.id.c_registr_back_btn);

        c_registr_address_ed = findViewById(R.id.c_registr_address_ed);
        c_registr_name_ed = findViewById(R.id.c_registr_Salname_ed);
        c_registr_email_ed = findViewById(R.id.c_registr_email_ed);
        c_registr_country_ed = findViewById(R.id.c_registr_country_ed);
        c_registr_pass_ed = findViewById(R.id.c_registr_pass_ed);
        c_registr_numer_ed = findViewById(R.id.c_registr_numer_ed);

        profileImage = findViewById(R.id.profileImage);

        if (loginType.equalsIgnoreCase("facebook") || loginType.equalsIgnoreCase("google")) {

            first_lay.setVisibility(View.GONE);
            second_lay.setVisibility(View.VISIBLE);
            third_lay.setVisibility(View.GONE);
            next.setText(getString(R.string.oneMore));
            valueOfLayout = getString(R.string.next);


            @SuppressLint("StaticFieldLeak") MyAsync obj = new MyAsync() {

                @Override
                protected void onPostExecute(Bitmap bmp) {
                    super.onPostExecute(bmp);

                    if (bmp != null) {


                        Bitmap thumbnail = bmp;
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                        file = saveImage1(thumbnail);
                        picturePath = saveImage(thumbnail);

                        FileOutputStream fo;
                        try {
                            file.createNewFile();
                            fo = new FileOutputStream(file);
                            fo.write(bytes.toByteArray());
                            fo.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        profileImage.setImageBitmap(thumbnail);
                    }
                }
            };
            obj.execute();


        } else {
            Toast.makeText(activity, "" + loginType, Toast.LENGTH_SHORT).show();
        }

        OnClickHandle();
        setUpGClient();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void OnClickHandle() {


        c_registr_address_ed.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (c_registr_address_ed.getRight() - c_registr_address_ed.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        // your action her

                        if (checkPermissions())
                            c_registr_address_ed.setText(address);

                        return true;
                    }
                }
                return false;
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getNextClick = next.getText().toString();

                if (loginType.equalsIgnoreCase("facebook") || loginType.equalsIgnoreCase("google")) {
                    if (getNextClick.equalsIgnoreCase(getString(R.string.oneMore))) {
                        if (validation2()) {
                            next.setText(getString(R.string.create_account));
                            first_lay.setVisibility(View.GONE);
                            second_lay.setVisibility(View.GONE);
                            third_lay.setVisibility(View.VISIBLE);
                            valueOfLayout = getString(R.string.oneMore);
                        }
                    } else {

                        if (picturePath.equalsIgnoreCase("")) {
                            Toast.makeText(activity, "" + getString(R.string.invalidProfile), Toast.LENGTH_SHORT).show();
                        } else
                            registrationAPI(ConstantClass.SOCIAL_NAME, ConstantClass.SOCIAL_EMAIL, ConstantClass.SOCIAL_PASSWORD);

                    }
                } else {


                    if (getNextClick.equalsIgnoreCase(getString(R.string.next))) {

                        if (validation1()) {
                            first_lay.setVisibility(View.GONE);
                            second_lay.setVisibility(View.VISIBLE);
                            third_lay.setVisibility(View.GONE);
                            next.setText(getString(R.string.oneMore));
                            valueOfLayout = getString(R.string.next);
                        }

                    } else if (getNextClick.equalsIgnoreCase(getString(R.string.oneMore))) {
                        if (validation2()) {
                            next.setText(getString(R.string.create_account));
                            first_lay.setVisibility(View.GONE);
                            second_lay.setVisibility(View.GONE);
                            third_lay.setVisibility(View.VISIBLE);
                            valueOfLayout = getString(R.string.oneMore);
                        }
                    } else {

                        if (picturePath.equalsIgnoreCase("")) {
                            Toast.makeText(activity, "" + getString(R.string.invalidProfile), Toast.LENGTH_SHORT).show();
                        } else
                            registrationAPI(st_name, st_email, st_pass);

                    }
                }
            }


        });


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        c_registr_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, C_LoginActivity.class));
                finish();
            }
        });
    }


    private boolean validation1() {

        c_registr_name_ed.setError(null);
        c_registr_email_ed.setError(null);
        c_registr_pass_ed.setError(null);

        st_name = c_registr_name_ed.getText().toString().trim();
        st_email = c_registr_email_ed.getText().toString().trim();
        st_pass = c_registr_pass_ed.getText().toString().trim();

        if (st_name.equalsIgnoreCase("")) {
            c_registr_name_ed.setError(getString(R.string.invalidName));
        } else if (!isValidEmail(st_email)) {
            c_registr_email_ed.setError(getString(R.string.inalidEmail));
        } else if (st_pass.length() < 6) {
            c_registr_pass_ed.setError(getString(R.string.invalidPass));
        } else {
            return true;
        }
        return false;
    }

    private boolean validation2() {

        c_registr_numer_ed.setError(null);
        c_registr_country_ed.setError(null);
        c_registr_address_ed.setError(null);

        st_number = c_registr_numer_ed.getText().toString().trim();
        st_country = c_registr_country_ed.getText().toString().trim();
        st_address = c_registr_address_ed.getText().toString().trim();

        if (st_number.length() < 10) {
            c_registr_numer_ed.setError(getString(R.string.invalidNumber));
        } else if (st_country.isEmpty()) {
            c_registr_country_ed.setError(getString(R.string.invalidField));
        } else if (st_address.isEmpty()) {
            c_registr_address_ed.setError(getString(R.string.invalidField));
        } else {
            return true;
        }
        return false;
    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
        switch (requestCode) {
            case CameraUtility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals(getResources().getString(R.string.take_photo)))
                        cameraIntent();
                    else if (userChoosenTask.equals(getResources().getString(R.string.choose_from)))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }
//this function is used to select the image on click listener

    private void selectImage() {
        final CharSequence[] items = {getResources().getString(R.string.take_photo), getResources().getString(R.string.choose_from),
                getResources().getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(getResources().getString(R.string.addPhoto));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = CameraUtility.checkPermission(activity);
                if (items[item].equals(getResources().getString(R.string.take_photo))) {
                    userChoosenTask = getResources().getString(R.string.take_photo);
                    if (result)
                        cameraIntent();

                } else if (items[item].equals(getResources().getString(R.string.choose_from))) {
                    userChoosenTask = getResources().getString(R.string.choose_from);
                    if (result)
                        galleryIntent();

                } else if (items[item].equals(getResources().getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    //Intent to open the gallery
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }


    //Camera intent to open the Camera view
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);

    }

    //on Activity Result method
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {


            if (requestCode == SELECT_FILE) {

                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            } else if (requestCode == REQUEST_CHECK_SETTINGS_GPS) {
                getMyLocation();
            }
        }
    }


    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        file = saveImage1(thumbnail);
        picturePath = saveImage(thumbnail);

        FileOutputStream fo;
        try {
            file.createNewFile();
            fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        profileImage.setImageBitmap(thumbnail);
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                file = saveImage1(bm);
                picturePath = saveImage(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        profileImage.setImageBitmap(bm);
    }

    ///////////code for image getting
    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File imageDirectory = new File(getExternalFilesDir(null) + IMAGE_DIRECTORY);
        if (!imageDirectory.exists()) {
            imageDirectory.mkdirs();
        }
        try {
            File f = new File(imageDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(activity,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public File saveImage1(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File imageDirectory = new File(getExternalFilesDir(null) + IMAGE_DIRECTORY);
        if (!imageDirectory.exists()) {
            imageDirectory.mkdirs();
        }
        try {
            File f = new File(imageDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(activity,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            return f;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    //     registration to server APi
    public void registrationAPI(String st_name, String st_email, String st_pass) {
        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        // initialize file here
        MultipartBody.Part f = null;
        if (file != null) {
            f = MultipartBody.Part.createFormData("profile_image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        }

        /*Create handle for the RetrofitInstance interface*/
        APIs service = RetrofitClientInstance.getRetrofitInstance().create(APIs.class);
        Call<RegistrationSerial> call = service.registration(f, st_name, st_email, st_pass, st_number, latitude + "", longitude + "", "android", "mydevice", st_address, st_country);
        call.enqueue(new Callback<RegistrationSerial>() {
            @Override
            public void onResponse(Call<RegistrationSerial> call, Response<RegistrationSerial> response) {


                progressDoalog.dismiss();
                if (response.body() != null) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                            valueOfLayout = "";
                            shared.setBoolean(ConstantClass.isProviderLoggeIn, false);
                            shared.setBoolean(ConstantClass.isCustomerLoggeIn, true);

                            shared.setString("profile", imageURL + response.body().getData().getUser().getProfileImage());
                            shared.setString("token", response.body().getData().getToken());
                            shared.setString(ConstantClass.USER_NAME, response.body().getData().getUser().getName());
                            shared.setString(ConstantClass.USER_Address, response.body().getData().getUser().getAddress());
                            shared.setString(ConstantClass.USER_Country, response.body().getData().getUser().getCountry());
                            shared.setString(ConstantClass.USER_Number, response.body().getData().getUser().getPhone());
                            shared.setString(ConstantClass.USER_Email, response.body().getData().getUser().getEmail());
                            startActivity(new Intent(C_RegisterActivity.this, C_DashboardAct.class));
                            finish();
                        } else {
                            Toast.makeText(activity, response.body().getError().getErrorMessage().getMessage().get(0), Toast.LENGTH_SHORT).show();

                        }


                    } else {
                    }
                } else {
                    try {
                        JSONObject jObjError = null;
                        if (response.errorBody() != null) {
                            jObjError = new JSONObject(response.errorBody().string());

                            String errorMessage = jObjError.getJSONObject("error").getJSONObject("error_message").getJSONArray("message").getString(0);

                            Toast.makeText(activity, "" + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(activity, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<RegistrationSerial> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Do whatever you need
        //You can display a message here
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //You can display a message here
    }

    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;
        if (mylocation != null) {
            latitude = mylocation.getLatitude();
            longitude = mylocation.getLongitude();
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                address = addresses.get(0).getAddressLine(0);
                // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private void getMyLocation() {
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(activity,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(googleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(activity,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied. 
                                    // However, we have no way               
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }


    private boolean checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
            return false;
        } else {
            getMyLocation();
            return true;
        }
    }


    @Override
    public void onBackPressed() {

        if (loginType.equalsIgnoreCase("facebook") || loginType.equalsIgnoreCase("google")) {
            if (valueOfLayout.equalsIgnoreCase(getString(R.string.oneMore))) {
                first_lay.setVisibility(View.GONE);
                second_lay.setVisibility(View.VISIBLE);
                third_lay.setVisibility(View.GONE);
                valueOfLayout = getString(R.string.next);
                next.setText(getString(R.string.oneMore));

            } else if (valueOfLayout.equalsIgnoreCase("")) {

                valueOfLayout = " ";

                Toast.makeText(activity, "Press again to exit", Toast.LENGTH_SHORT).show();
            } else {
                super.onBackPressed();
            }
        } else {

            if (valueOfLayout.equalsIgnoreCase(getString(R.string.next))) {
                first_lay.setVisibility(View.VISIBLE);
                second_lay.setVisibility(View.GONE);
                third_lay.setVisibility(View.GONE);
                next.setText(getString(R.string.next));
                valueOfLayout = "";

            } else if (valueOfLayout.equalsIgnoreCase(getString(R.string.oneMore))) {
                first_lay.setVisibility(View.GONE);
                second_lay.setVisibility(View.VISIBLE);
                third_lay.setVisibility(View.GONE);
                valueOfLayout = getString(R.string.next);
                next.setText(getString(R.string.oneMore));

            } else if (valueOfLayout.equalsIgnoreCase("")) {
                valueOfLayout = " ";
                Toast.makeText(activity, "Press again to exit", Toast.LENGTH_SHORT).show();
            } else {
                super.onBackPressed();
            }
        }
    }


    public class MyAsync extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {

            if (!ConstantClass.SOCIAL_PROFILE.equalsIgnoreCase("")) {
                try {


                    URL url = new URL(ConstantClass.SOCIAL_PROFILE);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    return myBitmap;


                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginType = "";

    }
}
