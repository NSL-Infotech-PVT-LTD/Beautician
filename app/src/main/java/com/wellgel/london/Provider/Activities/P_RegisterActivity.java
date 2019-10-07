package com.wellgel.london.Provider.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.wellgel.london.Customer.Activities.C_DashboardAct;
import com.wellgel.london.Customer.C_ConstantClass;
import com.wellgel.london.Customer.C_Product_model;
import com.wellgel.london.Provider.ModelSerialized.P_Registration_serial;
import com.wellgel.london.Provider.ModelSerialized.P_SubscripSerial;
import com.wellgel.london.Provider.ModelSerialized.ServiceSerilize;
import com.wellgel.london.Provider.ProviderAdapters.P_Adapter_Services;
import com.wellgel.london.Provider.ProviderAdapters.P_SubscriptionAdapter;
import com.wellgel.london.APIs.Provider_APIs;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wellgel.london.Provider.Activities.P_LoginActivity.loginType;

public class P_RegisterActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, P_SubscriptionAdapter.SubscribeClick, P_SubscriptionAdapter.SUBS_DETAIL {
    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;
    private LinearLayout first_lay, second_lay, third_lay;
    private RecyclerView serviceRecycler;
    private Button next, p_registr_back_btn;
    private P_RegisterActivity activity;
    private int REQUEST_CAMERA = 777, SELECT_FILE = 7777;
    private String picturePath = "";
    private String IMAGE_DIRECTORY = "/WellgelLondon/";
    private TextView show_selctStartTime, show_selctEndTime, p_service_add_tv;
    private File file;
    private String userChoosenTask;
    private CircleImageView profileImage, profileImage2, profileImage3, profileImage4;
    private ProgressDialog progressDoalog;
    private EditText c_registr_name_ed, c_registr_email_ed, c_registr_country_ed;
    private EditText c_registr_address_ed, c_registr_pass_ed, c_registr_numer_ed;
    private String st_name, st_email, st_number, st_country, st_address, st_pass;
    private List<C_Product_model> serviceList = new ArrayList<>();
    private P_Adapter_Services adapter_services;
    private C_Product_model serviceModel;
    private String address = "";
    private double latitude = 0.0, longitude;
    private String valueOfLayout = "";
    private PreferencesShared shared;
    private Chip addServiceMAin;
    private LinearLayout serviceRecyclerLayout;
    private String imageURL = C_ConstantClass.IMAGE_BASE_URL + "salon/profile_image/";

    private ArrayList<Integer> myIntegers;
    private CardView subsCard;
    private RecyclerView subs_recycler;
    private P_SubscriptionAdapter subscriptionAdapter;
    private String subscribedName = "";
    private int subscriptionID = 0;
    EditText cardNumberEditText, cardDate, cardCVV, cardHolder;
    public static final String PUBLISHABLE_KEY = "pk_test_EofuWsjaHTjaOaJ3ZdmuyDZ1009cv4oXN7";
    protected Card cardToSave;
    Stripe stripe;
    private int month, year;
    private String datePattern = "\\d{2}/\\d{4}", currentString;


