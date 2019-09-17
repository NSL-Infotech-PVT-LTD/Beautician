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
import com.wellgel.london.Customer.C_Product_model;
import com.wellgel.london.R;

import java.util.List;

public class C_Product_Adapte extends RecyclerView.Adapter<C_Product_Adapte.C_product_holder> {

    FragmentActivity context;
    onItemClick onItemClick;
    List<C_Product_model> list;


    private long mLastClickTime = System.currentTimeMillis();
    private long CLICK_TIME_INTERVAL = 600;

    public C_Product_Adapte(FragmentActivity context, List<C_Product_model> list, onItemClick onItemClick) {
        this.context = context;
        this.list = list;
        this.onItemClick = onItemClick;


    }

    @NonNull
    @Override
    public C_product_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.c_custom_express_gel_layout, parent, false);
        C_Product_Adapte.C_product_holder bookHolder = new C_Product_Adapte.C_product_holder(view);

        return bookHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final C_product_holder holder, int position) {

        final C_Product_model model = list.get(position);
        holder.productPrice.setText(model.getProddductPrice());
        holder.productNAme.setText(model.getProductName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onItemClick(model.getProductID());
            }
        });

        Picasso.with(context).load(model.getProductImage()).into(holder.productImage);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class C_product_holder extends RecyclerView.ViewHolder {

        TextView productNAme, productPrice;
        ImageView productImage;


        public C_product_holder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.c_exgel_iv);
            productNAme = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);


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


    public interface onItemClick {
        public void onItemClick(int id);
    }


}
