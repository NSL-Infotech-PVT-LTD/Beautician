package com.wellgel.london.Provider.ProviderAdapters;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.wellgel.london.Customer.C_Product_model;
import com.wellgel.london.R;

import java.util.ArrayList;
import java.util.List;

public class P_Adapter_Services extends RecyclerView.Adapter<P_Adapter_Services.P_Adapter_Services_holder> {

    Context context;
    List<C_Product_model> list;
    private int row_index = -1;

    private boolean value = true;

    public P_Adapter_Services(Context context, List<C_Product_model> list) {
        this.context = context;
        this.list = list;


    }

    @NonNull
    @Override
    public P_Adapter_Services_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.p_custom_service_added, parent, false);
        P_Adapter_Services.P_Adapter_Services_holder services_holder = new P_Adapter_Services.P_Adapter_Services_holder(view);

        return services_holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final P_Adapter_Services_holder holder, final int position) {

        final C_Product_model model = list.get(position);
        holder.serviceName.setText(model.getProductName());
        Chip maleChip = getChip(holder.chipGroup, model.getProductName());
        holder.chipGroup.addView(maleChip);

        holder.serviceName.setVisibility(View.GONE);




//        holder.serviceName.setTextColor(model.isChecked() ? Color.parseColor("#ffffff") : Color.parseColor("#000000"));
//        holder.backgroundLayout.setBackground(model.isChecked() ? context.getResources().getDrawable(R.drawable.capsule_button_shape) : context.getResources().getDrawable(R.drawable.capsule_button_shape_grey));


    }

    private Chip getChip(final ChipGroup chipGroup, String text) {
        final Chip chip = new Chip(context);
        chip.setChipDrawable(ChipDrawable.createFromResource(context, R.xml.chip));
        int paddingDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());
        chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
        chip.setText(text);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipGroup.removeView(chip);
            }
        });
        return chip;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class P_Adapter_Services_holder extends RecyclerView.ViewHolder {

        TextView serviceName;
        LinearLayout backgroundLayout;
        private ChipGroup chipGroup;

        public P_Adapter_Services_holder(@NonNull View itemView) {
            super(itemView);
            chipGroup = (ChipGroup) itemView.findViewById(R.id.user);
            serviceName = itemView.findViewById(R.id.p_service_tv);
            backgroundLayout = itemView.findViewById(R.id.backgroundLayout);

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

    public List<C_Product_model> getAll() {
        return list;
    }

    public List<C_Product_model> getSelected() {
        ArrayList<C_Product_model> selected = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isChecked()) {
                selected.add(list.get(i));
            }
        }
        return selected;
    }

}
