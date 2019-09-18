package com.wellgel.london.Customer.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wellgel.london.Customer.APIs;
import com.wellgel.london.Customer.Adapters.C_Booked_OrderAdapter;
import com.wellgel.london.Customer.Adapters.C_Product_Adapte;
import com.wellgel.london.Customer.C_BookedOrderModel;
import com.wellgel.london.Customer.C_ConstantClass;
import com.wellgel.london.Customer.C_Product_model;
import com.wellgel.london.Customer.SerializeModelClasses.C_ProductsSerial;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.PreferencesShared;
import com.wellgel.london.UtilClasses.Retrofit.RetrofitClientInstance;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class C_DashboardAct extends AppCompatActivity implements C_Product_Adapte.onItemClick {

    private RecyclerView c_dash_product_recycler, c_dash_chrome_recycler, c_dash_bookedList_recycler;
    private List<C_Product_model> productList = new ArrayList<>();
    private List<C_Product_model> chromeList = new ArrayList<>();
    private List<C_BookedOrderModel> bookedList = new ArrayList<>();
    private C_Product_Adapte product_adapte;
    private C_Product_Adapte chromAdpater;
    private C_Product_model product_model;
    private C_BookedOrderModel bookedModel;
    private CircleImageView c_dash_user_image;
    private PreferencesShared shared;
    private C_DashboardAct activity;
    private String baseUrlImage = C_ConstantClass.IMAGE_BASE_URL + "products/";
    private ProgressDialog progressDoalog;
    private ScrollView scrollView;
    private AutoCompleteTextView getSearchedItem;
    private C_Booked_OrderAdapter bookedAdpter;
    private ImageView c_dash_navi, cart_detail;
    private LinearLayoutManager linearLayoutManager;
    private TextView c_dash_createNail, cart_count, viewAll_products, viewAll_chrome;
    private PopupWindow popupWindow;

    @Override
    protected void onResume() {
        super.onResume();
        if (shared.getString("cartsize").equalsIgnoreCase("")) {
            cart_count.setVisibility(View.GONE);
        } else {
            cart_count.setVisibility(View.VISIBLE);
            cart_count.setText(shared.getString("cartsize"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__dashboard);
        activity = this;
        shared = new PreferencesShared(activity);
        c_dash_product_recycler = findViewById(R.id.c_dash_product_recycler);
        c_dash_bookedList_recycler = findViewById(R.id.c_dash_bookedList_recycler);
        c_dash_navi = findViewById(R.id.c_dash_navi);
        c_dash_chrome_recycler = findViewById(R.id.c_dash_chrome_recycler);
        c_dash_user_image = findViewById(R.id.c_dash_user_image);
        c_dash_createNail = findViewById(R.id.c_dash_createNail);
        cart_count = findViewById(R.id.cart_count);
        viewAll_products = findViewById(R.id.viewAll_products);
        viewAll_chrome = findViewById(R.id.viewAll_chrome);
        cart_detail = findViewById(R.id.cartValue);
        getSearchedItem = findViewById(R.id.getSearchedItem);


        getSearchedItem.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (getSearchedItem.getRight() - getSearchedItem.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        // your action her

                        ConstantClass.isProduct = true;
                        Intent intent = new Intent(activity, C_ViewAllProductsAct.class);
                        intent.putExtra("search", getSearchedItem.getText().toString());
                        startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });

        cart_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, C_CartDetailAct.class));
            }
        });
        c_dash_createNail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, C_CreateNailAct.class));
            }
        });
        c_dash_navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupWindow popupwindow_obj = popupDisplay();
                popupwindow_obj.showAsDropDown(view, 200, 18);
            }
        });


        Picasso.with(activity).load(shared.getString("profile")).into(c_dash_user_image);
        c_dash_product_recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        c_dash_chrome_recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        c_dash_user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, C_ProfileAct.class));
            }
        });
        viewAll_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantClass.isProduct = true;
                Intent intent = new Intent(activity, C_ViewAllProductsAct.class);
                intent.putExtra("search", "");
                startActivity(intent);

            }
        });
        viewAll_chrome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantClass.isProduct = false;

                startActivity(new Intent(activity, C_ViewAllProductsAct.class));
            }
        });
        linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        c_dash_bookedList_recycler.setLayoutManager(linearLayoutManager);

        for (int i = 0; i < 5; i++) {
            product_model = new C_Product_model();
            product_model.setProddductPrice("€12.00");
            product_model.setProductName("chrome powder");
            product_model.setProductImage(C_ConstantClass.IMAGE_BASE_URL + "products/5d6f8fe9d9602.webp");

            chromeList.add(product_model);
        }
        for (int i = 0; i < 5; i++) {
            bookedModel = new C_BookedOrderModel();
            bookedModel.setBookedSalon("manly and sons barber co..");
            bookedModel.setBookedDate("21\nToaday");
            bookedModel.setBookedSalonAdd("85,ca street ,Los angeles");

            bookedList.add(bookedModel);
        }
        chromAdpater = new C_Product_Adapte(this, chromeList, new C_Product_Adapte.onItemClick() {
            @Override
            public void onItemClick(int id) {

            }
        });
        bookedAdpter = new C_Booked_OrderAdapter(this, bookedList);
        c_dash_chrome_recycler.setAdapter(chromAdpater);
        c_dash_bookedList_recycler.setAdapter(bookedAdpter);
        productsAPI("");
    }

    //     Products from server APi
    public void productsAPI(String search) {
        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        // initialize file here

        /*Create handle for the RetrofitInstance interface*/
        APIs service = RetrofitClientInstance.getRetrofitInstance().create(APIs.class);
        Call<C_ProductsSerial> call = service.poducts("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"), "latest", search, "10", 1 + "");
        call.enqueue(new Callback<C_ProductsSerial>() {
            @Override
            public void onResponse(Call<C_ProductsSerial> call, Response<C_ProductsSerial> response) {


                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            productList.clear();
                            if (progressDoalog != null)
                                progressDoalog.dismiss();
                            for (int i = 0; i < response.body().getData().getData().size(); i++) {

                                product_model = new C_Product_model();
                                product_model.setProddductPrice(" £" + response.body().getData().getData().get(i).getPrice() + "");
                                product_model.setProductName(response.body().getData().getData().get(i).getName());
                                product_model.setProductImage(baseUrlImage + response.body().getData().getData().get(i).getImage());
                                product_model.setProductID(response.body().getData().getData().get(i).getId());

                                productList.add(product_model);
                            }
                            product_adapte = new C_Product_Adapte(activity, productList, activity);
                            c_dash_product_recycler.setAdapter(product_adapte);

                            String[] array = new String[productList.size()];

                            for (int i = 0; i < productList.size(); i++) {
                                array[i] = productList.get(i).getProductName();
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, array);
                            getSearchedItem.setThreshold(2);
                            getSearchedItem.setAdapter(adapter);

                            getSearchedItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    ConstantClass.isProduct = true;
                                    Intent intent = new Intent(activity, C_ViewAllProductsAct.class);
                                    intent.putExtra("search", adapterView.getItemAtPosition(i).toString());
                                    startActivity(intent);
                                }
                            });
                        } else {
                        }

                    }
                } else {

                    if (progressDoalog != null)
                        progressDoalog.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String errorMessage = jObjError.getJSONObject("error").getJSONObject("error_message").getJSONArray("message").getString(0);

                        Toast.makeText(activity, "" + errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }
            }


            @Override
            public void onFailure(Call<C_ProductsSerial> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(this);
        super.onBackPressed();

    }

    @Override
    public void onItemClick(int id) {


        Intent intent = new Intent(activity, C_ProductsDetailAct.class);
        intent.putExtra("product_id", id);
        startActivity(intent);
    }

    public PopupWindow popupDisplay() {


        popupWindow = new PopupWindow(this);

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.c_drawer_layout, null);
        RelativeLayout main = view.findViewById(R.id.main);
        TextView myOrder = view.findViewById(R.id.myOrders);
        TextView logout = view.findViewById(R.id.logout);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        myOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                startActivity(new Intent(activity, C_MyOrdersActivity.class));
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(C_DashboardAct.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("Are you sure you want to Logout")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                shared.clearShared();
                                popupWindow.dismiss();

                                startActivity(new Intent(C_DashboardAct.this, C_LoginAsActivity.class));
                                finish();

                            }

                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                popupWindow.dismiss();

                            }

                        })
                        .show();
            }

        });


        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                popupWindow.dismiss();
            }
        });
//
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setContentView(view);

        return popupWindow;
    }

}
