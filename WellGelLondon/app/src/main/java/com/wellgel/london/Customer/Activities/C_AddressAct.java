package com.wellgel.london.Customer.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wellgel.london.Customer.APIs;
import com.wellgel.london.Customer.Adapters.C_AddressAdapter;
import com.wellgel.london.Customer.C_AddressModel;
import com.wellgel.london.Customer.SerializeModelClasses.C_CreateAddressSerialize;
import com.wellgel.london.Customer.SerializeModelClasses.C_DeleteAddresSerial;
import com.wellgel.london.Customer.SerializeModelClasses.C_GetAddressListSerial;
import com.wellgel.london.Customer.SerializeModelClasses.C_OrderSerial;
import com.wellgel.london.Customer.SerializeModelClasses.C_UpdateAdressSerial;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.PreferencesShared;
import com.wellgel.london.UtilClasses.Retrofit.RetrofitClientInstance;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class C_AddressAct extends AppCompatActivity implements C_AddressAdapter.EditAddress, C_AddressAdapter.RemoveAddress {

    private RecyclerView address_recycler;
    private C_AddressAct activity;
    private PreferencesShared shared;
    private List<C_AddressModel> list = new ArrayList<>();
    private C_AddressModel model;
    private C_AddressAdapter addressAdapter;
    private CardView addNewAddress;
    private AlertDialog dialogMultiOrder;
    private ImageView c_cart_back;
    private ProgressDialog progressDoalog;
    private TextView continueShipping;
    private String adress = "", street = "", zip = "", county = "", country = "", city = "";
    private int address_id = 0;
    private String getStringValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__address);

        activity = this;
        shared = new PreferencesShared(activity);
        address_recycler = findViewById(R.id.address_recycler);
        c_cart_back = findViewById(R.id.c_cart_back);
        addNewAddress = findViewById(R.id.addNewAddress);
        continueShipping = findViewById(R.id.continueShipping);
        progressDoalog = new ProgressDialog(activity);

        c_cart_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                displayDialog(0);
            }
        });
        getAdress();
        address_recycler.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });


        continueShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (address_id != 0) {
                    ConstantClass.FromAddress = true;

                    Intent intent = new Intent(activity, C_CartDetailAct.class);
                    intent.putExtra("address_id", address_id);
                    startActivity(intent);
                } else {
                    Toast.makeText(C_AddressAct.this, "Please select Address", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void getAdress() {
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        // initialize file here

        /*Create handle for the RetrofitInstance interface*/
        APIs service = RetrofitClientInstance.getRetrofitInstance().create(APIs.class);
        Call<C_GetAddressListSerial> call = service.getAddressList("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"));
        call.enqueue(new Callback<C_GetAddressListSerial>() {
            @Override
            public void onResponse(Call<C_GetAddressListSerial> call, Response<C_GetAddressListSerial> response) {
                progressDoalog.dismiss();

                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            for (int i = 0; i < response.body().getData().size(); i++) {

                                adress = response.body().getData().get(i).getName();
                                street = response.body().getData().get(i).getStreetAddress();
                                city = response.body().getData().get(i).getCity();
                                country = response.body().getData().get(i).getCountry();
                                county = response.body().getData().get(i).getCounty();
                                zip = response.body().getData().get(i).getZip() + "";
                                model = new C_AddressModel();
                                model.setAddressID(response.body().getData().get(i).getId());
                                model.setAddresssOption("Edit");
                                model.setAddressType(response.body().getData().get(i).getName());
//                                model.setAddressUser(shared.getString(ConstantClass.USER_NAME));

                                model.setFullAddress(response.body().getData().get(i).getStreetAddress() + "\n" + response.body().getData().get(i).getCity() + ", " + response.body().getData().get(i).getCounty() + ", " + response.body().getData().get(i).getCountry());
                                list.add(model);

                            }
                            addressAdapter = new C_AddressAdapter(activity, list, new C_AddressAdapter.onItemClick() {
                                @Override
                                public void onItemClick(int id) {

                                    address_id = id;
                                }
                            }, activity, activity);


                            address_recycler.setAdapter(addressAdapter);

                        } else {
                        }

                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String errorMessage = jObjError.getJSONObject("error").getJSONObject("error_message").getJSONArray("message").getString(0);

                        Toast.makeText(C_AddressAct.this, "" + errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }
            }


            @Override
            public void onFailure(Call<C_GetAddressListSerial> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void deleteAddress(int id) {
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        // initialize file here

        /*Create handle for the RetrofitInstance interface*/
        APIs service = RetrofitClientInstance.getRetrofitInstance().create(APIs.class);
        Call<C_DeleteAddresSerial> call = service.deleteAddress("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"), String.valueOf(id));
        call.enqueue(new Callback<C_DeleteAddresSerial>() {
            @Override
            public void onResponse(Call<C_DeleteAddresSerial> call, Response<C_DeleteAddresSerial> response) {
                progressDoalog.dismiss();


                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            startActivity(new Intent(activity, C_AddressAct.class));

                            Toast.makeText(C_AddressAct.this, "" + response.body().getData().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {

                }
            }


            @Override
            public void onFailure(Call<C_DeleteAddresSerial> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void displayDialog(int id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View content = inflater.inflate(R.layout.c_custom_add_address_pop, null);
        builder.setView(content);

        dialogMultiOrder = builder.create();

        TextView home_tv, work_tv, other_Tv;
        home_tv = content.findViewById(R.id.homeBtn);
        work_tv = content.findViewById(R.id.workBtn);
        other_Tv = content.findViewById(R.id.otherBtn);

        getStringValue = home_tv.getText().toString();


        final EditText c_address_name = content.findViewById(R.id.c_address_name);
        final EditText c_street_address = content.findViewById(R.id.c_street_address);
        final EditText c_city = content.findViewById(R.id.c_city);
        final EditText c_county = content.findViewById(R.id.c_county);
        final EditText c_zip = content.findViewById(R.id.c_zip);
        final EditText c_country = content.findViewById(R.id.c_country);
        final Button c_close = content.findViewById(R.id.c_close);
        final Button c_add_new_address = content.findViewById(R.id.c_add_new_address);


        home_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c_address_name.setVisibility(View.GONE);
                getStringValue = home_tv.getText().toString();
                home_tv.setBackground(getResources().getDrawable(R.drawable.capsule_edittxt_shape_grey));
                home_tv.setTextColor(getResources().getColor(R.color.white));
                work_tv.setBackground(getResources().getDrawable(R.drawable.capsule_edittxt_shape));
                work_tv.setTextColor(getResources().getColor(R.color.black));
                other_Tv.setBackground(getResources().getDrawable(R.drawable.capsule_edittxt_shape));
                other_Tv.setTextColor(getResources().getColor(R.color.black));
            }
        });

        work_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c_address_name.setVisibility(View.GONE);
                getStringValue = work_tv.getText().toString();
                work_tv.setBackground(getResources().getDrawable(R.drawable.capsule_edittxt_shape_grey));
                work_tv.setTextColor(getResources().getColor(R.color.white));
                home_tv.setBackground(getResources().getDrawable(R.drawable.capsule_edittxt_shape));
                home_tv.setTextColor(getResources().getColor(R.color.black));
                other_Tv.setBackground(getResources().getDrawable(R.drawable.capsule_edittxt_shape));
                other_Tv.setTextColor(getResources().getColor(R.color.black));
            }
        });


        other_Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c_address_name.setVisibility(View.VISIBLE);
                getStringValue = other_Tv.getText().toString();
                other_Tv.setBackground(getResources().getDrawable(R.drawable.capsule_edittxt_shape_grey));
                other_Tv.setTextColor(getResources().getColor(R.color.white));
                work_tv.setBackground(getResources().getDrawable(R.drawable.capsule_edittxt_shape));
                work_tv.setTextColor(getResources().getColor(R.color.black));
                home_tv.setBackground(getResources().getDrawable(R.drawable.capsule_edittxt_shape));
                home_tv.setTextColor(getResources().getColor(R.color.black));
            }
        });

        if (id != 0) {
            c_address_name.setText(adress);
            c_street_address.setText(street);
            c_city.setText(city);
            c_county.setText(county);
            c_country.setText(country);
            c_zip.setText(zip);
        }
        if (id == 0) {
            c_add_new_address.setText("Create");
        } else {
            c_add_new_address.setText("Update");
        }

        c_add_new_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                adress = c_address_name.getText().toString().trim();
                street = c_street_address.getText().toString().trim();
                city = c_city.getText().toString().trim();
                county = c_county.getText().toString().trim();
                country = c_country.getText().toString().trim();
                zip = c_zip.getText().toString().trim();

