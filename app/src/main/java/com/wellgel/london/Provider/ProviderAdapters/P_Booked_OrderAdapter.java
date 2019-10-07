package com.wellgel.london.Provider.ProviderAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wellgel.london.Customer.C_Product_model;
import com.wellgel.london.Customer.SerializeModelClasses.C_AppointmentList;
import com.wellgel.london.Provider.ModelSerialized.P_AppointmentListSerial;
import com.wellgel.london.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class P_Booked_OrderAdapter extends RecyclerView.Adapter<P_Booked_OrderAdapter.C_BookedHoder> {

    Context context;
    List<P_AppointmentListSerial.Datum> list;
    OnBookingProviderID bookingID;


    private long mLastClickTime = System.currentTimeMillis();
    private long CLICK_TIME_INTERVAL = 600;

    public P_Booked_OrderAdapter(Context context, List<P_AppointmentListSerial.Datum> list, OnBookingProviderID bookingID) {
        this.context = context;
        this.bookingID = bookingID;
        this.list = list;


    }

    @NonNull
    @Override
    public C_BookedHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.c_custom_booked_list, parent, false);
        P_Booked_OrderAdapter.C_BookedHoder bookHolder = new P_Booked_OrderAdapter.C_BookedHoder(view);

        return bookHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final C_BookedHoder holder, int position) {

        P_AppointmentListSerial.Datum model = list.get(position);
        holder.salondate.setText(model.getRequestedDatetime());
        holder.salonName.setText(model.getCustomerdetails().get(0).getName());
        holder.salonAdd.setText(model.getCustomerdetails().get(0).getAddress());
        String currentString = model.getRequestedDatetime();
        String[] separated = currentString.split(" ");

        holder.salondate.setText(parseDateToddMMyyyy(separated[0]));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bookingID.getBookingProviderID(model.getId());
            }
        });
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

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd \nMMMM";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public interface OnBookingProviderID {
        public void getBookingProviderID(int id);
    }
// Add a list of items -- change to type used

    public void addAll(List<C_Product_model> list) {

        list.addAll(list);

        notifyDataSetChanged();

    }


}
