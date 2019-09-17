package com.wellgel.london.Customer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wellgel.london.Customer.APIs;
import com.wellgel.london.Customer.Adapters.C_CartAdapter;
import com.wellgel.london.Customer.C_CartModel;
import com.wellgel.london.Customer.C_ConstantClass;
import com.wellgel.london.Customer.SerializeModelClasses.C_CartDetailSerialize;
import com.wellgel.london.Customer.SerializeModelClasses.C_CartSerailized;
import com.wellgel.london.Customer.SerializeModelClasses.C_OrderSerial;
import com.wellgel.london.Customer.SerializeModelClasses.ProductDetailSerial;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;


import com.wellgel.london.UtilClasses.DropDown;
import com.wellgel.london.UtilClasses.PreferencesShared;
import com.wellgel.london.UtilClasses.Retrofit.RetrofitClientInstance;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class C_CartDetailAct extends AppCompatActivity implements DropDown.IQuantitySelected {


    private ImageView cartDeatil, cartbackPress;
    private RecyclerView cartRecycler;
    private C_CartAdapter adapter;
    private C_CartModel model;
    private TextView mycartText;
    private List<C_CartModel> list = new ArrayList<>();
    private C_CartDetailAct activity;

    private RelativeLayout placeOrder;
    private ProgressDialog progressDoalog;
    private String backString = "";
    private PreferencesShared shared;
    private TextView totalItems, cart_count, ordertxt, totalAmount, itemPrice;
    private CardView cardViewOfamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__cart_detail);

        init();
    }

    private void init() {

        backString = "yes";
        activity = this;
        shared = new PreferencesShared(activity);
        cartDeatil = findViewById(R.id.no_cart_data);
        cartbackPress = findViewById(R.id.c_cart_back);
        mycartText = findViewById(R.id.mycartText);
        cartRecycler = findViewById(R.id.cartRecycler);
        totalItems = findViewById(R.id.totalItems);
        totalAmount = findViewById(R.id.totalAmount);
        itemPrice = findViewById(R.id.itemPrice);
        ordertxt = findViewById(R.id.ordertxt);
        placeOrder = findViewById(R.id.placeOrder);
        cart_count = findViewById(R.id.cart_count);
        cardViewOfamount = findViewById(R.id.cardViewOfamount);

        progressDoalog = new ProgressDialog(activity);

        if (ConstantClass.FromAddress) {
            ordertxt.setText(getString(R.string.checkOut));

        } else {
            ordertxt.setText(getString(R.string.placeOrder));
        }

        cartRecycler.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });

        cartRecycler.setHasFixedSize(true);


        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ordertxt.getText().toString().equalsIgnoreCase(getString(R.string.checkOut))) {

                    requestOrder(getIntent().getIntExtra("address_id", 1));
                } else {
                    startActivity(new Intent(C_CartDetailAct.this, C_AddressAct.class));
                }
            }
        });


        cartbackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getCartDetail();
    }


    public void getCartDetail() {

        // initialize file here
        showProgress();

        /*Create handle for the RetrofitInstance interface*/
        APIs service = RetrofitClientInstance.getRetrofitInstance().create(APIs.class);
        Call<C_CartDetailSerialize> call = service.cartDetail("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"));
        call.enqueue(new Callback<C_CartDetailSerialize>() {
            @Override
            public void onResponse(Call<C_CartDetailSerialize> call, Response<C_CartDetailSerialize> response) {

                hideProgress();

                if (response.body() != null) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {

                            if (response.body().getData().size() > 0) {
                                if (response.body().getData().get(0).getCartdetails().size() > 0) {
                                    cardViewOfamount.setVisibility(View.VISIBLE);
                                    cart_count.setVisibility(View.VISIBLE);

                                    for (int i = 0; i < response.body().getData().get(0).getCartdetails().size(); i++) {
                                        model = new C_CartModel();
                                        model.setCartProductName(response.body().getData().get(0).getCartdetails().get(i).getProduct().getName());
                                        model.setCartQuantity(response.body().getData().get(0).getCartdetails().get(i).getQuantity());
                                        model.setCartID(response.body().getData().get(0).getCartdetails().get(i).getProduct().getId() + "");
                                        model.setCartProductPrice(response.body().getData().get(0).getCartdetails().get(i).getProduct().getPrice());
                                        model.setCartImage(C_ConstantClass.IMAGE_BASE_URL + "products/" + response.body().getData().get(0).getCartdetails().get(i).getProduct().getImage());
                                        list.add(model);
                                    }

                                    setDataFromSharedPreferences(list);
                                    totalAmount.setText(" £" + grandTotal(list) + ".00");
                                    cart_count.setText(list.size() + "");


                                    shared.setString("cartsize", String.valueOf(list.size()));
                                    itemPrice.setText(" £" + grandTotal(list) + ".00");
                                    adapter = new C_CartAdapter(activity, list, new C_CartAdapter.onItemClick() {
                                        @Override
                                        public void onItemClick(String id, String option, String quantity) {
                                            removeOrUpdateCartApi(id, "delete", quantity);

                                        }
                                    });

                                    cartRecycler.setAdapter(adapter);
                                    if (ConstantClass.FromAddress) {
                                        mycartText.setText(getString(R.string.checkOut));

                                    } else {
                                        mycartText.setText("My Cart(" + list.size() + ")");

                                    }
                                    totalItems.setText("Price (" + list.size() + " items)");


                                    cartDeatil.setVisibility(View.GONE);

                                } else {
                                    cartDeatil.setVisibility(View.VISIBLE);
                                    shared.setString("cartsize", "");
                                    placeOrder.setVisibility(View.GONE);
                                    cardViewOfamount.setVisibility(View.GONE);
                                }
                            } else {
                                cartDeatil.setVisibility(View.VISIBLE);
                                shared.setString("cartsize", "");
                                placeOrder.setVisibility(View.GONE);
                                cardViewOfamount.setVisibility(View.GONE);

                            }
                        } else {
                            cartDeatil.setVisibility(View.VISIBLE);
                            cart_count.setVisibility(View.GONE);
                            placeOrder.setVisibility(View.GONE);
                            cardViewOfamount.setVisibility(View.GONE);
                        }

                    }
                }else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String errorMessage = jObjError.getJSONObject("error").getJSONObject("error_message").getJSONArray("message").getString(0);

                        Toast.makeText(activity, "" + errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }
            }


            @Override
            public void onFailure(Call<C_CartDetailSerialize> call, Throwable t) {
                hideProgress();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private void showProgress() {

        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

    }

    private void hideProgress() {
        progressDoalog.dismiss();
    }

    private int grandTotal(List<C_CartModel> items) {

        int totalPrice = 0;
        for (int i = 0; i < items.size(); i++) {
            totalPrice += items.get(i).getCartProductPrice() * items.get(i).getCartQuantity();
        }

        return totalPrice;
    }


    public void removeOrUpdateCartApi(String productID, String cartOption, String cartQantity) {
        showProgress();
        // initialize file here

        /*Create handle for the RetrofitInstance interface*/
        APIs service = RetrofitClientInstance.getRetrofitInstance().create(APIs.class);
        Call<C_CartSerailized> call = service.cart("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"), productID, cartQantity, cartOption);
        call.enqueue(new Callback<C_CartSerailized>() {
            @Override
            public void onResponse(Call<C_CartSerailized> call, Response<C_CartSerailized> response) {

                hideProgress();

                if (response.body() != null) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                            list.clear();

                            if (response.body().getData().getCart().size() > 0) {

                                if (response.body().getData().getCart().get(0).getCartdetails().size() > 0) {
                                    cardViewOfamount.setVisibility(View.VISIBLE);
                                    cart_count.setVisibility(View.VISIBLE);

                                    for (int i = 0; i < response.body().getData().getCart().get(0).getCartdetails().size(); i++) {
                                        model = new C_CartModel();
                                        model.setCartProductName(response.body().getData().getCart().get(0).getCartdetails().get(i).getProduct().getName());
                                        model.setCartQuantity(response.body().getData().getCart().get(0).getCartdetails().get(i).getQuantity());
                                        model.setCartID(response.body().getData().getCart().get(0).getCartdetails().get(i).getProduct().getId() + "");
                                        model.setCartProductPrice(response.body().getData().getCart().get(0).getCartdetails().get(i).getProduct().getPrice());
                                        model.setCartImage(C_ConstantClass.IMAGE_BASE_URL + "products/" + response.body().getData().getCart().get(0).getCartdetails().get(i).getProduct().getImage());
                                        list.add(model);
                                    }

                                    setDataFromSharedPreferences(list);
                                    totalAmount.setText(" £" + grandTotal(list) + ".00");
                                    cart_count.setText(list.size() + "");


                                    shared.setString("cartsize", String.valueOf(list.size()));
                                    itemPrice.setText(" £" + grandTotal(list) + ".00");
                                    adapter = new C_CartAdapter(activity, list, new C_CartAdapter.onItemClick() {
                                        @Override
                                        public void onItemClick(String id, String option, String quantity) {
                                            removeOrUpdateCartApi(id, "delete", quantity);

                                        }
                                    });

                                    cartRecycler.setAdapter(adapter);
                                    if (ConstantClass.FromAddress) {
                                        mycartText.setText(getString(R.string.checkOut));

                                    } else {
                                        mycartText.setText("My Cart(" + list.size() + ")");

                                    }
                                    totalItems.setText("Price (" + list.size() + " items)");


                                    cartDeatil.setVisibility(View.GONE);

                                } else {
                                    cartDeatil.setVisibility(View.VISIBLE);
                                    shared.setString("cartsize", "");
                                    cardViewOfamount.setVisibility(View.GONE);
                                    mycartText.setText("My Cart(" + list.size() + ")");
                                    placeOrder.setVisibility(View.GONE);
                                }
                            } else {
                                cartDeatil.setVisibility(View.VISIBLE);
                                shared.setString("cartsize", "");
                                cardViewOfamount.setVisibility(View.GONE);
                                placeOrder.setVisibility(View.GONE);
                            }
                        } else {
                            cartDeatil.setVisibility(View.VISIBLE);
                            shared.setString("cartsize", "");
                            cardViewOfamount.setVisibility(View.GONE);
                            placeOrder.setVisibility(View.GONE);

                        }
                    } else {
                        cartDeatil.setVisibility(View.VISIBLE);
                        cart_count.setVisibility(View.GONE);
                        placeOrder.setVisibility(View.GONE);
                        cardViewOfamount.setVisibility(View.GONE);
                    }

                } else {

                }


            }


            @Override
            public void onFailure(Call<C_CartSerailized> call, Throwable t) {
                hideProgress();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void setDataFromSharedPreferences(List<C_CartModel> curProduct) {
        Gson gson = new Gson();
        String jsonCurProduct = gson.toJson(curProduct);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("value", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("list", jsonCurProduct);
        editor.commit();
    }

    public void requestOrder(int id) {
        progressDoalog.setMessage("Processing your order....");
        progressDoalog.show();

        // initialize file here

        /*Create handle for the RetrofitInstance interface*/
        APIs service = RetrofitClientInstance.getRetrofitInstance().create(APIs.class);
        Call<C_OrderSerial> call = service.orderRequest("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"), id + "");
        call.enqueue(new Callback<C_OrderSerial>() {
            @Override
            public void onResponse(Call<C_OrderSerial> call, Response<C_OrderSerial> response) {
                progressDoalog.dismiss();


                if (response.body() != null) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {

                            shared.setString("cartsize", "");
                            shared.setString("order_id", response.body().getData().getOrderId() + "");
                            startActivity(new Intent(activity, C_MakePaymentAct.class));
                            finish();

                        } else {
                        }

                    }
                } else {

                }
            }


            @Override
            public void onFailure(Call<C_OrderSerial> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConstantClass.FromAddress = false;
    }

    @Override
    public void onQuantitySelected(int drodownPos, int pos, String v, DropDown dropDown) {
        if (drodownPos != 0 && drodownPos != 1 && drodownPos != 2) {
            initiatePopupWindow(dropDown, v, list, pos);
        } else {
            removeOrUpdateCartApi(list.get(pos).getCartID(), "update", v);
        }
    }

    public void initiatePopupWindow(DropDown dropDown, String quantity, List<C_CartModel> model, int pos) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            final View layout = inflater.inflate(R.layout.c_customer_spinner_pop_up,
                    (ViewGroup) this.findViewById(R.id.mainPop));
            // create a 300px width and 470px height PopupWindow
            final PopupWindow pw = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            // display the popup in the center
            pw.showAtLocation(layout, Gravity.TOP, 0, 0);
            final TextView apply = layout.findViewById(R.id.apply);
            final TextView cancel = layout.findViewById(R.id.cancel);
            final EditText spinnerEdit = layout.findViewById(R.id.spinerValue);


            apply.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    pw.dismiss();
                    String spinnerValue = spinnerEdit.getText().toString().trim();
                    if (spinnerValue.isEmpty()) {
                        Toast.makeText(activity, "Please enter valid value", Toast.LENGTH_SHORT).show();
                    } else {
                        dropDown.setText(spinnerValue);
                        removeOrUpdateCartApi(model.get(pos).getCartID(), "update", dropDown.getText().toString());
                    }
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pw.dismiss();
                }
            });
            pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    pw.dismiss();
                }
            });

        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }


}
