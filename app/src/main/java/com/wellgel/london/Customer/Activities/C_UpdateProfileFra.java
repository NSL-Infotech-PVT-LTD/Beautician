package com.wellgel.london.Customer.Activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wellgel.london.APIs.Customer_APIs;
import com.wellgel.london.Customer.C_ConstantClass;
import com.wellgel.london.Customer.SerializeModelClasses.C_UpdateProfileSerial;
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
import java.util.Calendar;
import java.util.HashMap;
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
public class C_UpdateProfileFra extends AppCompatActivity {

    private ImageView user_profile;
    private TextView user_name, user_number, user_address, user_county, user_email;
    private PreferencesShared shared;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 888, SELECT_FILE = 8888;
    private String picturePath = "";
    private File file;
    private C_UpdateProfileFra activity;
    private String IMAGE_DIRECTORY = "/WellgelLondon/";
    private ProgressDialog progressDoalog;
    private Button update_profile_btn;
    private String imageURL = C_ConstantClass.IMAGE_BASE_URL + "customer/profile_image/";
    private String st_name, st_profile, st_number, st_country, st_address;


    public C_UpdateProfileFra() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_c__update_profile);
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


    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {

                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
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
        requestBodyMap.put("device_type", RequestBody.create(MediaType.parse("multipart/form-data"), ConstantClass.DEVICETYPE));
        requestBodyMap.put("device_token", RequestBody.create(MediaType.parse("multipart/form-data"), shared.getString(ConstantClass.DEVICE_TOKEN)));

        /*Create handle for the RetrofitInstance interface*/
        Customer_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Customer_APIs.class);
        Call<C_UpdateProfileSerial> call = service.updateProfil("Bearer " + shared.getString("token"),userImg, requestBodyMap);
        call.enqueue(new Callback<C_UpdateProfileSerial>() {
            @Override
            public void onResponse(Call<C_UpdateProfileSerial> call, Response<C_UpdateProfileSerial> response) {


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
            public void onFailure(Call<C_UpdateProfileSerial> call, Throwable t) {
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

}
