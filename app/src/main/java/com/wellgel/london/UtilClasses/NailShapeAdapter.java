package com.wellgel.london.UtilClasses;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.wellgel.london.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NailShapeAdapter extends RecyclerView.Adapter<NailShapeAdapter.MyViewHolder> {
    public static int selectedPosition = 0;
    private String FLAG = null;
    private String[] colors;
    private Context context;
    private int[] imageId;
    private LayoutInflater inflater;
    private onItemClickListener listener;
    private ArrayList<String> list;

    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.image = (CircleImageView) itemView.findViewById(R.id.circleImage);
        }
    }

    public interface onItemClickListener {
        void onPerformClick(int i, View view, String name);
    }

    public NailShapeAdapter(Context context, String FLAG, int[] menuImages, ArrayList<String> list, onItemClickListener listener) {
        this.context = context;
        this.imageId = menuImages;
        this.listener = listener;
        this.list = list;
        this.FLAG = FLAG;
        this.inflater = LayoutInflater.from(context);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(this.inflater.inflate(R.layout.skin_color_row, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (this.FLAG.equalsIgnoreCase("nail_color") || this.FLAG.equalsIgnoreCase("skin_color")) {
            holder.image.setImageDrawable(new ColorDrawable(Color.parseColor(this.colors[position])));
        } else {
            holder.image.setImageResource(this.imageId[position]);
        }
        if (selectedPosition == position) {
            holder.image.setBorderColor(this.context.getResources().getColor(R.color.pink));
        } else {
            holder.image.setBorderColor(this.context.getResources().getColor(R.color.nail_polish_colr));
        }
        holder.image.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                NailShapeAdapter.this.notifyItemChanged(NailShapeAdapter.selectedPosition);
                NailShapeAdapter.selectedPosition = position;
                NailShapeAdapter.this.notifyItemChanged(NailShapeAdapter.selectedPosition);
                NailShapeAdapter.this.listener.onPerformClick(position, v, list.get(position));
            }
        });
    }

    public int getItemCount() {
        if (this.FLAG.equalsIgnoreCase("nail_color") || this.FLAG.equalsIgnoreCase("skin_color")) {
            return this.colors.length;
        }
        return this.imageId.length;
    }
}
