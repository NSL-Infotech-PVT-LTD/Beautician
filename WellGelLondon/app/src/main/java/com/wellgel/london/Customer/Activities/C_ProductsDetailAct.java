package com.wellgel.london.Customer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.ViewStructure;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wellgel.london.Customer.APIs;
import com.wellgel.london.Customer.Adapters.MyCustomPagerAdapter;
import com.wellgel.london.Customer.C_CartModel;
import com.wellgel.london.Customer.C_ConstantClass;
import com.wellgel.london.Customer.SerializeModelClasses.C_CartSerailized;
import com.wellgel.london.Customer.SerializeModelClasses.ProductDetailSerial;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.PreferencesShared;
import com.wellgel.london.UtilClasses.Retrofit.RetrofitClientInstance;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class C_ProductsDetailAct extends AppCompatActivity {

    private TextView c_direction, c_description, c_shippingAddress, addtocart;
    private TextView c_product_name, c_product_price;
    private C_ProductsDetailAct activity;
    private ImageView c_ship_addr_iv, c_description_iv, c_use_direction_iv, backPress;
    private ProgressDialog progressDoalog;
    private PreferencesShared shared;
    private boolean diretion = true;
    private boolean description = true;
    private boolean shipping = true;
    private ImageView c_peoduct_image, cart_detail;
    ViewPager viewPager;
    List<String> images = new ArrayList<>();
    MyCustomPagerAdapter myCustomPagerAdapter;

    private int currentPage = 0;
    private int NUM_PAGES = 0;
    private String productID;
    private TextView cart_count;
    public int isAdded = 0;


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
        setContentView(R.layout.activity_c__products_detail);


        init();

    }

    private void init() {

        activity = this;
        shared = new PreferencesShared(activity);
        backPress = findViewById(R.id.c_product_back);
        c_direction = findViewById(R.id.c_direction);
        c_description = findViewById(R.id.c_description);
        addtocart = findViewById(R.id.addtocart);
        cart_detail = findViewById(R.id.cartValue);
        cart_count = findViewById(R.id.cart_count);
        c_shippingAddress = findViewById(R.id.c_shippingAddress);
        c_product_name = findViewById(R.id.c_product_name);
        c_product_price = findViewById(R.id.c_product_price);
        c_peoduct_image = findViewById(R.id.c_peoduct_image);

        c_ship_addr_iv = findViewById(R.id.c_ship_addr_iv);
        c_description_iv = findViewById(R.id.c_description_iv);
        c_use_direction_iv = findViewById(R.id.c_use_direction_iv);
        viewPager = findViewById(R.id.pager);


        onCLickListener();
    }

    private void onCLickListener() {

        c_use_direction_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (diretion) {
                    diretion = false;
                    c_direction.setVisibility(View.VISIBLE);
                    c_use_direction_iv.setImageResource(R.drawable.ic_remove_black_24dp);
                } else {
                    diretion = true;
                    c_direction.setVisibility(View.GONE);

                    c_use_direction_iv.setImageResource(R.drawable.ic_add_black_24dp);
                }
            }
        });

        c_description_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (description) {
                    description = false;
                    c_description.setVisibility(View.VISIBLE);

                    c_description_iv.setImageResource(R.drawable.ic_remove_black_24dp);
                } else {
                    description = true;
                    c_description.setVisibility(View.GONE);
                    c_description_iv.setImageResource(R.drawable.ic_add_black_24dp);
                }
            }
        });

        c_ship_addr_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shipping) {
                    shipping = false;
                    c_shippingAddress.setVisibility(View.VISIBLE);
                    c_ship_addr_iv.setImageResource(R.drawable.ic_remove_black_24dp);
                } else {
                    shipping = true;
                    c_shippingAddress.setVisibility(View.GONE);
                    c_ship_addr_iv.setImageResource(R.drawable.ic_add_black_24dp);
                }
            }
        });

        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        cart_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, C_CartDetailAct.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        productDetailAPI();
    }

    //     Products from server APi
    public void productDetailAPI() {
        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        // initialize file here

        /*Create handle for the RetrofitInstance interface*/
        APIs service = RetrofitClientInstance.getRetrofitInstance().create(APIs.class);
        Call<ProductDetailSerial> call = service.poductDetail("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"), getIntent().getIntExtra("product_id", 0) + "");
        call.enqueue(new Callback<ProductDetailSerial>() {
            @Override
            public void onResponse(Call<ProductDetailSerial> call, final Response<ProductDetailSerial> response) {
                progressDoalog.dismiss();


                if (response.body() != null) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                            images.clear();

                            images.add(C_ConstantClass.IMAGE_BASE_URL + "products/" + response.body().getData().getImage());

                            for (int i = 0; i < response.body().getData().getImages().size(); i++) {
                                images.add(C_ConstantClass.IMAGE_BASE_URL + "products/" + response.body().getData().getImages().get(i));
                            }
                            Picasso.with(activity).load(C_ConstantClass.IMAGE_BASE_URL + "products/" + response.body().getData().getImage()).into(c_peoduct_image);
                            c_product_price.setText(" £" + response.body().getData().getPrice() + ".00");
                            c_product_name.setText(response.body().getData().getName());
                            productID = response.body().getData().getId() + "";
                            isAdded = response.body().getData().getIsAddedToCartQuantity();
                            c_direction.setText(Html.fromHtml(response.body().getData().getDirectionsForUse()));
                            c_description.setText(Html.fromHtml(response.body().getData().getDescription()));
                            c_shippingAddress.setText(Html.fromHtml(response.body().getData().getShortDescription()));
                            myCustomPagerAdapter = new MyCustomPagerAdapter(activity, images);
                            viewPager.setAdapter(myCustomPagerAdapter);
                            CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
                            indicator.setViewPager(viewPager);
                            addtocart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (isAdded != 0) {
                                        updateCart(getIntent().getIntExtra("product_id", 0) + "", "update", isAdded + 1 + "");
                                    } else {
                                        addToCartApi();
                                    }
                                }
                            });

                        } else {
                        }

                    }
                } else {


                }
            }


            @Override
            public void onFailure(Call<ProductDetailSerial> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void updateCart(String productID, String cartOption, String cartQantity) {
        // initialize file here

        /*Create handle for the RetrofitInstance interface*/
        APIs service = RetrofitClientInstance.getRetrofitInstance().create(APIs.class);
        Call<C_CartSerailized> call = service.cart("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"), productID, cartQantity, cartOption);
        call.enqueue(new Callback<C_CartSerailized>() {
            @Override
            public void onResponse(Call<C_CartSerailized> call, Response<C_CartSerailized> response) {


                if (response.body() != null) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {


                            Intent intent = new Intent(activity, C_CartDetailAct.class);
                            startActivity(intent);
                        }
                    }
                }

            }


            @Override
            public void onFailure(Call<C_CartSerailized> call, Throwable t) {
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void addToCartApi() {
        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        // initialize file here

        /*Create handle for the RetrofitInstance interface*/
        APIs service = RetrofitClientInstance.getRetrofitInstance().create(APIs.class);
        Call<C_CartSerailized> call = service.cart("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"), productID, "1", "create");
        call.enqueue(new Callback<C_CartSerailized>() {
            @Override
            public void onResponse(Call<C_CartSerailized> call, Response<C_CartSerailized> response) {
                progressDoalog.dismiss();


                if (response.body() != null) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {

                            Intent intent = new Intent(activity, C_CartDetailAct.class);
                            startActivity(intent);


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
            public void onFailure(Call<C_CartSerailized> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
