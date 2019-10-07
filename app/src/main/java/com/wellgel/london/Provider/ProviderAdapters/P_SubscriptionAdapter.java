package com.wellgel.london.Provider.ProviderAdapters;

import android.content.Context;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wellgel.london.Customer.C_Product_model;
import com.wellgel.london.Provider.ModelSerialized.P_SubscripSerial;
import com.wellgel.london.R;


import java.util.List;

public class P_SubscriptionAdapter extends RecyclerView.Adapter<P_SubscriptionAdapter.P_Adapter_Services_holder> {

    Context context;
    List<P_SubscripSerial.Datum> list;

    SubscribeClick subscribeClick;
    SUBS_DETAIL subs_detail;
    int index = -1;

    public P_SubscriptionAdapter(Context context, List<P_SubscripSerial.Datum> list, SubscribeClick subscribeClick, SUBS_DETAIL subs_detail) {
        this.context = context;
        this.list = list;
        this.subs_detail = subs_detail;

        this.subscribeClick = subscribeClick;

    }

    @NonNull
    @Override
    public P_Adapter_Services_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.p_custom_choose_subs, parent, false);
        P_SubscriptionAdapter.P_Adapter_Services_holder services_holder = new P_SubscriptionAdapter.P_Adapter_Services_holder(view);

        return services_holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final P_Adapter_Services_holder holder, final int position) {

        final P_SubscripSerial.Datum model = list.get(position);
        holder.subsName.setText(model.getName());


        holder.subsPrice.setText(" "+context.getResources().getString(R.string.currency) + model.getPrice() + ".00");


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = position;
                notifyDataSetChanged();
                subscribeClick.getSubscribed(model.getId(), model.getName());
            }
        });

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                subs_detail.getDetail(model.getDescription(), model.getName());
            }
        });
        if (index == position) {
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.pink));
        } else {
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.transparent));

        }

    }

    public interface SubscribeClick {
        public void getSubscribed(int id, String name);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class P_Adapter_Services_holder extends RecyclerView.ViewHolder {

        TextView subsName, subsPrice;
        RelativeLayout relativeLayout;
        TextView textView;

        public P_Adapter_Services_holder(@NonNull View itemView) {
            super(itemView);
            subsName = itemView.findViewById(R.id.subs_type);
            subsPrice = itemView.findViewById(R.id.subs_price);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            textView = (TextView) itemView.findViewById(R.id.subs_view);
            SpannableString content = new SpannableString(context.getResources().getString(R.string.view_detail));
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            textView.setText(content);
        }
    }


    public void clear() {

        list.clear();

        notifyDataSetChanged();

    }

    public interface SUBS_DETAIL {
        public void getDetail(String detail, String name);
    }

// Add a list of items -- change to type used

    public void addAll(List<C_Product_model> list) {

        list.addAll(list);

        notifyDataSetChanged();

    }


}
