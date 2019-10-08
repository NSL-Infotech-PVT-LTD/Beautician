package com.wellgel.london.Customer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;
import com.wellgel.london.APIs.Customer_APIs;
import com.wellgel.london.Customer.SerializeModelClasses.C_CreateAppointment;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.PreferencesShared;
import com.wellgel.london.UtilClasses.Retrofit.RetrofitClientInstance;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class C_SelectDateAct extends AppCompatActivity implements DatePickerListener {

    private TextView near_salon_name, salonBookingDate, salonBookingTime, near_salon_address, salon_new, confirm_booking, shoTimeSelected;

    private ImageView c_dash_navi;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private String formattedDate;
    private String dateMatch = "";
    private CardView bookinCard;
    private String st_day, st_date, st_month, st_year, st_time;
    private PreferencesShared shared;
    private ProgressDialog progressDoalog;
    private C_SelectDateAct activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__select_date);
        // find the picker

        activity = this;
        shared = new PreferencesShared(activity);
        HorizontalPicker picker = (HorizontalPicker) findViewById(R.id.datePicker);
        shoTimeSelected = findViewById(R.id.shoTimeSelected);
        c_dash_navi = findViewById(R.id.c_dash_navi);
        confirm_booking = findViewById(R.id.confirm_booking);
        near_salon_name = findViewById(R.id.near_salon_name);
        salon_new = findViewById(R.id.salon_new);
        near_salon_address = findViewById(R.id.near_salon_address);
        bookinCard = findViewById(R.id.bookinCard);
        salonBookingDate = findViewById(R.id.salonBookingDate);
        salonBookingTime = findViewById(R.id.salonBookingTime);
        fragmentManager = getSupportFragmentManager();


        c_dash_navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd/M/yyyy");
        formattedDate = df.format(date);

        shoTimeSelected.setEnabled(true);
        shoTimeSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dateMatch.equalsIgnoreCase("")) {
                    Toast.makeText(C_SelectDateAct.this, "Please Select Date first", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(C_SelectDateAct.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                            if (formattedDate.equalsIgnoreCase(dateMatch)) {
                                if (selectedHour < hour) {
                                    setTime(hour, minute);
                                } else if (selectedHour == hour) {
                                    if (selectedMinute < minute) {
                                        Toast.makeText(C_SelectDateAct.this, "Please coorect time", Toast.LENGTH_SHORT).show();
                                    } else {
                                        setTime(selectedHour, selectedMinute);
                                    }
                                } else {
                                    setTime(selectedHour, selectedMinute);
                                }
                            } else {

                                setTime(selectedHour, selectedMinute);
                            }
//                        selctStartTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, false);//Yes 24 hour time


                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();


                }
            }
        });


// initialize it and attach a listener
        picker.setListener(this)
                .setDays(120)
                .setOffset(0)
                .setDateSelectedColor(getResources()
                        .getColor(R.color.pink))
                .setDateSelectedTextColor(Color.WHITE)
                .setMonthAndYearTextColor(Color.BLACK)
                .setTodayButtonTextColor(getResources()
                        .getColor(R.color.pink))
                .setTodayDateTextColor(getResources()
                        .getColor(R.color.pink))
