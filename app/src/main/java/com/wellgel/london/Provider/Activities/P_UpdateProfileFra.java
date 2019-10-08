package com.wellgel.london.Provider.Activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.squareup.picasso.Picasso;
import com.wellgel.london.APIs.Customer_APIs;
import com.wellgel.london.APIs.Provider_APIs;
import com.wellgel.london.Customer.C_ConstantClass;
import com.wellgel.london.Customer.C_Product_model;
import com.wellgel.london.Customer.SerializeModelClasses.C_UpdateProfileSerial;
import com.wellgel.london.Provider.ModelSerialized.P_UpdateProfileSeialize;
import com.wellgel.london.Provider.ModelSerialized.ServiceSerilize;
import com.wellgel.london.Provider.ProviderAdapters.P_Adapter_Services;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class P_UpdateProfileFra extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    private ImageView user_profile;
    private TextView user_name, user_number, user_address, user_county, user_email, p_service_add_tv;
    private PreferencesShared shared;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 888, SELECT_FILE = 8888;
    private String picturePath = "";
    private File file;
    private P_UpdateProfileFra activity;
    private String IMAGE_DIRECTORY = "/WellgelLondon/";
    private ProgressDialog progressDoalog;
    private Button update_profile_btn;
    private String imageURL = C_ConstantClass.IMAGE_BASE_URL + "salon/profile_image/";
    private String st_name, st_profile, st_number, st_country, st_address;

    private String address = "";
    private double latitude = 0.0, longitude = 0.0;
    private boolean apiHit = false;
    private Chip addServiceMAin;
    private RecyclerView serviceRecycler;
    private List<C_Product_model> serviceList = new ArrayList<>();
    private C_Product_model serviceModel;
    private ArrayList<Integer> myIntegers;
    private LinearLayout serviceRecyclerLayout;
    private P_Adapter_Services adapter_services;

    public P_UpdateProfileFra() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_updateprofile);
        activity = this;
        init();

    }


    private void init() {

        shared = new PreferencesShared(activity);
        user_profile = findViewById(R.id.user_profile);
        user_name = findViewById(R.id.user_name);
        user_number = findViewById(R.id.user_number);
        user_address = findViewById(R.id.user_address);
        user_county = findViewById(R.id.user_country);
        user_email = findViewById(R.id.user_email);
        update_profile_btn = findViewById(R.id.update_profile);
        addServiceMAin = findViewById(R.id.add_service);
        p_service_add_tv = findViewById(R.id.p_service_add_tv);
        serviceRecycler = findViewById(R.id.serviceRecycler);
        serviceRecyclerLayout = findViewById(R.id.serviceRecyclerLayout);
        if (!shared.getString(ConstantClass.USER_NAME).equalsIgnoreCase("")) {

            user_name.setText(shared.getString(ConstantClass.USER_NAME));
            user_email.setText(shared.getString(ConstantClass.USER_Email));
            user_number.setText(shared.getString(ConstantClass.USER_Number));
            user_address.setText(shared.getString(ConstantClass.USER_Address));
            user_county.setText(shared.getString(ConstantClass.USER_Country));
            Picasso.with(activity).load(shared.getString("profile")).into(user_profile);
        }
        user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        update_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_name.setError(null);
                user_email.setError(null);
                user_number.setError(null);
                user_county.setError(null);
                user_address.setError(null);
                st_number = user_number.getText().toString().trim();
                st_country = user_county.getText().toString().trim();
                st_address = user_address.getText().toString().trim();
                st_name = user_name.getText().toString().trim();

                if (st_name.equalsIgnoreCase("")) {
                    user_name.setError(getString(R.string.invalidName));
                }
                if (st_number.length() < 10) {
                    user_number.setError(getString(R.string.invalidNumber));
                } else if (st_address.isEmpty()) {
                    user_address.setError(getString(R.string.invalidField));
                } else if (st_country.isEmpty()) {
                    user_county.setError(getString(R.string.invalidField));
                } else {
                    update_profile();
                }
            }
        });
        user_address.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getRawX() >= (user_address.getRight() - user_address.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        // your action here
                        if (checkPermissions()) {
                            user_address.setText(address);
                        }
                        return true;
                    }
                }
                return false;
            }
        });


        p_service_add_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!apiHit) serviceApi();
                else initiatePopupWindow(activity, serviceList);

            }
        });
        setUpGClient();

    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


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
        picturePath = file.getAbsolutePath();

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

        user_profile.setImageBitmap(thumbnail);
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(activity.getApplicationContext().getContentResolver(), data.getData());
                file = saveImage1(bm);
                picturePath = file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        user_profile.setImageBitmap(bm);
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

    public void serviceApi() {
        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        /*Create handle for the RetrofitInstance interface*/
        final Provider_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Provider_APIs.class);
        Call<ServiceSerilize> call = service.services();
        call.enqueue(new Callback<ServiceSerilize>() {
            @Override
            public void onResponse(Call<ServiceSerilize> call, Response<ServiceSerilize> response) {

                progressDoalog.dismiss();

                serviceList.clear();
                if (response.body() != null) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {

                            apiHit = true;
                            for (int i = 0; i < response.body().getData().size(); i++) {

                                serviceModel = new C_Product_model();
                                serviceModel.setServiceID(response.body().getData().get(i).getId());
                                serviceModel.setProductName(response.body().getData().get(i).getName());

                                serviceList.add(serviceModel);
                            }

                            adapter_services = new P_Adapter_Services(activity, serviceList);

                            initiatePopupWindow(activity, serviceList);
                        }


                    }
                }
            }

            @Override
            public void onFailure(Call<ServiceSerilize> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initiatePopupWindow(final Activity activity, final List<C_Product_model> list) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            final View layout = inflater.inflate(R.layout.p_custom_popup_services,
                    (ViewGroup) activity.findViewById(R.id.mainPop));
            // create a 300px width and 470px height PopupWindow
            final PopupWindow pw = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            // display the popup in the center
            pw.showAtLocation(layout, Gravity.TOP, 0, 0);
            final TextView addService = layout.findViewById(R.id.addServices_tv);
            ChipGroup chipGroup;

            RelativeLayout relativeLayout = (RelativeLayout) layout.findViewById(R.id.relativeLayout);
            chipGroup = new ChipGroup(this);
            chipGroup.setSingleSelection(false);


            for (final C_Product_model list1 : list) {
                final Chip chip = new Chip(this);
                ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(this, null, 0, R.style.Widget_MaterialComponents_Chip_Filter);
                chip.setChipDrawable(chipDrawable);
                chip.setText(list1.getProductName());
                chip.setTag(list1.getServiceID());


                if (list1.isChecked()) {
                    chip.setChipBackgroundColor(getResources().getColorStateList(R.color.pink));
                    chip.setChecked(true);

                }
                chip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        list1.setChecked(!list1.isChecked());

//
                        if (list1.isChecked()) {
                            chip.setChipBackgroundColor(getResources().getColorStateList(R.color.pink));
                        } else {
                            chip.setChipBackgroundColor(getResources().getColorStateList(R.color.grey));
                        }
                    }
                });

                chipGroup.addView(chip);

            }

            relativeLayout.addView(chipGroup);


            addService.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    if (getSelected().size() > 0) {
                        pw.dismiss();
                        myIntegers = new ArrayList<Integer>();
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < getSelected().size(); i++) {
                            stringBuilder.append(getSelected().get(i).getProductName());
                            myIntegers.add(getSelected().get(i).getServiceID());
                            stringBuilder.append(",");

                        }

                        serviceRecyclerLayout.setVisibility(View.VISIBLE);
                        p_service_add_tv.setVisibility(View.GONE);
                        P_Adapter_Services adapter_services = new P_Adapter_Services(activity, getSelected());
                        serviceRecycler.setAdapter(adapter_services);
                        addServiceMAin.setText(getSelected().size() + "");
                        addServiceMAin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!apiHit) serviceApi();
                                else initiatePopupWindow(activity, serviceList);

                            }
                        });