//                if (getStringValue.equalsIgnoreCase(getString(R.string.other))) {
//                    if (adress.isEmpty()) {
//                        c_address_name.setError(getString(R.string.invalidField));
//                    }
//                } else
                if (street.isEmpty()) {
                    c_street_address.setError(getString(R.string.invalidField));

                } else if (city.isEmpty()) {
                    c_city.setError(getString(R.string.invalidField));

                } else if (county.isEmpty()) {
                    c_county.setError(getString(R.string.invalidField));

                } else if (country.isEmpty()) {
                    c_country.setError(getString(R.string.invalidField));

                } else if (zip.isEmpty()) {
                    c_zip.setError(getString(R.string.invalidField));

                } else {
                    if (id == 0) {
                        createAddress();
                    } else {
                        updateAddres(id, adress, street, county, zip, country, city);
                    }


                }

            }
        });
        c_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMultiOrder.dismiss();
            }
        });
        dialogMultiOrder.show();
        dialogMultiOrder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    public void createAddress() {
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        // initialize file here

        /*Create handle for the RetrofitInstance interface*/
        APIs service = RetrofitClientInstance.getRetrofitInstance().create(APIs.class);
        Call<C_CreateAddressSerialize> call = service.createAddress("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"), getStringValue, street, city, county, zip, country);


        call.enqueue(new Callback<C_CreateAddressSerialize>() {
            @Override
            public void onResponse(Call<C_CreateAddressSerialize> call, Response<C_CreateAddressSerialize> response) {
                progressDoalog.dismiss();


                if (response.body() != null) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {


                            dialogMultiOrder.dismiss();


                            startActivity(new Intent(activity, C_AddressAct.class));
                            finish();


                        } else {
                        }

                    }
                } else {

                }
            }


            @Override
            public void onFailure(Call<C_CreateAddressSerialize> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void updateAddres(int id, String name, String street, String county, String zip, String country, String city) {
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        // initialize file here

        /*Create handle for the RetrofitInstance interface*/
        APIs service = RetrofitClientInstance.getRetrofitInstance().create(APIs.class);
        Call<C_UpdateAdressSerial> call = service.updateAddress("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"), String.valueOf(id), name, street, city, county, zip, country);
        call.enqueue(new Callback<C_UpdateAdressSerial>() {
            @Override
            public void onResponse(Call<C_UpdateAdressSerial> call, Response<C_UpdateAdressSerial> response) {
                progressDoalog.dismiss();


                if (response.body() != null) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                            dialogMultiOrder.dismiss();

                            startActivity(new Intent(activity, C_AddressAct.class));
                            finish();

                        } else {
                        }

                    }
                } else {

                }
            }


            @Override
            public void onFailure(Call<C_UpdateAdressSerial> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onEditClick(int id) {

        displayDialog(id);


    }

    @Override
    public void onRemove(int id) {

        deleteAddress(id);
    }
}
