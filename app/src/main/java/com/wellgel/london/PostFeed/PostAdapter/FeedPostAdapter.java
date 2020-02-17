package com.wellgel.london.PostFeed.PostAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wellgel.london.Customer.C_AddressModel;
import com.wellgel.london.Customer.C_CartModel;
import com.wellgel.london.Customer.C_ConstantClass;
import com.wellgel.london.PostFeed.FeedPostModel;
import com.wellgel.london.PostFeed.POstListModel;
import com.wellgel.london.PostFeed.PostsComments;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.PreferencesShared;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedPostAdapter extends RecyclerView.Adapter<FeedPostAdapter.C_product_holder> {

    Context context;
    List<POstListModel.Datum> list;
    private PreferencesShared shared;
    LikePost likePost;



    public FeedPostAdapter(Context context, List<POstListModel.Datum> list,LikePost like) {
        this.context = context;
        this.list = list;
        this.likePost=like;
        shared = new PreferencesShared(context);
    }

    @NonNull
    @Override
    public C_product_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_feed_post_layoput, parent, false);
        FeedPostAdapter.C_product_holder bookHolder = new FeedPostAdapter.C_product_holder(view);

        return bookHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final C_product_holder holder, int position) {


        POstListModel.Datum model = list.get(position);
        holder.totalCommnt.setText(model.getPostComment()+" Comments");
        holder.totalLikes.setText(model.getPostLike()+" Likes");
        holder.totalDislike.setText(model.getPostDislike()+" Dislikes");
        holder.name.setText(shared.getString(ConstantClass.USER_NAME));
        if (model.getLikeByMe()!=null){
            if (model.getLikeByMe().toString().equalsIgnoreCase("like")){
                holder.likeImage.setImageResource(R.drawable.ic_like_active);
            }
            if (model.getLikeByMe().toString().equalsIgnoreCase("dislike")){
                holder.dislikeImage.setImageResource(R.drawable.ic_dislike_active);
            }
        }

        Picasso.with(context).load(shared.getString("profile")).into(holder.profileImage);
        Picasso.with(context).load(C_ConstantClass.IMAGE_BASE_URL+"post/media/" +model.getMedia()).into(holder.postImage);
        holder.likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likePost.onLike(model.getId(),holder.totalLikes,holder.totalDislike,"like",holder.likeImage,holder.dislikeImage);
                holder.dislikeImage.setImageResource(R.drawable.ic_dislike_deactive);
                holder.likeImage.setImageResource(R.drawable.ic_like_active);
            }
        });
        holder.dislikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likePost.onLike(model.getId(),holder.totalLikes,holder.totalDislike,"dislike",holder.likeImage,holder.dislikeImage);
                holder.dislikeImage.setImageResource(R.drawable.ic_dislike_active);
                holder.likeImage.setImageResource(R.drawable.ic_like_deactive);
            }
        });

        holder.commnetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent comments=new Intent(context, PostsComments.class);
                comments.putExtra("postId",model.getId()+"");
                context.startActivity(comments);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class C_product_holder extends RecyclerView.ViewHolder {

        TextView totalLikes, totalDislike, name, totalCommnt;
        CircleImageView profileImage;
        ImageView likeImage,postImage, dislikeImage, commnetImage;


        public C_product_holder(@NonNull View itemView) {
            super(itemView);

            totalLikes = itemView.findViewById(R.id.totalLikes);
            totalDislike = itemView.findViewById(R.id.totalDislikes);
            name = itemView.findViewById(R.id.name);
            totalCommnt = itemView.findViewById(R.id.totalCommnt);
            profileImage = itemView.findViewById(R.id.profileImage);
            likeImage = itemView.findViewById(R.id.like);
            postImage = itemView.findViewById(R.id.postImage);
            dislikeImage = itemView.findViewById(R.id.dislike);
            commnetImage = itemView.findViewById(R.id.comment);

        }
    }


    public void clear() {

        list.clear();

        notifyDataSetChanged();

    }


// Add a list of items -- change to type used

    public void addAll(List<C_CartModel> list) {

        list.addAll(list);

        notifyDataSetChanged();

    }

    public interface LikePost {
        public void onLike(int id ,TextView like,TextView dislike,String status,ImageView likeImg,ImageView dislikeImg);
    }

    public interface Dislike {
        public void onDislike(int id);
    }


    public interface onItemClick {
        public void onItemClick(int id);
    }


}
