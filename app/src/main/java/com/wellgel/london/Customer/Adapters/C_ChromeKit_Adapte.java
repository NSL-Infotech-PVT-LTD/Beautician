package com.wellgel.london.Customer.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wellgel.london.Customer.C_ConstantClass;
import com.wellgel.london.Customer.C_Product_model;
import com.wellgel.london.Customer.SerializeModelClasses.C_ProductsSerial;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.PreferencesShared;

import java.util.ArrayList;
import java.util.List;

public class C_ChromeKit_Adapte extends RecyclerView.Adapter<C_ChromeKit_Adapte.C_product_holder> {

    Context context;
    onItemClick onItemClick;
    List<C_Product_model> list;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private PreferencesShared shared;
    private String baseUrlImage = C_ConstantClass.IMAGE_BASE_URL + "products/";

    public C_ChromeKit_Adapte(Context context, List<C_Product_model> list, onItemClick onItemClick) {
        this.context = context;
        this.list = list;
        this.onItemClick = onItemClick;

        shared = new PreferencesShared(context);


    }

    public C_ChromeKit_Adapte(FragmentActivity context, onItemClick onItemClick) {
        this.context = context;
        list = new ArrayList<>();
        this.onItemClick = onItemClick;
        shared = new PreferencesShared(context);


    }

    @NonNull
    @Override
    public C_product_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        C_product_holder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);

                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;

//        switch (viewType) {
//            case ITEM:
//                return new C_product_holder(
//                        LayoutInflater.from(parent.getContext()).inflate(R.layout.c_custom_express_gel_layout, parent, false));
//            case LOADING:
//                return new C_product_holder(
//                        LayoutInflater.from(parent.getContext()).inflate(R.layout.c_custom_express_gel_layout, parent, false));
//            default:
//                return null;
//        }
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View view = layoutInflater.inflate(R.layout.c_custom_express_gel_layout, parent, false);
//        C_Product_Adapte.C_product_holder bookHolder = new C_Product_Adapte.C_product_holder(view);

//        return bookHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final C_product_holder holder, int position) {

        final C_Product_model model = list.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                if (shared.getString(ConstantClass.ROLL_PLAY).equalsIgnoreCase(ConstantClass.ROLL_CUSTOMER)) {
                    holder.productPrice.setText( model.getProddductPrice() + "");
                } else if (shared.getString(ConstantClass.ROLL_PLAY).equalsIgnoreCase(ConstantClass.ROLL_PROVIDER)) {
                    holder.productPrice.setText( model.getProddductPrice() + "");
                }
//                holder.productPrice.setText(model.getPrice() + "");
                holder.productNAme.setText(model.getProductName());


                Picasso.with(context).load(model.getProductImage()).placeholder(R.mipmap.logo).into(holder.productImage);
                break;
            case LOADING:
//                Do nothing
                break;

        }
    }

    @NonNull
    private C_product_holder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        C_product_holder viewHolder;
        View v1 = inflater.inflate(R.layout.c_custom_express_gel_layout, parent, false);
        viewHolder = new C_product_holder(v1);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    protected class LoadingVH extends C_product_holder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }
    /*
   Helpers
   _________________________________________________________________________________________________
    */


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


//    public List<C_Product_model> getProduct() {
//        return list;
//    }
//
//    public void setProduct(List<C_Product_model> list) {
//        this.list = list;
//    }


    public interface onItemClick {
        public void onItemClick(int id);
    }


}
