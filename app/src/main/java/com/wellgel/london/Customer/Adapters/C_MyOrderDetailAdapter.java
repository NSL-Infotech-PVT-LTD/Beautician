package com.wellgel.london.Customer.Adapters;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wellgel.london.Customer.Activities.C_MyorderDetails;
import com.wellgel.london.Customer.C_CartModel;

import com.wellgel.london.Customer.C_ConstantClass;
import com.wellgel.london.Customer.SerializeModelClasses.C_MyOrdersSerial;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.DropDown;

import java.io.Serializable;
import java.util.List;

public class C_MyOrderDetailAdapter extends RecyclerView.Adapter<C_MyOrderDetailAdapter.C_product_holder> {


    Activity context;
    onItemClick onItemClick;
    List<C_MyOrdersSerial.Orderdetail> list;


    private long mLastClickTime = System.currentTimeMillis();
    private long CLICK_TIME_INTERVAL = 600;
    private String spinnerValue;

    public C_MyOrderDetailAdapter(Activity context, List<C_MyOrdersSerial.Orderdetail> list) {
        this.context = context;
        this.list = list;
        this.onItemClick = onItemClick;


    }

    @NonNull
    @Override
    public C_product_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.c_custom_order_detail, parent, false);
        C_MyOrderDetailAdapter.C_product_holder bookHolder = new C_MyOrderDetailAdapter.C_product_holder(view);

        return bookHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final C_product_holder holder, int position) {

        final C_MyOrdersSerial.Orderdetail model = list.get(position);
        holder.productPrice.setText(" "+context.getResources().getString(R.string.currency) + model.getProductId().getPrice() + ".00");

//        holder.totalAmount.setText(" £" + model.getProductId().getPrice() + ".00");
        holder.productNAme.setText(model.getProductId().getName());

//        holder.textAddress.setText("ADDRESS : " + model.get + "," +
//                list.get(pos).getCity() + "\n" +
//                list.get(pos).getCounty() + "," +
//                list.get(pos).getCountry() + "\n" +
//                list.get(pos).getZip());


        holder.dropDown.setText("Quantity : " + model.getQuantity() + "");

        Picasso.with(context).load(C_ConstantClass.IMAGE_BASE_URL + "products/" + model.getProductId().getImage()).into(holder.cartProductImage);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class C_product_holder extends RecyclerView.ViewHolder {

        TextView productNAme, productPrice;
        public TextView removeProduct;
        ImageView cartProductImage;
        TextView dropDown, textAddress;


        public C_product_holder(@NonNull View itemView) {
            super(itemView);
            textAddress = itemView.findViewById(R.id.textAddress);

            productNAme = itemView.findViewById(R.id.cart_pro_name);
            productPrice = itemView.findViewById(R.id.cart_pro_price);
            removeProduct = itemView.findViewById(R.id.removeFromCart);
            cartProductImage = itemView.findViewById(R.id.cartProductImage);
            dropDown = itemView.findViewById(R.id.dropdown);


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
