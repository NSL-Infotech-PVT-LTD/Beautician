package com.wellgel.london.PostFeed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.wellgel.london.APIs.Customer_APIs;
import com.wellgel.london.Customer.Activities.C_DashboardAct;
import com.wellgel.london.Customer.Activities.C_RegisterActivity;
import com.wellgel.london.Customer.C_ConstantClass;
import com.wellgel.london.Customer.SerializeModelClasses.RegistrationSerial;
import com.wellgel.london.PostFeed.PostAdapter.FeedPostAdapter;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedPostActivtiy extends AppCompatActivity implements FeedPostAdapter.LikePost {


    private FeedPostModel model;
    private List<FeedPostModel> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private FeedPostAdapter adapter;
    private FeedPostActivtiy activity;
    private PreferencesShared shared;
    private CircleImageView profileImage;
    private ImageView c_dash_navi;
    private int REQUEST_CAMERA = 777, SELECT_FILE = 7777;

    private String picturePath = "";
    private File file;
    private String userChoosenTask;
    private RelativeLayout videoPost, photoPost;
    private TextView post_tv;
    private MaterialButton shareButton;
    private String IMAGE_DIRECTORY = "/WellgelLondon/";
    private RelativeLayout postFeedLayout;
    private ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_post_activtiy);
        activity = this;
        shared = new PreferencesShared(activity);
        recyclerView = findViewById(R.id.postRecyler);
        c_dash_navi = findViewById(R.id.c_dash_navi);
        videoPost = findViewById(R.id.videoPost);
        shareButton = findViewById(R.id.share);
        post_tv = findViewById(R.id.post);
        photoPost = findViewById(R.id.photoPost);
        postFeedLayout = findViewById(R.id.postFeed);
        profileImage = findViewById(R.id.profileImage);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        c_dash_navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        photoPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        videoPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (picturePath.isEmpty()) {
                    Snackbar.make(postFeedLayout, "Please select image or video to upload", Snackbar.LENGTH_LONG);
                } else {
                    postStore();
                }
            }
        });

        recyclerView.setLayoutManager(linearLayoutManager);



    }

    @Override
    protected void onResume() {
        super.onResume();
        postList();
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


    //Camera intent to open the Camera view
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    //Intent to open the gallery
    private void galleryIntent() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
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
        shareButton.setVisibility(View.VISIBLE);
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
        shareButton.setVisibility(View.VISIBLE);
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

    //     post store to server APi
    public void postStore() {
        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Please wait while uploading your post.....");
        progressDoalog.show();

        // initialize file here
        MultipartBody.Part f = null;
        if (file != null) {
            f = MultipartBody.Part.createFormData("media", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        }

        Map<String, RequestBody> requestBodyMap = new HashMap<>();
//        requestBodyMap.put("profile_image", fileReqBody);
        requestBodyMap.put("Content-Type", RequestBody.create(MediaType.parse("multipart/form-data"), "application/x-www-form-urlencoded"));
        requestBodyMap.put("content", RequestBody.create(MediaType.parse("multipart/form-data"), post_tv.getText().toString().trim()));

        /*Create handle for the RetrofitInstance interface*/
        Customer_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Customer_APIs.class);
        Call<PostStoreModel> call = service.postStore("Bearer " + shared.getString("token"), f, requestBodyMap);
        call.enqueue(new Callback<PostStoreModel>() {
            @Override
            public void onResponse(Call<PostStoreModel> call, Response<PostStoreModel> response) {


                progressDoalog.dismiss();
                if (response.body() != null) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                            shareButton.setVisibility(View.GONE);
                            picturePath = "";
                            post_tv.setHint(getString(R.string.postSomthing));
                            profileImage.setImageDrawable(getResources().getDrawable(R.drawable.profile));

                            Toast.makeText(FeedPostActivtiy.this, "" + response.body().getData().getMessage(), Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<PostStoreModel> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void postList() {
        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Loading.......");
        progressDoalog.show();


        /*Create handle for the RetrofitInstance interface*/
        Customer_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Customer_APIs.class);
        Call<POstListModel> call = service.postList("Bearer " + shared.getString("token"));
        call.enqueue(new Callback<POstListModel>() {
            @Override
            public void onResponse(Call<POstListModel> call, Response<POstListModel> response) {


                progressDoalog.dismiss();
                if (response.body() != null) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                            adapter = new FeedPostAdapter(activity, response.body().getData().getData(),FeedPostActivtiy.this);
                            recyclerView.setAdapter(adapter);
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
            public void onFailure(Call<POstListModel> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onLike(int id, TextView like, TextView dislike,String status,ImageView likeImg,ImageView dislikeImage) {

        likeOrDislikePosts(id,like,dislike,status,likeImg,dislikeImage);
    }
    public void likeOrDislikePosts(int id, TextView like, TextView dislike,String status,ImageView likeImg,ImageView disLikeImg) {
//        progressDoalog = new ProgressDialog(activity);
//        progressDoalog.setMessage("Loading.......");
//        progressDoalog.show();


        /*Create handle for the RetrofitInstance interface*/
        Customer_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Customer_APIs.class);
        Call<PostLikeModel> call = service.likeOrDislike("application/x-www-form-urlencoded","Bearer " + shared.getString("token"),id,status);
        call.enqueue(new Callback<PostLikeModel>() {
            @Override
            public void onResponse(Call<PostLikeModel> call, Response<PostLikeModel> response) {


//                progressDoalog.dismiss();
                if (response.body() != null) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                         like.setText(response.body().getData().getLike()+" Likes");
                         dislike.setText(response.body().getData().getDislike()+" Dislikes");
                         if (response.body().getData().getStatus().toString().equalsIgnoreCase("0")){
                             likeImg.setImageResource(R.drawable.ic_like_deactive);
                             disLikeImg.setImageResource(R.drawable.ic_dislike_deactive);
                         }

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
            public void onFailure(Call<PostLikeModel> call, Throwable t) {
//                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
