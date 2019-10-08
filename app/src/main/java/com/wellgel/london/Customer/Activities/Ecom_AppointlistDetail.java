package com.wellgel.london.Customer.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wellgel.london.APIs.Customer_APIs;
import com.wellgel.london.Customer.SerializeModelClasses.EcomAppoDetailSerial;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.PreferencesShared;
import com.wellgel.london.UtilClasses.Retrofit.RetrofitClientInstance;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ecom_AppointlistDetail extends AppCompatActivity {

    private Ecom_AppointlistDetail activity;
    private PreferencesShared shared;
    private TextView near_salon_name, salonBookingDate, salonBookingTime, near_salon_address, priceText;
    private ImageView c_booking_detail_navi;
    private ProgressDialog progressDoalog;
    private LinearLayout lay_pay, lay_accep_reject, layout_for_reschedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecom__appointlist_detail);

        activity = this;
        shared = new PreferencesShared(activity);
        near_salon_name = findViewById(R.id.near_salon_name);
        near_salon_address = findViewById(R.id.near_salon_address);
        salonBookingDate = findViewById(R.id.salonBookingDate);
        salonBookingTime = findViewById(R.id.salonBookingTime);
        c_booking_detail_navi = findViewById(R.id.c_booking_detail_navi);
        priceText = findViewById(R.id.priceText);
        lay_pay = findViewById(R.id.lay_pay);
        lay_accep_reject = findViewById(R.id.lay_accep_reject);
        layout_for_reschedule = findViewById(R.id.layout_for_reschedule);

        c_booking_detail_navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("appo_id_noti")) {
                appointDetailAPI(Integer.parseInt(extras.getString("appo_id_noti", "")));

            } else appointDetailAPI(getIntent().getIntExtra("appo_id", 0));

        }
    }


    public void appointDetailAPI(int appo_id) {
        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMessage("Getting Booking Detail....");
        progressDoalog.show();
        Customer_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Customer_APIs.class);
        Call<EcomAppoDetailSerial> call = service.appointmentDetail("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"), appo_id);
        call.enqueue(new Callback<EcomAppoDetailSerial>() {
            @Override
            public void onResponse(Call<EcomAppoDetailSerial> call, Response<EcomAppoDetailSerial> response) {

                progressDoalog.dismiss();

                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {


                            if (response.body().getData().size() > 0) {

                                if (shared.getString(ConstantClass.ROLL_PLAY).equalsIgnoreCase(ConstantClass.ROLL_CUSTOMER)) {
                                    near_salon_name.setText(response.body().getData().get(0).getSalonDetails().get(0).getName());
                                    near_salon_address.setText(response.body().getData().get(0).getSalonDetails().get(0).getAddress());
//                                    priceText.setText(response.body().getData().get(0).getPaymentMode());
                                    String currentString = response.body().getData().get(0).getRequestedDatetime();
                                    String[] separated = currentString.split(" ");
                                    if (response.body().getData().get(0).getAvailableDatetime() == response.body().getData().get(0).getRequestedDatetime()) {
                                        layout_for_reschedule.setVisibility(View.GONE);
                                    } else {
                                        layout_for_reschedule.setVisibility(View.VISIBLE);

                                    }

                                    salonBookingDate.setText(parseDateToddMMyyyy(separated[0]));

                                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                                    SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm aa");
                                    Date dt;
                                    try {
                                        dt = sdf.parse(separated[1]);
                                        salonBookingTime.setText(sdfs.format(dt));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else if (shared.getString(ConstantClass.ROLL_PLAY).equalsIgnoreCase(ConstantClass.ROLL_PROVIDER)) {
                                    near_salon_name.setText(response.body().getData().get(0).getCustomerDetails().get(0).getName());
                                    near_salon_address.setText(response.body().getData().get(0).getCustomerDetails().get(0).getAddress());
                                    String currentString = response.body().getData().get(0).getRequestedDatetime();
                                    String[] separated = currentString.split(" ");

                                    salonBookingDate.setText(parseDateToddMMyyyy(separated[0]));

                                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                                    SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm aa");
                                    Date dt;
                                    try {
                                        dt = sdf.parse(separated[1]);
                                        salonBookingTime.setText(sdfs.format(dt));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }
                        } else {

                            Toast.makeText(Ecom_AppointlistDetail.this, "dfjsdhskhdk", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<EcomAppoDetailSerial> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "EEEE dd MMMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
