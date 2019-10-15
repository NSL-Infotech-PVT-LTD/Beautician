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
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wellgel.london.APIs.Customer_APIs;
import com.wellgel.london.APIs.Provider_APIs;
import com.wellgel.london.Customer.Adapters.C_Booked_OrderAdapter;
import com.wellgel.london.Customer.Adapters.C_ChromeKit_Adapte;
import com.wellgel.london.Customer.Adapters.C_Product_Adapte;
import com.wellgel.london.Customer.C_BookedOrderModel;
import com.wellgel.london.Customer.C_ConstantClass;
import com.wellgel.london.Customer.C_Product_model;
import com.wellgel.london.Customer.EcomNotificationAct;
import com.wellgel.london.Customer.Ecom_BookingActivity;
import com.wellgel.london.Customer.SerializeModelClasses.C_AppointmentList;
import com.wellgel.london.Customer.SerializeModelClasses.C_ProductsSerial;
import com.wellgel.london.Provider.Activities.P_AcceptRejectAct;
import com.wellgel.london.Provider.Activities.P_ProfileAct;
import com.wellgel.london.Provider.ModelSerialized.P_AppointmentListSerial;
import com.wellgel.london.Provider.ProviderAdapters.P_Booked_OrderAdapter;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.NailPolishColorAdapter;
import com.wellgel.london.UtilClasses.NailShapeAdapter;
import com.wellgel.london.UtilClasses.PreferencesShared;
import com.wellgel.london.UtilClasses.Retrofit.RetrofitClientInstance;
import com.wellgel.london.UtilClasses.SkinColorAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class C_DashboardAct extends AppCompatActivity implements C_Product_Adapte.onItemClick, C_Booked_OrderAdapter.OnBookingID, P_Booked_OrderAdapter.OnBookingProviderID {

    private RecyclerView c_dash_product_recycler, c_dash_chrome_recycler, c_dash_bookedList_recycler;
    private List<C_Product_model> productList = new ArrayList<>();
    private List<C_Product_model> chromeList = new ArrayList<>();
    private List<C_AppointmentList.Datum> bookedList = new ArrayList<>();
    private List<P_AppointmentListSerial.Datum> bookedListProvider = new ArrayList<>();
    private C_Product_Adapte product_adapte;
    private C_ChromeKit_Adapte chromeKit_adapte;
    private C_Product_model product_model;
    private C_BookedOrderModel bookedModel;
    private CircleImageView c_dash_user_image, navi_profile;
    private PreferencesShared shared;
    private C_DashboardAct activity;
    private String baseUrlImage = C_ConstantClass.IMAGE_BASE_URL + "products/";
    private ProgressDialog progressDoalog;
    private ScrollView scrollView;
    private AutoCompleteTextView getSearchedItem;
    private C_Booked_OrderAdapter bookedAdpter;
    private P_Booked_OrderAdapter bookedAdpterProvider;
    private ImageView notification, c_dash_navi, cart_detail;
    private RelativeLayout lay_notification;
    private LinearLayoutManager linearLayoutManager;
    private TextView seeAll, c_dash_createNail, cart_count, viewAll_products, viewAll_chrome;
    private TextView navi_name, navi_address, navi_dashboard, navi_cart, navi_booking, navi_about_us, navi_rate_us;

    @Override
    protected void onResume() {
        super.onResume();

        SkinColorAdapter.selectedPosition = 0;
        NailShapeAdapter.selectedPosition = 0;
        NailPolishColorAdapter.selectedPosition = 0;
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);
        lay_notification.setAnimation(animation);
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
        notification = findViewById(R.id.notification);
        viewAll_products = findViewById(R.id.viewAll_products);
        lay_notification = findViewById(R.id.lay_notification);
        viewAll_chrome = findViewById(R.id.viewAll_chrome);
        cart_detail = findViewById(R.id.cartValue);
        seeAll = findViewById(R.id.seeAll);

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


        getSearchedItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ConstantClass.isProduct = true;
                    Intent intent = new Intent(activity, C_ViewAllProductsAct.class);
                    intent.putExtra("search", getSearchedItem.getText().toString());
                    startActivity(intent);
                    return true;
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
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, EcomNotificationAct.class));
            }
        });


        if (shared.getString(ConstantClass.ROLL_PLAY).equalsIgnoreCase("1")) {

            c_dash_createNail.setVisibility(View.VISIBLE);
        } else if (shared.getString(ConstantClass.ROLL_PLAY).equalsIgnoreCase("2")) {
            c_dash_createNail.setVisibility(View.GONE);

        }
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


        c_dash_product_recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        c_dash_chrome_recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        c_dash_user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shared.getString(ConstantClass.ROLL_PLAY).equalsIgnoreCase(ConstantClass.ROLL_CUSTOMER))
                    startActivity(new Intent(activity, C_ProfileAct.class));
                else if (shared.getString(ConstantClass.ROLL_PLAY).equalsIgnoreCase(ConstantClass.ROLL_PROVIDER))
                    startActivity(new Intent(activity, P_ProfileAct.class));
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
            product_model.setProddductPrice(getString(R.string.currency) + "12.00");
            product_model.setProductName("chrome powder");
            product_model.setProductImage(C_ConstantClass.IMAGE_BASE_URL + "products/5d6f8fe9d9602.webp");

            chromeList.add(product_model);
        }

        chromeKit_adapte = new C_ChromeKit_Adapte(this, chromeList, new C_ChromeKit_Adapte.onItemClick() {
            @Override
            public void onItemClick(int id) {

            }
        });
        c_dash_chrome_recycler.setAdapter(chromeKit_adapte);


        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, C_AllAppointListAct.class));
            }
        });


        productsAPI("");

        if (shared.getString(ConstantClass.ROLL_PLAY).equalsIgnoreCase(ConstantClass.ROLL_CUSTOMER))
            appointMentList();
        else if (shared.getString(ConstantClass.ROLL_PLAY).equalsIgnoreCase(ConstantClass.ROLL_PROVIDER))
            appointMentListProvider();

    }

    //     Products from server APi
    public void productsAPI(String search) {
        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        // initialize file here

        /*Create handle for the RetrofitInstance interface*/
        Customer_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Customer_APIs.class);
        Call<C_ProductsSerial> call = service.poducts("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"), "latest", search, "10", 1 + "");
        call.enqueue(new Callback<C_ProductsSerial>() {
            @Override
            public void onResponse(Call<C_ProductsSerial> call, Response<C_ProductsSerial> response) {

                progressDoalog.dismiss();

                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            productList.clear();
//                            for (int i = 0; i < response.body().getData().getData().size(); i++) {
//
//                                product_model = new C_Product_model();
//                                if (shared.getString(ConstantClass.ROLL_PLAY).equalsIgnoreCase(ConstantClass.ROLL_CUSTOMER)) {
//                                    product_model.setProddductPrice(" " + getString(R.string.currency) + response.body().getData().getData().get(i).getPrice() + "");
//                                } else if (shared.getString(ConstantClass.ROLL_PLAY).equalsIgnoreCase(ConstantClass.ROLL_PROVIDER)) {
//                                    product_model.setProddductPrice(" " + getString(R.string.currency) + response.body().getData().getData().get(i).getWholesale_price() + "");
//                                }
//                                product_model.setProductName(response.body().getData().getData().get(i).getName());
//                                product_model.setProductImage(baseUrlImage + response.body().getData().getData().get(i).getImage());
//                                product_model.setProductID(response.body().getData().getData().get(i).getId());
//
//                                productList.add(product_model);
//                            }
                            product_adapte = new C_Product_Adapte(activity, response.body().getData().getData(), activity);
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
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Inflate the view from a predefined XML layout

        // create a 300px width and 470px height PopupWindow


        // inflate your layout or dynamically add view

        View view = inflater.inflate(R.layout.c_drawer_layout, (ViewGroup) findViewById(R.id.main));
        final PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        // display the popup in the center
        popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);
        RelativeLayout main = view.findViewById(R.id.main);
        TextView myOrder = view.findViewById(R.id.myOrders);
        TextView logout = view.findViewById(R.id.logout);

        //drawer ids
        navi_about_us = view.findViewById(R.id.navi_about_us);
        navi_address = view.findViewById(R.id.navi_address);
        navi_booking = view.findViewById(R.id.navi_booking);
        navi_name = view.findViewById(R.id.navi_name);
        navi_dashboard = view.findViewById(R.id.navi_dashboard);
        navi_cart = view.findViewById(R.id.navi_cart);
        navi_rate_us = view.findViewById(R.id.navi_rate_us);
        navi_profile = view.findViewById(R.id.profileImage);


        navi_name.setText(shared.getString(ConstantClass.USER_NAME));
        navi_address.setText(shared.getString(ConstantClass.USER_Address));
        Picasso.with(activity).load(shared.getString("profile")).into(navi_profile);

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
        navi_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                startActivity(new Intent(activity, Ecom_BookingActivity.class));
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


        return popupWindow;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Picasso.with(activity).load(shared.getString("profile")).into(c_dash_user_image);

    }


    public void appointMentList() {

        Customer_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Customer_APIs.class);
        Call<C_AppointmentList> call = service.appointmentList("application/x-www-form-urlencoded", "Bearer  " + shared.getString("token"));
        call.enqueue(new Callback<C_AppointmentList>() {
            @Override
            public void onResponse(Call<C_AppointmentList> call, Response<C_AppointmentList> response) {


                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {


                            seeAll.setText("see all(" + response.body().getData().size() + ")");

                            if (response.body().getData().size() > 5) {
                                bookedList.add(response.body().getData().get(0));
                                bookedList.add(response.body().getData().get(1));
                                bookedList.add(response.body().getData().get(2));
                                bookedList.add(response.body().getData().get(3));
                                bookedList.add(response.body().getData().get(4));
                                bookedAdpter = new C_Booked_OrderAdapter(activity, bookedList, activity);
                            } else {
                                bookedAdpter = new C_Booked_OrderAdapter(activity, response.body().getData(), activity);
                            }


                            c_dash_bookedList_recycler.setAdapter(bookedAdpter);
                        } else {
                        }

                    }
                } else {


                    try {
                        JSONObject jObjError = null;
                        if (response.errorBody() != null) {
                            jObjError = new JSONObject(response.errorBody().string());
                            String errorMessage = jObjError.getJSONObject("error").getJSONObject("error_message").getJSONArray("message").getString(0);

                            Toast.makeText(activity, "" + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                    }
                }
            }


            @Override
            public void onFailure(Call<C_AppointmentList> call, Throwable t) {
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void appointMentListProvider() {

        Provider_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Provider_APIs.class);
        Call<P_AppointmentListSerial> call = service.appointmentListProvider("application/x-www-form-urlencoded", "Bearer  " + shared.getString("token"));
        call.enqueue(new Callback<P_AppointmentListSerial>() {
            @Override
            public void onResponse(Call<P_AppointmentListSerial> call, Response<P_AppointmentListSerial> response) {


                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {


                            seeAll.setText("see all(" + response.body().getData().size() + ")");

                            if (response.body().getData().size() > 5) {
                                bookedListProvider.add(response.body().getData().get(0));
                                bookedListProvider.add(response.body().getData().get(1));
                                bookedListProvider.add(response.body().getData().get(2));
                                bookedListProvider.add(response.body().getData().get(3));
                                bookedListProvider.add(response.body().getData().get(4));
                                bookedAdpterProvider = new P_Booked_OrderAdapter(activity, bookedListProvider, activity);
                            } else {
                                bookedAdpterProvider = new P_Booked_OrderAdapter(activity, response.body().getData(), activity);
                            }


                            c_dash_bookedList_recycler.setAdapter(bookedAdpterProvider);
                        } else {
                        }

                    }
                } else {


                    try {
                        JSONObject jObjError = null;
                        if (response.errorBody() != null) {
                            jObjError = new JSONObject(response.errorBody().string());
                            String errorMessage = jObjError.getJSONObject("error").getJSONObject("error_message").getJSONArray("message").getString(0);

                            Toast.makeText(activity, "" + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                    }
                }
            }


            @Override
            public void onFailure(Call<P_AppointmentListSerial> call, Throwable t) {
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void getBookinID(int id) {

        Intent intent = new Intent(activity, Ecom_AppointlistDetail.class);
        intent.putExtra("appo_id", id);
        startActivity(intent);

    }


    @Override
    public void getBookingProviderID(int id) {
        Intent intent = new Intent(activity, P_AcceptRejectAct.class);
        intent.putExtra("appo_id", id);
        startActivity(intent);
    }
}
