package com.wellgel.london.Customer.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.wellgel.london.Customer.APIs;
import com.wellgel.london.Customer.Adapters.C_Product_Adapte;
import com.wellgel.london.Customer.C_ConstantClass;
import com.wellgel.london.Customer.C_Product_model;
import com.wellgel.london.Customer.EndlessRecyclerOnScrollListener;
import com.wellgel.london.Customer.PaginationListener;
import com.wellgel.london.Customer.SerializeModelClasses.C_ProductsSerial;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.PreferencesShared;
import com.wellgel.london.UtilClasses.Retrofit.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class C_ViewAllProductsAct extends AppCompatActivity implements C_Product_Adapte.onItemClick {

    private RecyclerView c_dash_product_recycler;
    private List<C_Product_model> productList = new ArrayList<>();
    private ProgressDialog progressDoalog;
    C_ViewAllProductsAct activity;
    private PreferencesShared shared;
    private C_Product_model product_model;
    private C_Product_Adapte product_adapte;
    private ImageView c_view_back;
    private TextView textHeader, cart_count;
    private ImageView cart_detail, filterByICon, orderByICon;
    private TextView no_search_data;
    private PopupWindow popupWindow;
    int page = 1;
    private int mLoadedItems = 0;
    private ProgressBar itemProgressBar;
    private LinearLayout layoutBottomSheet;
    private BottomSheetBehavior sheetBehavior;
    private TextView sort_name_asc, sort_name_dsc, sort_latest, sort_price_high, sort_price_low;
    private int sort_count = 3;

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

        setContentView(R.layout.activity_c__view_all_products);
        activity = this;
        shared = new PreferencesShared(activity);

        c_dash_product_recycler = findViewById(R.id.viewAllRecycler);
        c_view_back = findViewById(R.id.c_view_back);
        cart_count = findViewById(R.id.cart_count);
        textHeader = findViewById(R.id.textHeader);
        filterByICon = findViewById(R.id.filterByICon);
        orderByICon = findViewById(R.id.orderByICon);
        no_search_data = findViewById(R.id.no_search_data);
        itemProgressBar = findViewById(R.id.item_progress_bar);
        layoutBottomSheet = findViewById(R.id.bottomsheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        bottomSheetBehavior();
        filterByICon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetUpDown();
            }
        });
        layoutBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetUpDown();
            }
        });

        c_view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (ConstantClass.isProduct) {
            productFun();
        } else {
            chromeFun();
        }

        cart_detail = findViewById(R.id.cartValue);

        cart_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, C_CartDetailAct.class));
            }
        });
    }

    private void productFun() {

        textHeader.setText(getString(R.string.wellgelExpres));
        GridLayoutManager layoutManager = new GridLayoutManager(activity, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        c_dash_product_recycler.setLayoutManager(layoutManager);

        c_dash_product_recycler.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {

//                productsAPI("created_at", "asc", "", page);
            }
        });
        if (getIntent() != null) {

            String search = getIntent().getStringExtra("search");
            if (search != null) {
                if (search.equalsIgnoreCase("")) {
                    productsAPI("latest", "", page);

                } else {
                    productsAPI("latest", getIntent().getStringExtra("search"), page);

                }
            } else {
                productsAPI("latest", getIntent().getStringExtra("search"), page);
            }

        }
    }


    private void chromeFun() {
        textHeader.setText(getString(R.string.chromeKIt));
        c_dash_product_recycler.setLayoutManager(new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });


        for (int i = 0; i < 5; i++) {
            product_model = new C_Product_model();
            product_model.setProddductPrice("€12.00");
            product_model.setProductName("chrome powder");
            product_model.setProductImage(C_ConstantClass.IMAGE_BASE_URL + "products/5d6f8fe9d9602.webp");

            productList.add(product_model);
        }

        product_adapte = new C_Product_Adapte(activity, productList, new C_Product_Adapte.onItemClick() {
            @Override
            public void onItemClick(int id) {

            }
        });
        c_dash_product_recycler.setAdapter(product_adapte);

    }

    private void bottomSheetBehavior() {
        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:

                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }


    //     Products from server APi
    public void productsAPI(String orderBy, String search, int pages) {
        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        // initialize file here

        /*Create handle for the RetrofitInstance interface*/
        APIs service = RetrofitClientInstance.getRetrofitInstance().create(APIs.class);
        Call<C_ProductsSerial> call = service.poducts("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"), orderBy, search + "", "10", pages + "");
        call.enqueue(new Callback<C_ProductsSerial>() {
            @Override
            public void onResponse(Call<C_ProductsSerial> call, Response<C_ProductsSerial> response) {
                progressDoalog.dismiss();

                if (popupWindow != null)
                    popupWindow.dismiss();

                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {

                            productList.clear();

                            if (response.body().getData().getData().size() > 0) {
                                no_search_data.setVisibility(View.GONE);

                                for (int i = 0; i < response.body().getData().getData().size(); i++) {

                                    product_model = new C_Product_model();
                                    product_model.setProddductPrice(" £" + response.body().getData().getData().get(i).getPrice() + "");
                                    product_model.setProductName(response.body().getData().getData().get(i).getName());
                                    product_model.setProductImage(C_ConstantClass.IMAGE_BASE_URL + "products/" + response.body().getData().getData().get(i).getImage());
                                    product_model.setProductID(response.body().getData().getData().get(i).getId());

                                    productList.add(product_model);
                                }

                                product_adapte = new C_Product_Adapte(activity, productList, activity);
                                c_dash_product_recycler.setAdapter(product_adapte);

                            } else {
                                no_search_data.setVisibility(View.VISIBLE);

                                orderByICon.setVisibility(View.GONE);
                                filterByICon.setVisibility(View.GONE);
                            }
                        } else {
                            orderByICon.setVisibility(View.GONE);
                            filterByICon.setVisibility(View.GONE);
                            no_search_data.setVisibility(View.VISIBLE);
                        }

                    }
                } else {
                    no_search_data.setVisibility(View.VISIBLE);
                    orderByICon.setVisibility(View.GONE);
                    filterByICon.setVisibility(View.GONE);
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
    public void onItemClick(int id) {

        Intent intent = new Intent(activity, C_ProductsDetailAct.class);
        intent.putExtra("product_id", id);
        startActivity(intent);
    }

    private void bottomSheetUpDown() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        bottomOnClickSort();
    }

    private void bottomOnClickSort() {

        sort_latest = findViewById(R.id.sort_latest);
        sort_name_asc = findViewById(R.id.sort_name_asc);
        sort_name_dsc = findViewById(R.id.sort_name_dsc);
        sort_price_high = findViewById(R.id.sort_price_high);
        sort_price_low = findViewById(R.id.sort_price_low);

        if (sort_count == 1) {
            sort_price_high.setTypeface(null, Typeface.BOLD);
            sort_price_low.setTypeface(null, Typeface.NORMAL);
            sort_name_dsc.setTypeface(null, Typeface.NORMAL);
            sort_name_asc.setTypeface(null, Typeface.NORMAL);
            sort_latest.setTypeface(null, Typeface.NORMAL);
        } else if (sort_count == 2) {
            sort_price_high.setTypeface(null, Typeface.NORMAL);
            sort_price_low.setTypeface(null, Typeface.BOLD);
            sort_name_dsc.setTypeface(null, Typeface.NORMAL);
            sort_name_asc.setTypeface(null, Typeface.NORMAL);
            sort_latest.setTypeface(null, Typeface.NORMAL);
        } else if (sort_count == 3) {
            sort_price_high.setTypeface(null, Typeface.NORMAL);
            sort_price_low.setTypeface(null, Typeface.NORMAL);
            sort_name_dsc.setTypeface(null, Typeface.NORMAL);
            sort_name_asc.setTypeface(null, Typeface.NORMAL);
            sort_latest.setTypeface(null, Typeface.BOLD);
        } else if (sort_count == 4) {
            sort_price_high.setTypeface(null, Typeface.NORMAL);
            sort_price_low.setTypeface(null, Typeface.NORMAL);
            sort_name_dsc.setTypeface(null, Typeface.NORMAL);
            sort_name_asc.setTypeface(null, Typeface.BOLD);
            sort_latest.setTypeface(null, Typeface.NORMAL);
        } else if (sort_count == 5) {
            sort_price_high.setTypeface(null, Typeface.NORMAL);
            sort_price_low.setTypeface(null, Typeface.NORMAL);
            sort_name_dsc.setTypeface(null, Typeface.BOLD);
            sort_name_asc.setTypeface(null, Typeface.NORMAL);
            sort_latest.setTypeface(null, Typeface.NORMAL);
        }
        sort_price_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort_price_high.setTypeface(null, Typeface.BOLD);
                bottomSheetUpDown();
                sort_count = 1;
                productsAPI("price_high", "", page);

            }
        });
        sort_price_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort_price_low.setTypeface(null, Typeface.BOLD);
                bottomSheetUpDown();
                sort_count = 2;
                productsAPI("price_low", "", page);

            }
        });
        sort_latest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort_latest.setTypeface(null, Typeface.BOLD);
                bottomSheetUpDown();
                sort_count = 3;
                productsAPI("latest", "", page);

            }
        });
        sort_name_asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort_name_asc.setTypeface(null, Typeface.BOLD);
                bottomSheetUpDown();
                sort_count = 4;
                productsAPI("name_a2z", "", page);

            }
        });
        sort_name_dsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort_name_dsc.setTypeface(null, Typeface.BOLD);
                bottomSheetUpDown();
                sort_count = 5;
                productsAPI("name_z2a", "", page);

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
