package com.wellgel.london.Customer.Adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wellgel.london.Customer.C_CartModel;
import com.wellgel.london.Customer.C_ConstantClass;
import com.wellgel.london.Customer.SerializeModelClasses.C_MyOrdersSerial;
import com.wellgel.london.Customer.SerializeModelClasses.C_SalonListSerial;
import com.wellgel.london.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class C_SalonListAdapetr extends RecyclerView.Adapter<C_SalonListAdapetr.C_product_holder> {


    Activity context;
    List<C_SalonListSerial.Datum> list;
    ONSALONCLICK onItemClick;

    private long mLastClickTime = System.currentTimeMillis();
    private long CLICK_TIME_INTERVAL = 600;
    private String spinnerValue;

    public C_SalonListAdapetr(Activity context, List<C_SalonListSerial.Datum> list, ONSALONCLICK onItemClick) {
        this.context = context;
        this.list = list;
        this.onItemClick = onItemClick;


    }

    @NonNull
    @Override
    public C_product_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.c_custom_nearby_salons, parent, false);
        C_SalonListAdapetr.C_product_holder bookHolder = new C_SalonListAdapetr.C_product_holder(view);

        return bookHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final C_product_holder holder, int position) {

        final C_SalonListSerial.Datum model = list.get(position);

        holder.salonName.setText(model.getName());
        holder.salonAddress.setText(model.getAddress());
        holder.salonDistance.setText(model.getDistance() + " Miles");
        if (model.getRating() == 0) {
            holder.salonRating.setVisibility(View.GONE);
            holder.salon_new.setVisibility(View.VISIBLE);
        } else {
            holder.salonRating.setVisibility(View.VISIBLE);
            holder.salon_new.setVisibility(View.GONE);
            holder.salonRating.setRating(model.getRating());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat requestime = new SimpleDateFormat("hh:mm aa");
        Date dt = null, dt2 = null;

        try {
            dt = sdf.parse(model.getBusinessHourStart());
            dt2 = sdf.parse(model.getBusinessHourEnd());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.salonTime.setText("Timings: " + requestime.format(dt) + " - " + requestime.format(dt2));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.getSalon(model.getId(), model.getName(), model.getAddress(), model.getBusinessHourStart(), model.getBusinessHourEnd(), model.getRating(), C_ConstantClass.IMAGE_BASE_URL + "salon/profile_image/" + model.getProfileImage(), C_ConstantClass.IMAGE_BASE_URL + "salon/profile_image/" + model.getImage1(), C_ConstantClass.IMAGE_BASE_URL + "salon/profile_image/" + model.getImage2(), C_ConstantClass.IMAGE_BASE_URL + "salon/profile_image/" + model.getImage3());
            }
        });

    }


    public interface ONSALONCLICK {
        public void getSalon(int ID, String name, String address, String startTime, String endTime, int rating, String image, String img1, String img2, String img3);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class C_product_holder extends RecyclerView.ViewHolder {
        TextView salonName, salonAddress;
        RatingBar salonRating;
        TextView salonTime, salon_new, salonDistance;

        public C_product_holder(@NonNull View itemView) {
            super(itemView);

            salonName = itemView.findViewById(R.id.near_salon_name);
            salonAddress = itemView.findViewById(R.id.near_salon_address);
            salonRating = itemView.findViewById(R.id.near_salon_rating);
            salonTime = itemView.findViewById(R.id.near_salon_timing);
            salonDistance = itemView.findViewById(R.id.near_salon_distance);
            salon_new = itemView.findViewById(R.id.salon_new);


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


}
