package com.wellgel.london.Customer.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.wellgel.london.APIs.Customer_APIs;
import com.wellgel.london.Customer.Adapters.C_SalonListAdapetr;
import com.wellgel.london.Customer.SerializeModelClasses.C_SalonListSerial;
import com.wellgel.london.Customer.SerializeModelClasses.C_UpdateProfileSerial;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.PreferencesShared;
import com.wellgel.london.UtilClasses.Retrofit.RetrofitClientInstance;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class C_NearBySalonAct extends AppCompatActivity implements OnMapReadyCallback, C_SalonListAdapetr.ONSALONCLICK, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private SupportMapFragment map;
    private ProgressDialog progressDoalog;
    private GoogleMap mGoogleMap;

    private C_NearBySalonAct activity;
    private PreferencesShared shared;
    private RecyclerView salonRecycler;
    private C_SalonListAdapetr adapetr;
    ArrayList<C_SalonListSerial.Datum> markersArray = new ArrayList<>();
    private LatLng latng;
    private int value;
    private ImageView salon_sort, backSalon;

    private LinearLayout layoutBottomSheet, bottomsheet_address, bottomsheet_sort;
    private BottomSheetBehavior sheetBehavior, sheetBehavior_address, sheetBehavior_sort;
    private TextView sort_rating, sort_distance, locationName, location_address, updateAddress, nameOfaddHolder, fullAddress;
    private EditText edit_address;

    private final int REQUEST_CHECK_SETTINGS_GPS = 0x3;
    private final int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x4;
    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private String address = "";
    private double latitude = 0.0, longitude = 0.0;
    private AutoCompleteTextView chooseSalon;
    int sort_count = 1;
    private boolean isBottomSort = false;
    private boolean isBottomList = false;
    private boolean isBottomaddress = false;
    private RelativeLayout addLocation;

    @Override
    public void onBackPressed() {

        if (isBottomSort) {
            isBottomSort = false;
            bottomSheetUpDown_sort();
        } else if (isBottomaddress) {
            isBottomaddress = false;
            bottomSheetUpDown_address();
        } else if (isBottomList) {
            isBottomList = false;
            bottomSheetUpDown();
        } else {
            super.onBackPressed();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__near_by_salon);
        activity = this;
        shared = new PreferencesShared(activity);


        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
                R.id.map));
        if (map != null) {
            map.getMapAsync(this);
        }


        salonRecycler = findViewById(R.id.salonRecycelr);
        backSalon = findViewById(R.id.backSalon);
        nameOfaddHolder = findViewById(R.id.nameOfaddHolder);
        updateAddress = findViewById(R.id.updateAddress);
        locationName = findViewById(R.id.locationName);
        location_address = findViewById(R.id.location_address);
        fullAddress = findViewById(R.id.fullAddress);
        edit_address = findViewById(R.id.edit_address);
        chooseSalon = findViewById(R.id.chooseSalon);
        addLocation = findViewById(R.id.addLocation);
        salon_sort = findViewById(R.id.salon_sort);
        salonRecycler.setLayoutManager(new LinearLayoutManager(this));
        layoutBottomSheet = findViewById(R.id.bottomsheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        bottomsheet_address = findViewById(R.id.bottomsheet_address);
        sheetBehavior_address = BottomSheetBehavior.from(bottomsheet_address);

        bottomsheet_sort = findViewById(R.id.bottomsheet_sort);
        sheetBehavior_sort = BottomSheetBehavior.from(bottomsheet_sort);
        bottomSheetBehavior();
        bottomSheetBehavior_address();
        bottomSheetBehavior_sort();
        backSalon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetUpDown_address();
            }
        });
        bottomsheet_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetUpDown_address();
            }
        });
        salon_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetUpDown_sort();
            }
        });


        setText();
        setUpGClient();

        updateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edit_address.getText().toString().isEmpty()) {
                    Toast.makeText(activity, "" + getString(R.string.enterAddress), Toast.LENGTH_SHORT).show();
                } else {
                    update_profile();
                }
            }
        });

    }

    private void bottomSheetUpDown() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

    }

    private void bottomSheetUpDown_address() {
        if (sheetBehavior_address.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior_address.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior_address.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

    }

    private void bottomSheetUpDown_sort() {
        if (sheetBehavior_sort.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior_sort.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior_sort.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        bottomOnClickSort();

    }


    private void setText() {
//        edit_address.setText(shared.getString(ConstantClass.USER_Address));

        edit_address.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onTouch(View view, MotionEvent event) {

//                final int DRAWABLE_RIGHT = 2;
//
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (event.getRawX() >= (edit_address.getRight() - edit_address.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
//
//                        // your action her

                if (checkPermissions())
                    edit_address.setText(address);

//                        return true;
//                    }
//                }
                return true;
            }
        });


        chooseSalon.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (chooseSalon.getRight() - chooseSalon.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        // your action her
                        salonList(mGoogleMap, chooseSalon.getText().toString(), "distance");

                        return true;
                    }
                }
                return false;
            }
        });

        chooseSalon.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    salonList(mGoogleMap, chooseSalon.getText().toString(), "distance");
                    return true;
                }
                return false;
            }
        });
        fullAddress.setText(shared.getString(ConstantClass.USER_Address));
        location_address.setText(shared.getString(ConstantClass.USER_Address));
        nameOfaddHolder.setText(shared.getString(ConstantClass.USER_NAME));


        locationName.setText(shared.getString(ConstantClass.USER_Country));