//                .setTodayDateBackgroundColor(Color.GRAY)
//                .setUnselectedDayTextColor(Color.DKGRAY)
//                .setDayOfWeekTextColor(Color.DKGRAY)
                .setUnselectedDayTextColor(getResources()
                        .getColor(R.color.pink))
                .showTodayButton(false)
                .init();

        picker.setBackgroundColor(getResources().getColor(R.color.white));


    }


    @Override
    public void onDateSelected(DateTime dateSelected) {

        day(dateSelected);
        month(dateSelected);
        st_year = dateSelected.getYear() + "";
        st_date = dateSelected.getDayOfMonth() + "";

        if (st_date.length() == 1) {
            st_date = "0" + st_date;
        }
        shoTimeSelected.setTextColor(getResources().getColor(R.color.black));

        dateMatch = dateSelected.getDayOfMonth() + "/" + dateSelected.getMonthOfYear() + "/" + dateSelected.getYear();

        confirm_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateTime = st_year + "-" + getmonthCount(dateSelected) + "-" + st_date
                        + " " + st_time;
                createAppointMent(dateTime);
            }
        });


    }

    private String getmonthCount(DateTime dateTime) {
        String value = String.valueOf(dateTime.getMonthOfYear());
        if (value.equalsIgnoreCase("1")) {
            value = "01";
        } else if (value.equalsIgnoreCase("2")) {

            value = "02";
        } else if (value.equalsIgnoreCase("3")) {

            value = "03";
        } else if (value.equalsIgnoreCase("4")) {

            value = "04";
        } else if (value.equalsIgnoreCase("5")) {

            value = "05";
        } else if (value.equalsIgnoreCase("6")) {

            value = "06";
        } else if (value.equalsIgnoreCase("7")) {

            value = "07";
        } else if (value.equalsIgnoreCase("8")) {

            value = "08";
        } else if (value.equalsIgnoreCase("9")) {

            value = "09";
        }
        return value;
    }

    private void month(DateTime dateSelected) {
        if (dateSelected.getMonthOfYear() == 1) {
            st_month = "January";
        }
        if (dateSelected.getMonthOfYear() == 2) {
            st_month = "Febuary";
        }
        if (dateSelected.getMonthOfYear() == 3) {
            st_month = "March";
        }
        if (dateSelected.getMonthOfYear() == 4) {
            st_month = "April";
        }
        if (dateSelected.getMonthOfYear() == 5) {
            st_month = "May";
        }
        if (dateSelected.getMonthOfYear() == 6) {
            st_month = "June";
        }
        if (dateSelected.getMonthOfYear() == 7) {
            st_month = "July";
        }
        if (dateSelected.getMonthOfYear() == 8) {
            st_month = "August";
        }
        if (dateSelected.getMonthOfYear() == 9) {
            st_month = "September";
        }
        if (dateSelected.getMonthOfYear() == 10) {
            st_month = "October";
        }
        if (dateSelected.getMonthOfYear() == 11) {
            st_month = "November";
        }
        if (dateSelected.getMonthOfYear() == 12) {
            st_month = "December";
        }


    }

    private void day(DateTime dateSelected) {
        if (dateSelected.getDayOfWeek() == 1) {
            st_day = "Monday";
        } else if (dateSelected.getDayOfWeek() == 2) {
            st_day = "Tuesday";
        } else if (dateSelected.getDayOfWeek() == 3) {
            st_day = "Wednesday";
        } else if (dateSelected.getDayOfWeek() == 4) {
            st_day = "Thursday";
        } else if (dateSelected.getDayOfWeek() == 5) {
            st_day = "Friday";
        } else if (dateSelected.getDayOfWeek() == 6) {
            st_day = "Saturday";
        } else if (dateSelected.getDayOfWeek() == 7) {
            st_day = "Sunday";
        }


    }

    private void setTime(int selectedHour, int selectedMinute) {

        st_time = selectedHour + ":" + selectedMinute;
        bookinCard.setVisibility(View.VISIBLE);
        confirm_booking.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(C_SelectDateAct.this, R.anim.slide_in_right);
        Animation animation2 = AnimationUtils.loadAnimation(C_SelectDateAct.this, R.anim.slide_in_left);
        bookinCard.setAnimation(animation);
        confirm_booking.setAnimation(animation2);

        near_salon_name.setText(getIntent().getStringExtra("salon_name"));
        near_salon_address.setText(getIntent().getStringExtra("salon_address"));

        salonBookingDate.setText(st_day + "," + st_date + " " + st_month + " " + st_year);
//        if (getIntent().getIntExtra("salon_rating", 0) == 0) {
//            near_salon_rating.setVisibility(View.GONE);
//            salon_new.setVisibility(View.VISIBLE);
//        } else {
//            near_salon_rating.setVisibility(View.VISIBLE);
//            salon_new.setVisibility(View.GONE);
//            near_salon_rating.setRating(getIntent().getIntExtra("salon_rating", 0));
//        }
        if (selectedHour > 12) {

            salonBookingTime.setText(String.valueOf(selectedHour - 12) + ":" + (String.valueOf(selectedMinute) + " PM"));
            shoTimeSelected.setText(String.valueOf(selectedHour - 12) + ":" + (String.valueOf(selectedMinute) + " PM"));
        } else if (selectedHour == 12) {
            salonBookingTime.setText("12" + ":" + (String.valueOf(selectedMinute) + " PM"));
            shoTimeSelected.setText("12" + ":" + (String.valueOf(selectedMinute) + " PM"));
        } else if (selectedHour < 12) {
            if (selectedHour != 0) {
                salonBookingTime.setText(String.valueOf(selectedHour) + ":" + (String.valueOf(selectedMinute) + " AM"));
                shoTimeSelected.setText(String.valueOf(selectedHour) + ":" + (String.valueOf(selectedMinute) + " AM"));
            } else {
                salonBookingTime.setText("12" + ":" + (String.valueOf(selectedMinute) + " AM"));
                shoTimeSelected.setText("12" + ":" + (String.valueOf(selectedMinute) + " AM"));
            }
        }
    }

    public void createAppointMent(String dateTime) {

        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Checking appointment....");
        progressDoalog.show();

        /*Create handle for the RetrofitInstance interface*/
        Customer_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Customer_APIs.class);
        Call<C_CreateAppointment> call = service.appointmentsCreate("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"), ConstantClass.HAND_COLOR, ConstantClass.NAIL_POlish_COLOR, ConstantClass.NAIL_SHAPE, ConstantClass.SALON_ID, dateTime, "");
        call.enqueue(new Callback<C_CreateAppointment>() {
            @Override
            public void onResponse(Call<C_CreateAppointment> call, Response<C_CreateAppointment> response) {


                progressDoalog.dismiss();
                if (response.body() != null) {
                    if (response.isSuccessful()) {


                        if (response.body().getStatus()) {
                            Intent intent = new Intent(activity, C_DashboardAct.class);
//                            intent.putExtra("appo_id", response.body().getData().get)

                            startActivity(intent);
                            finish();
                        } else {

                        }


                    } else {
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
                        Toast.makeText(activity, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }

            }

            @Override
            public void onFailure(Call<C_CreateAppointment> call, Throwable t) {
                progressDoalog.dismiss();
            }
        });
    }

}