    private ArrayList<String> imgList = new ArrayList<>();
    String backString = "";
    String imageEncoded = "";
    MultipartBody.Part userImg = null;
    MultipartBody.Part portFolioImage1 = null, portFolioImage2 = null, portFolioImage3 = null, portFolioImage4 = null;
    private ImageView imageView;
    private int position;
    private List<File> imagesSelected = new ArrayList<>();
    private List<MultipartBody.Part> imgPortfolio;
    private boolean apiHit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p__register);

        init();
    }

    private void init() {
        activity = this;
        shared = new PreferencesShared(activity);

        first_lay = findViewById(R.id.first_layout);
        second_lay = findViewById(R.id.second_layout);
        third_lay = findViewById(R.id.third_layout);
        next = findViewById(R.id.c_registr_next_btn);
        p_registr_back_btn = findViewById(R.id.p_registr_back_btn);
        p_service_add_tv = findViewById(R.id.p_service_add_tv);
        subs_recycler = findViewById(R.id.subs_recycler);
        subsCard = findViewById(R.id.subsCard);

        c_registr_address_ed = findViewById(R.id.c_registr_address_ed);
        c_registr_name_ed = findViewById(R.id.c_registr_Salname_ed);
        c_registr_email_ed = findViewById(R.id.c_registr_email_ed);
        c_registr_country_ed = findViewById(R.id.c_registr_country_ed);
        c_registr_pass_ed = findViewById(R.id.c_registr_pass_ed);
        c_registr_numer_ed = findViewById(R.id.c_registr_numer_ed);
        addServiceMAin = findViewById(R.id.add_service);
        serviceRecycler = findViewById(R.id.serviceRecycler);
        serviceRecyclerLayout = findViewById(R.id.serviceRecyclerLayout);

        show_selctStartTime = findViewById(R.id.show_selctStartTime);
        show_selctEndTime = findViewById(R.id.show_selctEndTime);

        serviceRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        subs_recycler.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        profileImage = findViewById(R.id.profileImage);
        profileImage2 = findViewById(R.id.profileImage2);
        profileImage3 = findViewById(R.id.profileImage3);
        profileImage4 = findViewById(R.id.profileImage4);

        cardNumberEditText = findViewById(R.id.cardNumberEditText);
        cardDate = findViewById(R.id.carDate);
        cardCVV = findViewById(R.id.cardCVV);
        cardHolder = findViewById(R.id.cardHolder);
        stripe = new Stripe(this);


        cardNumberEditText.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (cardNumberEditText.getText().length() == 19)
                    cardDate.requestFocus();
                return false;
            }
        });

        cardDate.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (cardDate.getText().toString().matches(datePattern))
                    cardCVV.requestFocus();
                return false;

            }
        });

        cardCVV.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (cardCVV.getText().length() == 3)
                    cardHolder.requestFocus();
                return false;
            }
        });


        checkCard(cardNumberEditText);
        checkDate(cardDate);
        subscription();


        if (loginType.equalsIgnoreCase("facebook") || loginType.equalsIgnoreCase("google")) {
            first_lay.setVisibility(View.GONE);
            second_lay.setVisibility(View.VISIBLE);
            third_lay.setVisibility(View.GONE);
            valueOfLayout = getString(R.string.next);
            next.setText(getString(R.string.oneMore));


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

                        // your action here
                        if (checkPermissions()) {

                            c_registr_address_ed.setText(address);
                        }
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

                        fun();
                    }
                } else {
                    if (getNextClick.equalsIgnoreCase(getString(R.string.next))) {

                        if (validation1()) {
                            first_lay.setVisibility(View.GONE);
                            second_lay.setVisibility(View.VISIBLE);
                            third_lay.setVisibility(View.GONE);
                            valueOfLayout = getString(R.string.next);
                            next.setText(getString(R.string.oneMore));
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
                        fun();
                    }
                }
            }
        });

        p_service_add_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!apiHit) serviceApi();
                else initiatePopupWindow(activity, serviceList);

            }
        });
        p_registr_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, P_LoginActivity.class));
                finish();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (checktimings(show_selctStartTime.getText().toString(), show_selctEndTime.getText().toString())) {
                position = 1;
                selectImage();
            }
        });
        profileImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (checktimings(show_selctStartTime.getText().toString(), show_selctEndTime.getText().toString())) {
                position = 2;
                selectImage();
            }
        });
        profileImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (checktimings(show_selctStartTime.getText().toString(), show_selctEndTime.getText().toString())) {
                position = 3;
                selectImage();
            }
        });
        profileImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (checktimings(show_selctStartTime.getText().toString(), show_selctEndTime.getText().toString())) {
                position = 4;
                selectImage();
            }
        });


        show_selctStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedHour > 12) {
                            show_selctStartTime.setText(String.valueOf(selectedHour - 12) + ":" + (String.valueOf(selectedMinute) + " PM"));
                        } else if (selectedHour == 12) {
                            show_selctStartTime.setText("12" + ":" + (String.valueOf(selectedMinute) + " PM"));
                        } else if (selectedHour < 12) {
                            if (selectedHour != 0) {
                                show_selctStartTime.setText(String.valueOf(selectedHour) + ":" + (String.valueOf(selectedMinute) + " AM"));
                            } else {
                                show_selctStartTime.setText("12" + ":" + (String.valueOf(selectedMinute) + " AM"));
                            }
                        }
