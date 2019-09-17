package com.wellgel.london.Customer.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wellgel.london.Customer.C_BookedOrderModel;
import com.wellgel.london.Customer.C_Product_model;
import com.wellgel.london.R;

import java.util.List;

public class C_Booked_OrderAdapter extends RecyclerView.Adapter<C_Booked_OrderAdapter.C_BookedHoder> {

    FragmentActivity context;
    List<C_BookedOrderModel> list;


    private long mLastClickTime = System.currentTimeMillis();
    private long CLICK_TIME_INTERVAL = 600;

    public C_Booked_OrderAdapter(FragmentActivity context, List<C_BookedOrderModel> list) {
        this.context = context;
        this.list = list;


    }

    @NonNull
    @Override
    public C_BookedHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.c_custom_booked_list, parent, false);
        C_Booked_OrderAdapter.C_BookedHoder bookHolder = new C_Booked_OrderAdapter.C_BookedHoder(view);

        return bookHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final C_BookedHoder holder, int position) {

        C_BookedOrderModel model = list.get(position);
        holder.salondate.setText(model.getBookedDate());
        holder.salonName.setText(model.getBookedSalon());
        holder.salonAdd.setText(model.getBookedSalonAdd());


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class C_BookedHoder extends RecyclerView.ViewHolder {

        TextView salonName, salonAdd, salondate;


        public C_BookedHoder(@NonNull View itemView) {
            super(itemView);

            salonName = itemView.findViewById(R.id.c_dash_salonName);
            salonAdd = itemView.findViewById(R.id.c_dash_salonAddrs);
            salondate = itemView.findViewById(R.id.c_dash_salonDate);


        }
    }


    public void clear() {

        list.clear();

        notifyDataSetChanged();

    }


// Add a list of items -- change to type used

    public void addAll(List<C_Product_model> list) {

        list.addAll(list);

        notifyDataSetChanged();

    }


}
