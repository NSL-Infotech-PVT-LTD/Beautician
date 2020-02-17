package com.wellgel.london.Customer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.wellgel.london.APIs.Customer_APIs;
import com.wellgel.london.Customer.Adapters.C_MyOrdersAdapter;
import com.wellgel.london.Customer.MyOrdersModel;
import com.wellgel.london.Customer.SerializeModelClasses.C_MyOrdersSerial;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.PreferencesShared;
import com.wellgel.london.UtilClasses.Retrofit.RetrofitClientInstance;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class C_MyOrdersActivity extends AppCompatActivity {

    private ProgressDialog progressDoalog;
    private C_MyOrdersActivity activity;
    private PreferencesShared shared;
    private RecyclerView myOrderRecycler;
    private MyOrdersModel myOrdersModel;
    private List<MyOrdersModel> list = new ArrayList<>();
    private C_MyOrdersAdapter adapter;
    private ImageView c_order_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__my_orders);
        activity = this;

        shared = new PreferencesShared(activity);

        myOrderRecycler = findViewById(R.id.ordersRecycler);
        c_order_back = findViewById(R.id.c_order_back);
        myOrderRecycler.setLayoutManager(new LinearLayoutManager(this));

        c_order_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        orders();
    }

    public void orders() {
        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage(getString(R.string.checkOrder));
        progressDoalog.show();

        // initialize file here

        /*Create handle for the RetrofitInstance interface*/
        Customer_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Customer_APIs.class);
        Call<C_MyOrdersSerial> call = service.myOrders("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"));
        call.enqueue(new Callback<C_MyOrdersSerial>() {
            @Override
            public void onResponse(Call<C_MyOrdersSerial> call, Response<C_MyOrdersSerial> response) {
                progressDoalog.dismiss();


                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {

                            if (response.body().getData().size() > 0) {

                                adapter = new C_MyOrdersAdapter(activity, response.body().getData());
                                myOrderRecycler.setAdapter(adapter);

//                                for (int j = 0; j < response.body().getData().size(); j++) {
//
//                                    for (int i = 0; i < response.body().getData().get(j).getOrderdetails().size(); i++) {
//                                        myOrdersModel = new MyOrdersModel();
//                                        myOrdersModel.setMyOrderstatus(response.body().getData().get(j).getStatus());
//                                        myOrdersModel.setMyOrderTotalAmount(response.body().getData().get(j).getTotalPaid());
//                                        myOrdersModel.setMyProductname(response.body().getData().get(j).getOrderdetails().get(i).getProductId().getName());
//                                        myOrdersModel.setInvoiceNum("Invoice No :  " + response.body().getData().get(j).getId() + "");
//                                        myOrdersModel.setMyProductQuan(response.body().getData().get(j).getOrderdetails().get(i).getQuantity() + "");
//                                        myOrdersModel.setMyProductPrice(response.body().getData().get(j).getOrderdetails().get(i).getProductId().getPrice());
//
//                                    }
//
//                                    for (int i = 0; i < response.body().getData().get(j).getAddresses().size(); i++) {
//                                        myOrdersModel.setMyProductAddress(response.body().getData().get(j).getAddresses().get(i).getName() + "\n" +
//                                                response.body().getData().get(j).getAddresses().get(i).getCity() + "," + response.body().getData().get(j).getAddresses().get(i).getCounty() + "\n" +
//                                                response.body().getData().get(j).getAddresses().get(i).getCountry()
//                                        );
//                                    }
//                                    list.add(myOrdersModel);
//                                }
//                                List<MyOrdersModel> lists = list;
//                                adapter = new C_MyOrdersAdapter(activity, lists);
//                                myOrderRecycler.setAdapter(adapter);
                            }
                        }

                    }
                } else {

                    try {
                        JSONObject jObjError = null;
                        if (response.errorBody() != null) {
                            jObjError = new JSONObject(response.errorBody().string());
                            String errorMessage = jObjError.getJSONObject("error").getJSONObject("error_message").getJSONArray("message").getString(0);

                            Toast.makeText(activity, "" + errorMessage, Toast.LENGTH_SHORT).show();    }

                    } catch (Exception e) {

                    }
                }
            }


            @Override
            public void onFailure(Call<C_MyOrdersSerial> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
