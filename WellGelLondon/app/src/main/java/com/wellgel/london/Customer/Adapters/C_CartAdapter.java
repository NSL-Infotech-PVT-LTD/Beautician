package com.wellgel.london.Customer.Adapters;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wellgel.london.Customer.Activities.C_CartDetailAct;
import com.wellgel.london.Customer.C_CartModel;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.DropDown;

import java.util.ArrayList;
import java.util.List;

public class C_CartAdapter extends RecyclerView.Adapter<C_CartAdapter.C_product_holder> {

    C_CartDetailAct context;
    onItemClick onItemClick;
    List<C_CartModel> list;


    private long mLastClickTime = System.currentTimeMillis();
    private long CLICK_TIME_INTERVAL = 600;
    private String spinnerValue;

    public C_CartAdapter(C_CartDetailAct context, List<C_CartModel> list, onItemClick onItemClick) {
        this.context = context;
        this.list = list;
        this.onItemClick = onItemClick;


    }

    @NonNull
    @Override
    public C_product_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.c_custom_cart_detail, parent, false);
        C_CartAdapter.C_product_holder bookHolder = new C_CartAdapter.C_product_holder(view);

        return bookHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final C_product_holder holder, int position) {

        final C_CartModel model = list.get(position);
        holder.productPrice.setText(" £" + model.getCartProductPrice() + ".00");
        holder.productNAme.setText(model.getCartProductName());
        holder.productNAme.setText(model.getCartProductName());
        ArrayList<String> data = new ArrayList<>();
        data.add("1");
        data.add("2");
        data.add("3");
        data.add("More");

        ((DropDown) holder.dropDown).setOptions(data,position);
        ((DropDown) holder.dropDown).setItemListener(context);
        holder.dropDown.setText(model.getCartQuantity() + "");

        Picasso.with(context).load(model.getCartImage()).into(holder.cartProductImage);

        holder.removeProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onItemClick.onItemClick(model.getCartID(), holder.removeProduct.getText().toString(), holder.dropDown.getText().toString());
            }
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class C_product_holder extends RecyclerView.ViewHolder {

        TextView productNAme, productPrice;
        public static TextView removeProduct;
        ImageView cartProductImage;
        DropDown dropDown;


        public C_product_holder(@NonNull View itemView) {
            super(itemView);

            productNAme = itemView.findViewById(R.id.cart_pro_name);
            productPrice = itemView.findViewById(R.id.cart_pro_price);
            removeProduct = itemView.findViewById(R.id.removeFromCart);
            cartProductImage = itemView.findViewById(R.id.cartProductImage);
            dropDown = itemView.findViewById(R.id.dropdown);




        }
    }

    public interface onItemClick {
        public void onItemClick(String id, String value, String quantity);
    }


}
