package com.wellgel.london.Customer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.wellgel.london.APIs.Customer_APIs;
import com.wellgel.london.APIs.Provider_APIs;
import com.wellgel.london.Customer.Adapters.C_Booked_OrderAdapter;
import com.wellgel.london.Customer.SerializeModelClasses.C_AppointmentList;
import com.wellgel.london.Provider.Activities.P_AcceptRejectAct;
import com.wellgel.london.Provider.ModelSerialized.P_AppointmentListSerial;
import com.wellgel.london.Provider.ProviderAdapters.P_Booked_OrderAdapter;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.PreferencesShared;
import com.wellgel.london.UtilClasses.Retrofit.RetrofitClientInstance;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class C_AllAppointListAct extends AppCompatActivity implements C_Booked_OrderAdapter.OnBookingID, P_Booked_OrderAdapter.OnBookingProviderID {
    private C_Booked_OrderAdapter bookedAdpter;

    private PreferencesShared shared;
    private C_AllAppointListAct activity;
    private RecyclerView c_dash_bookedList_recycler;
    private List<P_AppointmentListSerial.Datum> bookedListProvider = new ArrayList<>();
    private P_Booked_OrderAdapter bookedAdpterProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__all_appoint_list);
        activity = this;
        shared = new PreferencesShared(activity);
        c_dash_bookedList_recycler = findViewById(R.id.c_dash_bookedList_recycler);

        if (shared.getString(ConstantClass.ROLL_PLAY).equalsIgnoreCase(ConstantClass.ROLL_CUSTOMER))
            appointMentList();
        else if (shared.getString(ConstantClass.ROLL_PLAY).equalsIgnoreCase(ConstantClass.ROLL_PROVIDER))
            appointMentListProvider();
    }

    public void appointMentList() {

        Customer_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Customer_APIs.class);
        Call<C_AppointmentList> call = service.appointmentList("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"));
        call.enqueue(new Callback<C_AppointmentList>() {
            @Override
            public void onResponse(Call<C_AppointmentList> call, Response<C_AppointmentList> response) {


                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            bookedAdpter = new C_Booked_OrderAdapter(activity, response.body().getData(), activity);
                            c_dash_bookedList_recycler.setAdapter(bookedAdpter);
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


                            bookedAdpterProvider = new P_Booked_OrderAdapter(activity, response.body().getData(), activity);
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