//        StringUtils.capitalize(shared.getString(ConstantClass.USER_Country));

    }


    private void checkClick() {
        sort_distance.setTypeface(null, Typeface.NORMAL);
        sort_rating.setTypeface(null, Typeface.NORMAL);


        //change color
        sort_distance.setTextColor(getResources().getColor(R.color.black));
        sort_rating.setTextColor(getResources().getColor(R.color.black));

    }

    private void bottomOnClickSort() {

        sort_distance = findViewById(R.id.sort_distance);
        sort_rating = findViewById(R.id.sort_rating);

        if (sort_count == 1) {
            checkClick();
            sort_distance.setTypeface(null, Typeface.BOLD);
            sort_distance.setTextColor(getResources().getColor(R.color.pink));

        } else if (sort_count == 2) {
            checkClick();
            sort_rating.setTypeface(null, Typeface.BOLD);
            sort_rating.setTextColor(getResources().getColor(R.color.pink));

        }
        sort_distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort_distance.setTypeface(null, Typeface.BOLD);
                bottomSheetUpDown_sort();
                sort_count = 1;
                salonList(mGoogleMap, "", "distance");

            }
        });
        sort_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort_rating.setTypeface(null, Typeface.BOLD);
                bottomSheetUpDown_sort();
                sort_count = 2;
                salonList(mGoogleMap, "", "rating");

            }
        });


    }

    private void bottomSheetBehavior() {
        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        isBottomList = true;
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        isBottomList = false;
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:

                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void bottomSheetBehavior_sort() {
        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehavior_sort.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        isBottomSort = true;
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        isBottomSort = false;
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:

                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void bottomSheetBehavior_address() {
        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehavior_address.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        isBottomaddress = true;
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        isBottomaddress = false;
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:

                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    public void salonList(GoogleMap mGoogleMap, String search, String sort) {
        // initialize file here

        /*Create handle for the RetrofitInstance interface*/
        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Getting near by salons....");
        progressDoalog.show();
        Customer_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Customer_APIs.class);
        Call<C_SalonListSerial> call = service.salonList("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"), "500", search, sort);
        call.enqueue(new Callback<C_SalonListSerial>() {


            @Override
            public void onResponse(Call<C_SalonListSerial> call, Response<C_SalonListSerial> response) {


                progressDoalog.dismiss();
                if (response.body() != null) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {

                            value = response.body().getData().size();

                            adapetr = new C_SalonListAdapetr(activity, response.body().getData(), activity);
                            salonRecycler.setAdapter(adapetr);

                            if (value > 0) {
                                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.marker);
                                Bitmap b = bitmapdraw.getBitmap();
                                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 60, 60, false);
                                for (int i = 0; i < value; i++) {
                                    latng = new LatLng(Double.parseDouble(response.body().getData().get(i).getLatitude()), Double.parseDouble(response.body().getData().get(i).getLongitude()));
                                    mGoogleMap.addMarker(new MarkerOptions().position(latng).title("")
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

                                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latng, 6f);
                                    mGoogleMap.animateCamera(update);
                                }


                            }

                        }
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String errorMessage = jObjError.getJSONObject("error").getJSONObject("error_message").getJSONArray("message").getString(0);
                        Toast.makeText(C_NearBySalonAct.this, "" + errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(C_NearBySalonAct.this, "" + e, Toast.LENGTH_SHORT).show();
                    }
                }

            }


            @Override
            public void onFailure(Call<C_SalonListSerial> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        salonList(mGoogleMap, "", "distance");

    }


    @Override
    public void getSalon(int ID, String name, String address, String startTime, String endTime, int rating, String image, String img1, String img2, String img3) {


        ConstantClass.SALON_ID = ID + "";
        Intent intent = new Intent(activity, C_SalonDetailAct.class);


        intent.putExtra("salon_name", name);
        intent.putExtra("salon_address", address);
        intent.putExtra("salon_startTime", startTime);
        intent.putExtra("salon_endTime", endTime);
        intent.putExtra("salon_rating", rating);
        intent.putExtra("salon_Image", image);
        if (!img1.contains("null"))
            intent.putExtra("salon_Image1", img1);
        if (!img2.contains("null"))
            intent.putExtra("salon_Image2", img2);
        if (!img3.contains("null"))
            intent.putExtra("salon_Image3", img3);

        startActivity(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHECK_SETTINGS_GPS) {
                getMyLocation();
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

    public void update_profile() {
        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Updating your profile...");
        progressDoalog.setCancelable(false);
        progressDoalog.show();

        MultipartBody.Part userImg;
//            userImg = MultipartBody.Part.createFormData( "profile_image",photoFile.getName(), RequestBody.create(MediaType.parse("image/*"), photoFile));

        RequestBody mFile1 = RequestBody.create(MediaType.parse("image/*"), "");
        userImg = MultipartBody.Part.createFormData("profile_image", "", mFile1);

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
//        requestBodyMap.put("profile_image", fileReqBody);
        requestBodyMap.put("Content-Type", RequestBody.create(MediaType.parse("multipart/form-data"), "application/x-www-form-urlencoded"));
        requestBodyMap.put("address", RequestBody.create(MediaType.parse("multipart/form-data"), address));
        requestBodyMap.put("latitude", RequestBody.create(MediaType.parse("multipart/form-data"), latitude + ""));
        requestBodyMap.put("longitude", RequestBody.create(MediaType.parse("multipart/form-data"), longitude + ""));
        requestBodyMap.put("device_type", RequestBody.create(MediaType.parse("multipart/form-data"), ConstantClass.DEVICETYPE));
        requestBodyMap.put("device_token", RequestBody.create(MediaType.parse("multipart/form-data"), shared.getString(ConstantClass.DEVICE_TOKEN)));



        /*Create handle for the RetrofitInstance interface*/
        Customer_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Customer_APIs.class);
        Call<C_UpdateProfileSerial> call = service.updateProfil("Bearer " + shared.getString("token"), userImg, requestBodyMap);
        call.enqueue(new Callback<C_UpdateProfileSerial>() {
            @Override
            public void onResponse(Call<C_UpdateProfileSerial> call, Response<C_UpdateProfileSerial> response) {


                progressDoalog.dismiss();
                if (response.body() != null) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
//                            if (file != null) delete(activity, file);
                            shared.setString(ConstantClass.USER_NAME, response.body().getData().getUser().getName());
                            shared.setString(ConstantClass.USER_Address, response.body().getData().getUser().getAddress());
                            shared.setString(ConstantClass.USER_Country, response.body().getData().getUser().getCountry());
                            setText();
                            bottomSheetUpDown_address();

                        } else {

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

                    }
                }
            }

            @Override
            public void onFailure(Call<C_UpdateProfileSerial> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
