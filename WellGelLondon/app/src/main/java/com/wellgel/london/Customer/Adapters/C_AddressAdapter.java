package com.wellgel.london.Customer.Adapters;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.wellgel.london.Customer.C_AddressModel;
import com.wellgel.london.Customer.C_CartModel;
import com.wellgel.london.R;

import java.util.List;

public class C_AddressAdapter extends RecyclerView.Adapter<C_AddressAdapter.C_product_holder> {

    FragmentActivity context;
    onItemClick onItemClick;
    List<C_AddressModel> list;
    int lastPos = -1;


    private long mLastClickTime = System.currentTimeMillis();
    private long CLICK_TIME_INTERVAL = 600;
    private String spinnerValue;
    public EditAddress editAddress;
    public RemoveAddress removeAddress;

    public C_AddressAdapter(FragmentActivity context, List<C_AddressModel> list, onItemClick onItemClick, EditAddress editAddress, RemoveAddress removeAddress) {
        this.context = context;
        this.list = list;
        this.onItemClick = onItemClick;

        this.editAddress = editAddress;
        this.removeAddress = removeAddress;


    }

    @NonNull
    @Override
    public C_product_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.c_custom_address_list, parent, false);
        C_AddressAdapter.C_product_holder bookHolder = new C_AddressAdapter.C_product_holder(view);

        return bookHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final C_product_holder holder, int position) {

        final C_AddressModel model = list.get(position);
        holder.addressOption.setText(model.getAddresssOption());
        holder.addressType.setText(model.getAddressType());
        holder.addressUSer.setText(model.getAddressUser());
        holder.fullAddress.setText(model.getFullAddress());

        holder.select_address_radio.setChecked(lastPos == position);
        holder.select_address_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onItemClick(model.getAddressID());
                lastPos = position;
                notifyDataSetChanged();


            }
        });

        if (lastPos == position) {
            holder.addressOption.setText("Edit");
            holder.addressOption.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            holder.addressOption.setText("Remove");
            holder.addressOption.setTextColor(context.getResources().getColor(R.color.red));

        }

        holder.addressOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = holder.addressOption.getText().toString();
                if (value.equalsIgnoreCase("Edit")) {

                    editAddress.onEditClick(model.getAddressID());
                } else if (value.equalsIgnoreCase("Remove")) {

                    removeAddress.onRemove(model.getAddressID());
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class C_product_holder extends RecyclerView.ViewHolder {

        TextView fullAddress, addressUSer, addressType, addressOption;
        RadioButton select_address_radio;


        public C_product_holder(@NonNull View itemView) {
            super(itemView);

            fullAddress = itemView.findViewById(R.id.address_fullr);
            addressUSer = itemView.findViewById(R.id.address_user);
            addressType = itemView.findViewById(R.id.address_type);
            select_address_radio = itemView.findViewById(R.id.select_address_radio);
            addressOption = itemView.findViewById(R.id.address_option);

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