//                        p_service_add_tv.setText(stringBuilder.toString());
//                        p_service_add_tv.setBackground(getResources().getDrawable(R.drawable.capsule_button_shape_grey));


                    } else {
                        Toast.makeText(activity, "" + getString(R.string.adservice), Toast.LENGTH_SHORT).show();
                    }

                }
            });

            pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    pw.dismiss();
                }
            });

        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    public List<C_Product_model> getSelected() {
        ArrayList<C_Product_model> selected = new ArrayList<>();
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).isChecked()) {
                selected.add(serviceList.get(i));
            }
        }
        return selected;
    }

    //     registration to server APi
    public void update_profile() {
        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Updating your profile...");
        progressDoalog.setCancelable(false);
        progressDoalog.show();

        MultipartBody.Part userImg = null;
        if (file != null) {
            userImg = prepareFilePart("profile_image", file.getName(), file);
//            userImg = MultipartBody.Part.createFormData( "profile_image",photoFile.getName(), RequestBody.create(MediaType.parse("image/*"), photoFile));
        }


        Map<String, RequestBody> requestBodyMap = new HashMap<>();
//        requestBodyMap.put("profile_image", fileReqBody);
        requestBodyMap.put("Content-Type", RequestBody.create(MediaType.parse("multipart/form-data"), "application/x-www-form-urlencoded"));
        requestBodyMap.put("name", RequestBody.create(MediaType.parse("multipart/form-data"), st_name));
        requestBodyMap.put("phone", RequestBody.create(MediaType.parse("multipart/form-data"), st_number));
        requestBodyMap.put("address", RequestBody.create(MediaType.parse("multipart/form-data"), st_address));
        requestBodyMap.put("country", RequestBody.create(MediaType.parse("multipart/form-data"), st_country));
        requestBodyMap.put("latitude", RequestBody.create(MediaType.parse("multipart/form-data"), latitude + ""));
        requestBodyMap.put("longitude", RequestBody.create(MediaType.parse("multipart/form-data"), longitude + ""));
        requestBodyMap.put("service_ids", RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(myIntegers)));

        requestBodyMap.put("device_type", RequestBody.create(MediaType.parse("multipart/form-data"), ConstantClass.DEVICETYPE));
        requestBodyMap.put("device_token", RequestBody.create(MediaType.parse("multipart/form-data"), shared.getString(ConstantClass.DEVICE_TOKEN)));

        /*Create handle for the RetrofitInstance interface*/
        Provider_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Provider_APIs.class);
        Call<P_UpdateProfileSeialize> call = service.updateProfilProvider("Bearer " + shared.getString("token"), userImg, requestBodyMap);
        call.enqueue(new Callback<P_UpdateProfileSeialize>() {
            @Override
            public void onResponse(Call<P_UpdateProfileSeialize> call, Response<P_UpdateProfileSeialize> response) {


                progressDoalog.dismiss();
                if (response.body() != null) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
//                            if (file != null) delete(activity, file);
                            shared.setString("profile", imageURL + response.body().getData().getUser().getProfileImage());
                            shared.setString(ConstantClass.USER_NAME, response.body().getData().getUser().getName());
                            shared.setString(ConstantClass.USER_Address, response.body().getData().getUser().getAddress());
                            shared.setString(ConstantClass.USER_Country, response.body().getData().getUser().getCountry());
                            shared.setString(ConstantClass.USER_Number, response.body().getData().getUser().getPhone());
                            finish();

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
            public void onFailure(Call<P_UpdateProfileSeialize> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected MultipartBody.Part prepareFilePart(String partName, String fileName, File file) {
        if (file == null || !file.exists())
            return null;


        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        if (TextUtils.isEmpty(fileName))
            fileName = file.getName();

        return MultipartBody.Part.createFormData(partName, fileName, requestFile);
    }

    public boolean delete(final Context context, final File file) {
        final String where = MediaStore.MediaColumns.DATA + "=?";
        final String[] selectionArgs = new String[]{
                file.getAbsolutePath()
        };
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri filesUri = MediaStore.Files.getContentUri("external");

        contentResolver.delete(filesUri, where, selectionArgs);

        if (file.exists()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    Files.delete(Paths.get(String.valueOf(file)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            contentResolver.delete(filesUri, where, selectionArgs);
        }
        return !file.exists();
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
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
    //this function i
}
