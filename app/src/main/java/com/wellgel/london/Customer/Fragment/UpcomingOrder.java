package com.wellgel.london.Customer.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wellgel.london.APIs.Customer_APIs;
import com.wellgel.london.APIs.Provider_APIs;
import com.wellgel.london.Customer.Activities.Ecom_AppointlistDetail;
import com.wellgel.london.Customer.Adapters.C_Booked_OrderAdapter;
import com.wellgel.london.Customer.C_BookedOrderModel;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingOrder extends Fragment {

    private RecyclerView upcoming_recycler;
    private Context context;
    private List<C_AppointmentList.Datum> bookedList = new ArrayList<>();
    private List<P_AppointmentListSerial.Datum> bookedListProvider = new ArrayList<>();
    private C_Booked_OrderAdapter bookedAdpter;
    private P_Booked_OrderAdapter bookedAdpterProvider;
    private PreferencesShared shared;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public UpcomingOrder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming_order, container, false);

        upcoming_recycler = view.findViewById(R.id.upcoming_recycler);

        shared = new PreferencesShared(context);
//        bookedAdpter = new C_Booked_OrderAdapter(getActivity(), bookedList);
//        upcoming_recycler.setAdapter(bookedAdpter);
        if (shared.getString(ConstantClass.ROLL_PLAY).equalsIgnoreCase(ConstantClass.ROLL_CUSTOMER))
            appointMentList();
        else if (shared.getString(ConstantClass.ROLL_PLAY).equalsIgnoreCase(ConstantClass.ROLL_PROVIDER))
            appointMentListProvider();

        return view;
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


                            bookedListProvider.clear();
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                String status = response.body().getData().get(i).getStatus();

                                if (status.equalsIgnoreCase("requested") || status.equalsIgnoreCase("open")) {

                                    bookedListProvider.add(response.body().getData().get(i));


                                }

                            }

                            bookedAdpterProvider = new P_Booked_OrderAdapter(context, bookedListProvider, new P_Booked_OrderAdapter.OnBookingProviderID() {
                                @Override
                                public void getBookingProviderID(int id) {
                                    Intent intent = new Intent(context, P_AcceptRejectAct.class);
                                    intent.putExtra("appo_id", id);
                                    startActivity(intent);
                                }
                            });

                            upcoming_recycler.setAdapter(bookedAdpterProvider);
                        } else {
                        }

                    }
                } else {


                    try {
                        JSONObject jObjError = null;
                        if (response.errorBody() != null) {
                            jObjError = new JSONObject(response.errorBody().string());
                            String errorMessage = jObjError.getJSONObject("error").getJSONObject("error_message").getJSONArray("message").getString(0);

                            Toast.makeText(context, "" + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                    }
                }
            }


            @Override
            public void onFailure(Call<P_AppointmentListSerial> call, Throwable t) {
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
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

                            bookedList.clear();
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                String status = response.body().getData().get(i).getStatus();

                                if (status.equalsIgnoreCase("requested")) {

                                    bookedList.add(response.body().getData().get(i));


                                }

                            }


                            bookedAdpter = new C_Booked_OrderAdapter(context, bookedList, new C_Booked_OrderAdapter.OnBookingID() {
                                @Override
                                public void getBookinID(int id) {
                                    Intent intent = new Intent(context, Ecom_AppointlistDetail.class);
                                    intent.putExtra("appo_id", id);
                                    startActivity(intent);

                                }
                            });
                            upcoming_recycler.setAdapter(bookedAdpter);
                        } else {
                        }

                    }
                } else {


                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String errorMessage = jObjError.getJSONObject("error").getJSONObject("error_message").getJSONArray("message").getString(0);

                        Toast.makeText(context, "" + errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }
                }
            }


            @Override
            public void onFailure(Call<C_AppointmentList> call, Throwable t) {
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
