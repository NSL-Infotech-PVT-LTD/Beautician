package com.wellgel.london.Customer.Adapters;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.wellgel.london.Customer.Activities.C_MyorderDetails;
import com.wellgel.london.Customer.C_AddressModel;
import com.wellgel.london.Customer.C_CartModel;

import com.wellgel.london.Customer.SerializeModelClasses.C_MyOrdersSerial;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.PreferencesShared;

import java.io.Serializable;
import java.util.List;

public class C_MyOrdersAdapter extends RecyclerView.Adapter<C_MyOrdersAdapter.C_product_holder> {

    FragmentActivity context;
    Activity activity;
    onItemClick onItemClick;
    List<C_MyOrdersSerial.Datum> list;
    List<C_MyOrdersSerial.Address> list1;
    int lastPos = -1;


    private long mLastClickTime = System.currentTimeMillis();
    private long CLICK_TIME_INTERVAL = 600;
    private String spinnerValue;
    public EditAddress editAddress;
    public RemoveAddress removeAddress;
    private PreferencesShared shared;

    public C_MyOrdersAdapter(FragmentActivity context, List<C_MyOrdersSerial.Datum> list) {
        this.context = context;
        this.list = list;

        shared = new PreferencesShared(context);

    }


    @NonNull
    @Override
    public C_product_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.c_custom_orders_card, parent, false);
        C_MyOrdersAdapter.C_product_holder bookHolder = new C_MyOrdersAdapter.C_product_holder(view);

        return bookHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final C_product_holder holder, int position) {

        final C_MyOrdersSerial.Datum model = list.get(position);

//        holder.productAdd.setText(model.getMyProductAddress());
        holder.invoiceNo.setText(model.getId() + "");
//        holder.productPrice.setText("£" + model.getMyProductPrice() + ".00");
//        holder.productQauan.setText(model.getMyProductQuan());
//        holder.totalItems.setText("(" + model.getMyProductQuan() + " items)");
        holder.my_pro_status.setText(model.getStatus());
        holder.productPrice.setText(""+context.getResources().getString(R.string.currency) + model.getTotalPaid());
//        holder.itemPrice.setText("£" + model.getMyProductPrice());

        holder.productCLik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, C_MyorderDetails.class);
                intent.putExtra("order_detail_list", list.get(position).getOrderdetails());
                intent.putExtra("order_address_list", list.get(position).getAddresses());
                intent.putExtra("pos", position);
                context.startActivity(intent);
//                if (holder.productCLik.getText().toString().equalsIgnoreCase("Check invoice")) {
//                    holder.productCLik.setText(context.getResources().getString(R.string.hideInv));
//                    holder.cardViewOfamount.setVisibility(View.VISIBLE);
//                    holder.productCLik.setTextColor(context.getResources().getColor(R.color.red));
//
//
//                } else {
//                    holder.productCLik.setText(context.getResources().getString(R.string.view));
//                    holder.cardViewOfamount.setVisibility(View.GONE);
//                    holder.productCLik.setTextColor(context.getResources().getColor(R.color.green));
//
//                }
//                lastPos = position;
//                notifyDataSetChanged();

            }
        });

        if (lastPos == position) {
            holder.productCLik.setText(context.getResources().getString(R.string.hideInv));
            holder.cardViewOfamount.setVisibility(View.VISIBLE);
            holder.productCLik.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            holder.productCLik.setText(context.getResources().getString(R.string.view));
            holder.cardViewOfamount.setVisibility(View.GONE);
            holder.productCLik.setTextColor(context.getResources().getColor(R.color.red));

        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class C_product_holder extends RecyclerView.ViewHolder {

        TextView invoiceNo, totalItems, itemPrice, totalAmount, my_pro_status, productPrice, productAdd, productQauan, productCLik;
        CardView cardViewOfamount;


        public C_product_holder(@NonNull View itemView) {
            super(itemView);

            productPrice = itemView.findViewById(R.id.my_pro_price);
            invoiceNo = itemView.findViewById(R.id.invoiceNo);
            my_pro_status = itemView.findViewById(R.id.my_pro_status);
            totalItems = itemView.findViewById(R.id.totalItems);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            totalAmount = itemView.findViewById(R.id.totalAmount);
            productAdd = itemView.findViewById(R.id.my_pro_add);
            cardViewOfamount = itemView.findViewById(R.id.cardViewOfamount);
            productQauan = itemView.findViewById(R.id.my_pro_qaun);
            productCLik = itemView.findViewById(R.id.my_pro_click);

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

    public interface EditAddress {
        public void onEditClick(int id);
    }

    public interface RemoveAddress {
        public void onRemove(int id);
    }


    public interface onItemClick {
        public void onItemClick(int id);
    }


}
