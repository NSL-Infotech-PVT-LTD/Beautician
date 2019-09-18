package com.wellgel.london.Customer.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.NailPolishColorAdapter;
import com.wellgel.london.UtilClasses.NailShapeAdapter;
import com.wellgel.london.UtilClasses.SkinColorAdapter;

public class C_CreateNailAct extends AppCompatActivity implements View.OnClickListener {

    private int[] NailImages = new int[]{R.drawable.nail_one, R.drawable.nail_two, R.drawable.nail_three, R.drawable.nail_four, R.drawable.nail_five};
    TextView bt_nailPolishColor;
    TextView bt_nailShape;
    TextView bt_skinColor;
    private int[][] handSamples = new int[][]{new int[]{R.drawable.square_fair_one, R.drawable.square_round_fair_one, R.drawable.round_fair_one, R.drawable.pointed_fair_one, R.drawable.pointed_round_fair_one}, new int[]{R.drawable.square_fair_two, R.drawable.square_round_fair_two, R.drawable.round_fair_two, R.drawable.pointed_fair_two, R.drawable.pointed_round_fair_two}, new int[]{R.drawable.square_medium, R.drawable.square_round_medium, R.drawable.round_medium, R.drawable.pointed_medium, R.drawable.pointed_round_medium}, new int[]{R.drawable.square_black_one, R.drawable.square_round_black_one, R.drawable.round_black_one, R.drawable.pointed_black_one, R.drawable.pointed_round_black_one}, new int[]{R.drawable.square_black_two, R.drawable.square_round_black_two, R.drawable.round_black_two, R.drawable.pointed_black_two, R.drawable.pointed_round_black_two}};
    ImageView im_hands;
    RelativeLayout ln_nailColor;
    private NailPolishColorAdapter nailPolishColorAdapter;
    private NailShapeAdapter nailShapeAdapter;
    private String[] nail_colors = new String[]{"#FFFFFF", "#CC66CC", "#333366", "#009999", "#CC00CC", "#0033FF", "#99FFFF", "#CCFF99", "#006633", "#CC9900", "#33FF00", "#669966", "#666666", "#00FFCC", "#993333", "#990099", "#9999FF"};
    private int naleShapePos;
    RecyclerView recyclerView;
    private SkinColorAdapter skinColorAdapter;
    private int skinComplexPos;
    private String[] skin_colors = new String[]{"#F2D9B7", "#EFB38A", "#A07561", "#795D4C", "#3D2923"};
    private Drawable top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__create_nail);

        ln_nailColor = findViewById(R.id.mainLAyout);
        bt_skinColor = findViewById(R.id.skin_color);
        bt_nailShape = findViewById(R.id.nail_shape);
        bt_nailPolishColor = findViewById(R.id.nail_polish);

        recyclerView = findViewById(R.id.color_recycler);
        im_hands = findViewById(R.id.hand_image);

        bt_skinColor.setOnClickListener(this);
        bt_nailShape.setOnClickListener(this);
        bt_nailPolishColor.setOnClickListener(this);
        im_hands.setImageResource(handSamples[SkinColorAdapter.selectedPosition][NailShapeAdapter.selectedPosition]);
        setButtonDefaultStyle();
        bt_nailPolishColor.setTextColor(ContextCompat.getColor(this, R.color.pink));
        bt_nailPolishColor.setTypeface(null, Typeface.BOLD);

//        top = ContextCompat.getDrawable(this, R.drawable.nail_police_color_selected);
        bt_nailPolishColor.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
        nailPolishColorAdapter = new NailPolishColorAdapter(this, "nail_color", nail_colors, new NailPolishColorAdapter.onItemClickListener() {
            public void onPerformClick(int pos, View view) {
                ln_nailColor.setBackgroundColor(Color.parseColor(nail_colors[pos]));
            }
        });
        recyclerView.setAdapter(nailPolishColorAdapter);
    }

    @Override
    public void onClick(View view) {
        TextView button = (TextView) view;

        switch (view.getId()) {
            case R.id.nail_polish:
                setButtonDefaultStyle();
                button.setTextColor(ContextCompat.getColor(this, R.color.pink));
                button.setTypeface(null, Typeface.BOLD);

                button.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                nailPolishColorAdapter = new NailPolishColorAdapter(this, "nail_color", nail_colors, new NailPolishColorAdapter.onItemClickListener() {
                    public void onPerformClick(int pos, View view) {
                        ln_nailColor.setBackgroundColor(Color.parseColor(nail_colors[pos]));
                    }
                });
                recyclerView.setAdapter(nailPolishColorAdapter);
                return;


            case R.id.skin_color:
                setButtonDefaultStyle();
                button.setTextColor(ContextCompat.getColor(this, R.color.pink));
//                top = ContextCompat.getDrawable(this, R.drawable.skin_color_select);
                button.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                button.setTypeface(null, Typeface.BOLD);
                skinColorAdapter = new SkinColorAdapter(this, "skin_color", skin_colors, new SkinColorAdapter.onItemClickListener() {
                    public void onPerformClick(int pos, View view) {
                        im_hands.setImageResource(handSamples[SkinColorAdapter.selectedPosition][NailShapeAdapter.selectedPosition]);
                    }
                });
                recyclerView.setAdapter(skinColorAdapter);
                return;
            case R.id.nail_shape:
                setButtonDefaultStyle();
                button.setTextColor(ContextCompat.getColor(this, R.color.pink));
//                top = ContextCompat.getDrawable(this, R.drawable.nail_shape_selected);
                button.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                button.setTypeface(null, Typeface.BOLD);
                nailShapeAdapter = new NailShapeAdapter(this, "nail_shape", NailImages, new NailShapeAdapter.onItemClickListener() {
                    public void onPerformClick(int pos, View view) {
                        try {
                            im_hands.setImageResource(handSamples[SkinColorAdapter.selectedPosition][NailShapeAdapter.selectedPosition]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                recyclerView.setAdapter(nailShapeAdapter);
                return;
            default:
                return;
        }


    }

    /* Access modifiers changed, original: 0000 */
    public void setButtonDefaultStyle() {
        bt_skinColor.setTextColor(Color.parseColor("#000000"));
        bt_skinColor.setTypeface(null, Typeface.NORMAL);
        bt_nailPolishColor.setTypeface(null, Typeface.NORMAL);
        bt_nailShape.setTypeface(null, Typeface.NORMAL);
        bt_skinColor.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
        bt_nailShape.setTextColor(Color.parseColor("#000000"));
        bt_nailShape.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
        bt_nailPolishColor.setTextColor(Color.parseColor("#000000"));
        bt_nailPolishColor.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
    }
}