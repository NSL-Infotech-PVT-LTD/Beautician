package com.wellgel.london.Customer.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import android.widget.Toast;

import com.wellgel.london.Customer.C_SkinColorModel;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.NailPolishColorAdapter;
import com.wellgel.london.UtilClasses.NailShapeAdapter;
import com.wellgel.london.UtilClasses.SkinColorAdapter;

import java.util.ArrayList;
import java.util.List;

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
    RecyclerView recyclerView;
    private SkinColorAdapter skinColorAdapter;
    //    private String[] skin_colors = new String[]{"#F2D9B7", "#EFB38A", "#A07561", "#795D4C", "#3D2923"};
    private ArrayList<String> listSkinColor = new ArrayList<>();
    private ArrayList<String> listNailColor = new ArrayList<>();
    private ArrayList<String> listNailShape = new ArrayList<>();
    private Drawable top;
    private TextView chooseSalon;
    ImageView backPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__create_nail);

        ln_nailColor = findViewById(R.id.mainLAyout);
        bt_skinColor = findViewById(R.id.skin_color);
        bt_nailShape = findViewById(R.id.nail_shape);
        bt_nailPolishColor = findViewById(R.id.nail_polish);

        recyclerView = findViewById(R.id.color_recycler);
        backPress = findViewById(R.id.c_dash_navi);
        im_hands = findViewById(R.id.hand_image);
        chooseSalon = findViewById(R.id.chooseSalon);


        chooseSalon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(C_CreateNailAct.this, C_NearBySalonAct.class));
            }
        });

        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        bt_skinColor.setOnClickListener(this);
        bt_nailShape.setOnClickListener(this);
        bt_nailPolishColor.setOnClickListener(this);
        im_hands.setImageResource(handSamples[SkinColorAdapter.selectedPosition][NailShapeAdapter.selectedPosition]);
        setButtonDefaultStyle();
        bt_nailPolishColor.setTextColor(ContextCompat.getColor(this, R.color.pink));
        bt_nailPolishColor.setTypeface(null, Typeface.BOLD);


//        listNailColor.add(0, "#FFFFFF");
//        listNailColor.add(1, "#CC66CC");
//        listNailColor.add(2, "#333366");
//        listNailColor.add(3, "#009999");
//        listNailColor.add(4, "#CC00CC");
//        listNailColor.add(5, "#0033FF");
//        listNailColor.add(6, "#99FFFF");
//        listNailColor.add(7, "#CCFF99");
//        listNailColor.add(8, "#006633");
//        listNailColor.add(9, "#CC9900");
//        listNailColor.add(10, "#33FF00");
//        listNailColor.add(11, "#669966");
//        listNailColor.add(12, "#666666");
//        listNailColor.add(13, "#00FFCC");
//        listNailColor.add(14, "#993333");
//        listNailColor.add(15, "#990099");
//        listNailColor.add(16, "#9999FF");
//
//
//        listSkinColor.add(0, "#F2D9B7");
//        listSkinColor.add(1, "#EFB38A");
//        listSkinColor.add(2, "#A07561");
//        listSkinColor.add(3, "#795D4C");
//        listSkinColor.add(4, "#3D2923");
//
//
//        listNailShape.add(0, "square");
//        listNailShape.add(1, "round");
//        listNailShape.add(2, "oval");
//        listNailShape.add(3, "stillete");
//        listNailShape.add(4, "pointed");

        ConstantClass.ListFunc(listSkinColor, listNailColor, listNailShape);

        ConstantClass.NAIL_POlish_COLOR = listNailColor.get(0);
        ConstantClass.NAIL_SHAPE = listNailShape.get(0);
        ConstantClass.HAND_COLOR = listSkinColor.get(0);


//        top = ContextCompat.getDrawable(this, R.drawable.nail_police_color_selected);
        bt_nailPolishColor.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
        nailPolishColorAdapter = new NailPolishColorAdapter(this, "nail_color", listNailColor, new NailPolishColorAdapter.onItemClickListener() {
            public void onPerformClick(int pos, View view, String color) {
                ConstantClass.NAIL_POlish_COLOR = color;
                ln_nailColor.setBackgroundColor(Color.parseColor(color));
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
                nailPolishColorAdapter = new NailPolishColorAdapter(this, "nail_color", listNailColor, new NailPolishColorAdapter.onItemClickListener() {
                    public void onPerformClick(int pos, View view, String color) {
                        ConstantClass.NAIL_POlish_COLOR = color;
                        ln_nailColor.setBackgroundColor(Color.parseColor(color));
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


                skinColorAdapter = new SkinColorAdapter(this, "skin_color", listSkinColor, new SkinColorAdapter.onItemClickListener() {
                    public void onPerformClick(int pos, View view, String color) {
                        im_hands.setImageResource(handSamples[SkinColorAdapter.selectedPosition][NailShapeAdapter.selectedPosition]);


                        ConstantClass.HAND_COLOR = color;
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
                nailShapeAdapter = new NailShapeAdapter(this, "nail_shape", NailImages, listNailShape, new NailShapeAdapter.onItemClickListener() {
                    public void onPerformClick(int pos, View view, String name) {

                        ConstantClass.NAIL_SHAPE = name;
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