//                        selctStartTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time


                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        show_selctEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedHour > 12) {
                            show_selctEndTime.setText(String.valueOf(selectedHour - 12) + ":" + (String.valueOf(selectedMinute) + " PM"));
                        } else if (selectedHour == 12) {
                            show_selctEndTime.setText("12" + ":" + (String.valueOf(selectedMinute) + " PM"));
                        } else if (selectedHour < 12) {
                            if (selectedHour != 0) {
                                show_selctEndTime.setText(String.valueOf("0" + selectedHour) + ":" + (String.valueOf(selectedMinute) + " AM"));
                            } else {
                                show_selctEndTime.setText("12" + ":" + (String.valueOf(selectedMinute) + " AM"));
                            }
                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


    }

    private void fun() {

        if (picturePath.equalsIgnoreCase("")) {
            Toast.makeText(activity, "" + getString(R.string.invalidProfile), Toast.LENGTH_SHORT).show();
        } else if ((getSelected().size() > 0)) {
            if (subscribedName.equalsIgnoreCase("free")) {
                if (loginType.equalsIgnoreCase("facebook") || loginType.equalsIgnoreCase("google")) {
                    registrationAPI(ConstantClass.SOCIAL_NAME, ConstantClass.SOCIAL_EMAIL, ConstantClass.SOCIAL_PASSWORD, "");
                } else {
                    registrationAPI(st_name, st_email, st_pass, "");

                }
            } else {
                datePattern = "\\d{2}/\\d{4}";
                currentString = cardDate.getText().toString();
                if (cardNumberEditText.getText().toString().trim().isEmpty()) {
                    cardNumberEditText.setError(getString(R.string.invalidField));
                } else if (cardDate.getText().toString().trim().isEmpty()) {
                    cardDate.setError(getString(R.string.invalidField));
                } else if (!currentString.matches(datePattern)) {
                    cardDate.setError(getString(R.string.invalidDate));

                } else if (cardCVV.getText().toString().trim().isEmpty()) {
                    cardCVV.setError(getString(R.string.invalidField));
                } else {

                    String[] separated = currentString.split("/");

                    if ((!separated[0].isEmpty()) && (!separated[1].isEmpty())) {
                        month = Integer.parseInt(separated[0].trim());

                        year = Integer.parseInt(separated[1].trim());
                    }

                    buy();
                }
            }
        } else if (subscribedName.equalsIgnoreCase("")) {
            Toast.makeText(activity, "" + getString(R.string.choose_subscription), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "" + getString(R.string.selectSerive), Toast.LENGTH_SHORT).show();
        }
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

//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE);


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


            if (position == 1) {
                file.createNewFile();
                fo = new FileOutputStream(file);
                fo.write(bytes.toByteArray());
                fo.close();
                profileImage.setImageBitmap(thumbnail);
                portFolioImage1 = MultipartBody.Part.createFormData("profile_image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

            }

            if (position == 2) {
                file.createNewFile();
                fo = new FileOutputStream(file);
                fo.write(bytes.toByteArray());
                fo.close();
                profileImage2.setImageBitmap(thumbnail);
                portFolioImage2 = MultipartBody.Part.createFormData("image_1", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

            }
            if (position == 3) {
                file.createNewFile();
                fo = new FileOutputStream(file);
                fo.write(bytes.toByteArray());
                fo.close();
                profileImage3.setImageBitmap(thumbnail);
                portFolioImage3 = MultipartBody.Part.createFormData("image_2", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

            }
            if (position == 4) {
                file.createNewFile();
                fo = new FileOutputStream(file);
                fo.write(bytes.toByteArray());
                fo.close();

                profileImage4.setImageBitmap(thumbnail);
                portFolioImage4 = MultipartBody.Part.createFormData("image_3", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;

//
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        if (data.getData() != null) {         //on Single image selected
            if (data != null) {
                try {


//            Uri mImageUri = data.getData();
//
//            // Get the cursor
//            Cursor cursor = getContentResolver().query(mImageUri, filePathColumn, null, null, null);
//            // Move to first row
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            imageEncoded = cursor.getString(columnIndex);
//            cursor.close();
                    if (position == 1) {
                        bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                        file = saveImage1(bm);
                        picturePath = saveImage(bm);

                        profileImage.setImageBitmap(bm);
                        portFolioImage1 = MultipartBody.Part.createFormData("profile_image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

                    }

                    if (position == 2) {
                        bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                        file = saveImage1(bm);
                        picturePath = saveImage(bm);
                        profileImage2.setImageBitmap(bm);
                        portFolioImage2 = MultipartBody.Part.createFormData("image_1", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

                    }
                    if (position == 3) {
                        bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                        file = saveImage1(bm);
                        picturePath = saveImage(bm);
                        profileImage3.setImageBitmap(bm);
                        portFolioImage3 = MultipartBody.Part.createFormData("image_2", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

                    }
                    if (position == 4) {
                        bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                        file = saveImage1(bm);
                        picturePath = saveImage(bm);


                        profileImage4.setImageBitmap(bm);
                        portFolioImage4 = MultipartBody.Part.createFormData("image_3", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        else {                              //on multiple image selected
//            if (data.getClipData() != null) {
//                ClipData mClipData = data.getClipData();
//                ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
//                if (mClipData.getItemCount() == 0) {
//                    Toast.makeText(activity, "You haven't select any photo", Toast.LENGTH_SHORT).show();
//                } else {
//                    if (mClipData.getItemCount() > 4) {
//                        Toast.makeText(activity, "Please select 4 or less photos", Toast.LENGTH_SHORT).show();
//                    } else {
//                        for (int i = 0; i < mClipData.getItemCount(); i++) {
//
//                            ClipData.Item item = mClipData.getItemAt(i);
//                            Uri uri = item.getUri();
//                            mArrayUri.add(uri);
//                            // Get the cursor
//                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
//                            // Move to first row
//                            cursor.moveToFirst();
//
//                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                            imageEncoded = cursor.getString(columnIndex);
//
//
//                            imgList.add(imageEncoded);
//
//                            cursor.close();
//                            if (imgList.size() > 4) {
//                                imgList.clear();
//
//                                imgList.add(imageEncoded);
//                                cursor.close();
//
//                            }
//                        }
//
//                        for (int i = 0; i < imgList.size(); i++) {
//                            Log.v("MainActivity", "Selected Images" + imgList.get(i));
//                        }
//
//                        if (imgList.size() > 0 && (imgList.get(0) != null))
//                            profileImage.setImageURI(Uri.parse(imgList.get(0)));
//                        if (imgList.size() > 1 && (imgList.get(1) != null))
//                            profileImage2.setImageURI(Uri.parse(imgList.get(1)));
//                        if (imgList.size() > 2 && (imgList.get(2) != null))
//                            profileImage3.setImageURI(Uri.parse(imgList.get(2)));
//                        if (imgList.size() > 3 && (imgList.get(3) != null))
//                            profileImage4.setImageURI(Uri.parse(imgList.get(3)));
//
//                    }
//                }
//            }
//        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            if (data.getClipData() != null) {
//                int count = data.getClipData().getItemCount();
//                int currentItem = 0;
//                while (currentItem < count) {
//                    //do something with the image (save it to some directory or whatever you need to do with it here)
//                    try {
//                        bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getClipData().getItemAt(currentItem).getUri());
//                        file = saveImage1(bm);
//                        picturePath = saveImage(bm);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    currentItem = currentItem + 1;
//
//
//                }
//            } else if (data.getData() != null) {
//
//                if (data != null) {
//                    try {
//                        bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
//                        file = saveImage1(bm);
//                        picturePath = saveImage(bm);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//
//            }
//        }
//
//        imgList.add(file);


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
    public void registrationAPI(String st_name, String st_email, String st_pass, String token) {
        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Registering....");
        progressDoalog.show();

        // initialize file here
        MultipartBody.Part f = null;
        if (file != null) {
            f = MultipartBody.Part.createFormData("profile_image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        }

        /*Create handle for the RetrofitInstance interface*/
        Provider_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Provider_APIs.class);
        Call<P_Registration_serial> call = service.registration(portFolioImage1, portFolioImage2, portFolioImage3, portFolioImage4, st_name, st_email, st_pass, st_number, latitude + "", longitude + "", "android",shared.getString(ConstantClass.DEVICE_TOKEN), st_address, String.valueOf(myIntegers), token, subscriptionID + "", show_selctStartTime.getText().toString(), show_selctEndTime.getText().toString(), st_country);
        call.enqueue(new Callback<P_Registration_serial>() {
            @Override
            public void onResponse(Call<P_Registration_serial> call, Response<P_Registration_serial> response) {
                progressDoalog.dismiss();


                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            valueOfLayout = "";


                            shared.setBoolean(ConstantClass.isProviderLoggeIn, true);
                            shared.setBoolean(ConstantClass.isCustomerLoggeIn, false);

                            shared.setString("profile",imageURL + response.body().getData().getUser().getProfileImage());
                            shared.setString("token", response.body().getData().getToken());
                            shared.setString(ConstantClass.USER_NAME, response.body().getData().getUser().getName());
                            shared.setString(ConstantClass.USER_Address, response.body().getData().getUser().getAddress());
                            shared.setString(ConstantClass.USER_Country, response.body().getData().getUser().getCountry());
                            shared.setString(ConstantClass.USER_Number, response.body().getData().getUser().getPhone());
                            shared.setString(ConstantClass.USER_Email, response.body().getData().getUser().getEmail());
                            shared.setString(ConstantClass.ROLL_PLAY, ConstantClass.ROLL_PROVIDER);
                            startActivity(new Intent(activity, C_DashboardAct.class));
                            finish();
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
            public void onFailure(Call<P_Registration_serial> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void subscription() {
        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        // initialize file here

        Provider_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Provider_APIs.class);
        Call<P_SubscripSerial> call = service.subscription();
        call.enqueue(new Callback<P_SubscripSerial>() {
            @Override
            public void onResponse(Call<P_SubscripSerial> call, Response<P_SubscripSerial> response) {
                progressDoalog.dismiss();


                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {


                            subscriptionAdapter = new P_SubscriptionAdapter(activity, response.body().getData(), activity, activity);
                            subs_recycler.setAdapter(subscriptionAdapter);
                        }


                    } else {
                    }
                }
            }

            @Override
            public void onFailure(Call<P_SubscripSerial> call, Throwable t) {
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

    @Override
    public void getSubscribed(int id, String name) {

        subscriptionID = id;
        subscribedName = name;
        if (name.equalsIgnoreCase("free")) {
            subsCard.setVisibility(View.GONE);
        } else {
            subsCard.setVisibility(View.VISIBLE);
        }
    }


    private void buy() {
        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Validate your card....");
        progressDoalog.setCancelable(false);
        progressDoalog.show();


        cardToSave = new Card(
                cardNumberEditText.getText().toString(), //card number
                month, //expMonth
                year,//expYear
                cardCVV.getText().toString());//cvc

        boolean validation = cardToSave.validateCard();
        if (validation) {
            new Stripe(this).createToken(
                    cardToSave,
                    PUBLISHABLE_KEY,
                    new TokenCallback() {
                        @Override
                        public void onError(Exception error) {
                            progressDoalog.dismiss();
                            Log.d("Stripe", error.toString());
                        }

                        @Override
                        public void onSuccess(Token token) {
                            //  charge(token);

                            progressDoalog.dismiss();
                            if (loginType.equalsIgnoreCase("facebook") || loginType.equalsIgnoreCase("google")) {
                                registrationAPI(ConstantClass.SOCIAL_NAME, ConstantClass.SOCIAL_EMAIL, ConstantClass.SOCIAL_PASSWORD, token.getId());
                            } else {
                                registrationAPI(st_name, st_email, st_pass, token.getId());

                            }

                        }
                    });
        } else if (!cardToSave.validateNumber()) {
            progressDoalog.dismiss();

            Toast.makeText(activity, "" + getString(R.string.invalidCardDetail), Toast.LENGTH_SHORT).show();
        } else if (!cardToSave.validateExpiryDate()) {
            progressDoalog.dismiss();
            Toast.makeText(activity, "" + getString(R.string.invalidCardDetail), Toast.LENGTH_SHORT).show();
            Log.d("Stripe", "The expiration date that you entered is invalid");
        } else if (!cardToSave.validateCVC()) {
            progressDoalog.dismiss();
            Toast.makeText(activity, "" + getString(R.string.invalidCardDetail), Toast.LENGTH_SHORT).show();
            Log.d("Stripe", "The CVC code that you entered is invalid");
        } else {
            Log.d("Stripe", "The card details that you entered are invalid");
            Toast.makeText(activity, "" + getString(R.string.invalidCardDetail), Toast.LENGTH_SHORT).show();
            progressDoalog.dismiss();
        }
    }


    private void checkCard(EditText cardNumberEditText) {
        cardNumberEditText.addTextChangedListener(new TextWatcher() {

            private static final int TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
            private static final int TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
            private static final int DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
            private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
            private static final char DIVIDER = ' ';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // noop
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    s.replace(0, s.length(), buildCorrectString(getDigitArray(s, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER));

                }
                if (s.length() == 19) {
                    cardDate.requestFocus();
                }
            }

            private boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
                boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
                for (int i = 0; i < s.length(); i++) { // check that every element is right
                    if (i > 0 && (i + 1) % dividerModulo == 0) {
                        isCorrect &= divider == s.charAt(i);
                    } else {
                        isCorrect &= Character.isDigit(s.charAt(i));
                    }
                }
                return isCorrect;
            }

            private String buildCorrectString(char[] digits, int dividerPosition, char divider) {
                final StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < digits.length; i++) {
                    if (digits[i] != 0) {
                        formatted.append(digits[i]);
                        if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                            formatted.append(divider);
                        }
                    }
                }

                return formatted.toString();
            }

            private char[] getDigitArray(final Editable s, final int size) {
                char[] digits = new char[size];
                int index = 0;
                for (int i = 0; i < s.length() && index < size; i++) {
                    char current = s.charAt(i);
                    if (Character.isDigit(current)) {
                        digits[index] = current;
                        index++;
                    }
                }
                return digits;
            }
        });
    }

    private void checkDate(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "MMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int j, int i1, int i2) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 6) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int mon = Integer.parseInt(clean.substring(0, 2));
                        int year = Integer.parseInt(clean.substring(2, 6));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        clean = String.format("%02d%02d", mon, year);
                    }

                    clean = String.format("%s/%s",
                            clean.substring(0, 2),
                            clean.substring(2, 6));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    editText.setText(current);
                    editText.setSelection(sel < current.length() ? sel : current.length());

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginType = "";
        //setDataFromSharedPreferences();
    }

    @Override
    public void getDetail(String detail, String name) {

        subs_POPUP(name, detail);
    }

    public void subs_POPUP(String nam, String desc) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            final View layout = inflater.inflate(R.layout.p_custom_pop_subs_detail,
                    (ViewGroup) activity.findViewById(R.id.mainPop));
            // create a 300px width and 470px height PopupWindow
            final PopupWindow pw = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            // display the popup in the center
            pw.showAtLocation(layout, Gravity.TOP, 0, 0);
            TextView subsname = layout.findViewById(R.id.subs_plan);
            TextView subsDes = layout.findViewById(R.id.sub_plan_desc);
            ImageView subGotIt = layout.findViewById(R.id.subs_got_it);


            subsDes.setText(desc);
            subsname.setText(nam);
            subGotIt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pw.dismiss();
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


    private boolean checktimings(String time, String endtime) {

        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);

            if (date1 != null) {
                if (date1.before(date2)) {
                    return true;
                } else {

                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
