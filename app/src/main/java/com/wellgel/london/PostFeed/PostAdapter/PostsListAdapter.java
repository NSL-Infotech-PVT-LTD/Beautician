package com.wellgel.london.PostFeed.PostAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wellgel.london.Customer.C_CartModel;
import com.wellgel.london.Customer.C_ConstantClass;
import com.wellgel.london.PostFeed.POstListModel;
import com.wellgel.london.PostFeed.PostsComments;
import com.wellgel.london.PostFeed.PostsCommentsModel;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.PreferencesShared;
import com.wellgel.london.UtilClasses.TimeAgo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.C_product_holder> {

    Context context;
    List<PostsCommentsModel.Datum> list;
    private PreferencesShared shared;



    public PostsListAdapter(Context context, List<PostsCommentsModel.Datum> list) {
        this.context = context;
        this.list = list;

        shared = new PreferencesShared(context);
    }

    @NonNull
    @Override
    public PostsListAdapter.C_product_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.posts_view, parent, false);
        PostsListAdapter.C_product_holder bookHolder = new PostsListAdapter.C_product_holder(view);

        return bookHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final PostsListAdapter.C_product_holder holder, int position) {


        PostsCommentsModel.Datum model = list.get(position);
        holder.postBy.setText(model.getUser_details().getName());
        holder.postComment.setText(model.getComment());
        TimeAgo timeAgo = new TimeAgo();
        if (model.getCreatedAt()!=null && ! model.getCreatedAt().isEmpty()) {
            String date = model.getCreatedAt();
            String MyFinalValue = timeAgo.covertTimeToText(date);
            holder.timeOfCommentTv.setText(MyFinalValue);
        }


        Picasso.with(context).load(shared.getString("profile")).into(holder.profileImage);
        Picasso.with(context).load("https://dev.netscapelabs.com/wellgellondon/public/uploads/customer/profile_image/" +model.getUser_details().getProfileImage()).into(holder.profileImage);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class C_product_holder extends RecyclerView.ViewHolder {

        TextView postBy, postComment,timeOfCommentTv;
        CircleImageView profileImage;

        public C_product_holder(@NonNull View itemView) {
            super(itemView);

            postBy = itemView.findViewById(R.id.postUserName);
            postComment = itemView.findViewById(R.id.postsUserComment);
            profileImage = itemView.findViewById(R.id.postsImageView);
            timeOfCommentTv = itemView.findViewById(R.id.timeOfCommentTv);


        }
    }


    public void clear() {

        list.clear();

        notifyDataSetChanged();

    }


// Add a list of items -- change to type used

//    public void addAll(List<C_CartModel> list) {
//
//        list.addAll(list);
//
//        notifyDataSetChanged();
//
//    }

    public interface LikePost {
        public void onLike(int id);
    }

    public interface Dislike {
        public void onDislike(int id);
    }


    public interface onItemClick {
        public void onItemClick(int id);
    }

}
