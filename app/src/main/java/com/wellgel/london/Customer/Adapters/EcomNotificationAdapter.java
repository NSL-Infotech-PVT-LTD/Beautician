package com.wellgel.london.Customer.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wellgel.london.Customer.Activities.C_CartDetailAct;
import com.wellgel.london.Customer.C_CartModel;
import com.wellgel.london.Customer.EcomNoti_Model;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.DropDown;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EcomNotificationAdapter extends RecyclerView.Adapter<EcomNotificationAdapter.C_product_holder> {

    Context context;
    onItemClick onItemClick;
    List<EcomNoti_Model> list;


    private long mLastClickTime = System.currentTimeMillis();
    private long CLICK_TIME_INTERVAL = 600;
    private String spinnerValue;

    public EcomNotificationAdapter(Context context, List<EcomNoti_Model> list, onItemClick onItemClick) {
        this.context = context;
        this.list = list;
        this.onItemClick = onItemClick;


    }

    @NonNull
    @Override
    public C_product_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_notificaion_layout, parent, false);
        EcomNotificationAdapter.C_product_holder bookHolder = new EcomNotificationAdapter.C_product_holder(view);

        return bookHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final C_product_holder holder, int position) {

        final EcomNoti_Model model = list.get(position);
        holder.noti_Name.setText(model.getName() + " " + model.getMessage());

        Picasso.with(context).load(model.getImage()).into(holder.noti_Image);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class C_product_holder extends RecyclerView.ViewHolder {

        TextView noti_Name;
        CircleImageView noti_Image;


        public C_product_holder(@NonNull View itemView) {
            super(itemView);

            noti_Name = itemView.findViewById(R.id.noti_Name);
            noti_Image = itemView.findViewById(R.id.noti_Image);


        }
    }

    public interface onItemClick {
        public void onItemClick(String id, String value, String quantity);
    }


}
