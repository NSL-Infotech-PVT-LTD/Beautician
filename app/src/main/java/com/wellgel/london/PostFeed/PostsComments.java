package com.wellgel.london.PostFeed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.wellgel.london.APIs.Customer_APIs;
import com.wellgel.london.PostFeed.PostAdapter.FeedPostAdapter;
import com.wellgel.london.PostFeed.PostAdapter.PostsListAdapter;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.PreferencesShared;
import com.wellgel.london.UtilClasses.Retrofit.RetrofitClientInstance;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsComments extends AppCompatActivity implements View.OnClickListener {
    private PostsComments activity;
    private ProgressDialog progressDoalog;
    private PreferencesShared shared;
    private PostsListAdapter postsListAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String id="";
    private AppCompatImageView sendPostImg;
    private TextInputEditText commentText;
    private String userComment="";
    private ImageView backImage;
    List<PostsCommentsModel.Datum>postsList=new ArrayList<>();
    private PostsCommentsModel.Datum commentModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_comments);
        activity = this;
        shared = new PreferencesShared(activity);
        recyclerView=findViewById(R.id.postsRecycler);
        sendPostImg=findViewById(R.id.sendPostsBtn);
        commentText=findViewById(R.id.commentText);
        backImage=findViewById(R.id.backImg);
        layoutManager=new LinearLayoutManager(activity);
        sendPostImg.setOnClickListener(this);
        backImage.setOnClickListener(this);

        if (getIntent().getExtras()!=null){
            id=getIntent().getStringExtra("postId");
        }
        if (! TextUtils.isEmpty(id))
        likeOrDislikePosts();

    }
    public void likeOrDislikePosts() {
        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Loading.......");
        progressDoalog.show();


        /*Create handle for the RetrofitInstance interface*/
        Customer_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Customer_APIs.class);
        Call<PostsCommentsModel> call = service.getPostsComments("application/x-www-form-urlencoded",Integer.parseInt(id));
        call.enqueue(new Callback<PostsCommentsModel>() {
            @Override
            public void onResponse(Call<PostsCommentsModel> call, Response<PostsCommentsModel> response) {


                progressDoalog.dismiss();
                if (response.body() != null) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                            postsList.addAll(response.body().getData().getData());
                            postsListAdapter = new PostsListAdapter(activity, postsList);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(postsListAdapter);
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
            public void onFailure(Call<PostsCommentsModel> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void postComments() {
        /*Create handle for the RetrofitInstance interface*/
        Customer_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Customer_APIs.class);
        Call<SendCommentModel> call = service.sendUserComments("application/x-www-form-urlencoded","Bearer " + shared.getString("token"),Integer.parseInt(id),userComment);
        call.enqueue(new Callback<SendCommentModel>() {
            @Override
            public void onResponse(Call<SendCommentModel> call, Response<SendCommentModel> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                            String comment=response.body().getData().getPostComment().getComment().toString();

                            commentModel=new PostsCommentsModel.Datum();
                            PostsCommentsModel.UserDetails userDetails=new PostsCommentsModel.UserDetails();
                            userDetails.setName(shared.getString(ConstantClass.USER_NAME));
                            File file = new File(shared.getString("profile"));
                            String imageName = file.getName(); // --> "viewemployee.jsff"
                            userDetails.setProfileImage(imageName);
                            commentModel.setUser_details(userDetails);
                            commentModel.setComment(comment);
                            commentModel.setCreatedAt(response.body().getData().getPostComment().getCreatedAt());
                            postsList.add(commentModel);
                            postsListAdapter.notifyDataSetChanged();
//                            recyclerView.smoothScrollToPosition(recyclerView.getBottom());
                            commentText.setText("");
                            commentText.setHint("Write a comment");
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
            public void onFailure(Call<SendCommentModel> call, Throwable t) {
//                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sendPostsBtn:
                if (commentText.getText()!=null){
                    userComment=commentText.getText().toString().trim();
                    postComments();
                }else {
                    Toast.makeText(activity, "Add text to post a comment", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.backImg:
                finish();
                break;
        }
    }
}
