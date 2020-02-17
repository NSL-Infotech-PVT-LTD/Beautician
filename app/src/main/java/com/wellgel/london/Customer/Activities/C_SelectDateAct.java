package com.wellgel.london.Customer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;
import com.wellgel.london.APIs.Customer_APIs;
import com.wellgel.london.APIs.Provider_APIs;
import com.wellgel.london.Customer.SerializeModelClasses.C_CreateAppointment;
import com.wellgel.london.Provider.ModelSerialized.RescheduleAppointment;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.PreferencesShared;
import com.wellgel.london.UtilClasses.Retrofit.RetrofitClientInstance;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.sql.Struct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wellgel.london.UtilClasses.AlertClas.alertDialog;

public class C_SelectDateAct extends AppCompatActivity implements DatePickerListener {

    private TextView near_salon_name, salonBookingDate, salonBookingTime, near_salon_address, confirm_booking, shoTimeSelected;

    private ImageView c_dash_navi;

    private String formattedDate;
    private String dateMatch = "";
    private CardView bookinCard;
    private String st_day, st_date, st_month, st_year, st_time;
    private PreferencesShared shared;
    private ProgressDialog progressDoalog;
    private C_SelectDateAct activity;
    private ArrayList<String> listSkinColor = new ArrayList<>();
    private ArrayList<String> listNailColor = new ArrayList<>();
    private ArrayList<String> listNailShape = new ArrayList<>();
    private int[][] handSamples = new int[][]{new int[]{R.drawable.square_fair_one, R.drawable.square_round_fair_one, R.drawable.round_fair_one, R.drawable.pointed_fair_one, R.drawable.pointed_round_fair_one},
            new int[]{R.drawable.square_fair_two, R.drawable.square_round_fair_two, R.drawable.round_fair_two, R.drawable.pointed_fair_two, R.drawable.pointed_round_fair_two},
            new int[]{R.drawable.square_medium, R.drawable.square_round_medium, R.drawable.round_medium, R.drawable.pointed_medium, R.drawable.pointed_round_medium},
            new int[]{R.drawable.square_black_one, R.drawable.square_round_black_one, R.drawable.round_black_one, R.drawable.pointed_black_one, R.drawable.pointed_round_black_one},
            new int[]{R.drawable.square_black_two, R.drawable.square_round_black_two, R.drawable.round_black_two, R.drawable.pointed_black_two, R.drawable.pointed_round_black_two}};
    private TextView nail_shape, salonTime;
    private LinearLayout lay_nail_color, lay_skin_color;
    private CircleImageView c_dash_userhand_image;
    private RelativeLayout ln_nailColor;
    private String[] startTime, endTime;
    private String currentString, currentString2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__select_date);

        activity = this;
        shared = new PreferencesShared(activity);
        HorizontalPicker picker = (HorizontalPicker) findViewById(R.id.datePicker);
        shoTimeSelected = findViewById(R.id.shoTimeSelected);
        c_dash_navi = findViewById(R.id.c_dash_navi);
        confirm_booking = findViewById(R.id.confirm_booking);
        near_salon_name = findViewById(R.id.near_salon_name);
        near_salon_address = findViewById(R.id.near_salon_address);
        bookinCard = findViewById(R.id.bookinCard);
        salonBookingDate = findViewById(R.id.salonBookingDate);
        salonBookingTime = findViewById(R.id.salonBookingTime);
        lay_nail_color = findViewById(R.id.lay_nail_color);
        lay_skin_color = findViewById(R.id.lay_skin_color);
        nail_shape = findViewById(R.id.nail_shape);
        ln_nailColor = findViewById(R.id.nail_back);
        c_dash_userhand_image = findViewById(R.id.c_dash_userhand_image);
        salonTime = findViewById(R.id.salonTime);

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
                    Toast.makeText(activity, "Please Select Date first", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {


                            if (formattedDate.equalsIgnoreCase(dateMatch)) {
                                if (selectedHour < hour) {
                                    Toast.makeText(activity, "This Session has been Expired ,Choose Correct Time", Toast.LENGTH_SHORT).show();
                                } else if (selectedHour == hour) {
                                    if (selectedMinute < minute) {
                                        Toast.makeText(activity, "Choose Correct Time", Toast.LENGTH_SHORT).show();
                                    } else {
                                        compareTime(selectedHour, selectedMinute);
                                    }
                                } else {
                                    compareTime(selectedHour, selectedMinute);
                                }
                            } else {

                                compareTime(selectedHour, selectedMinute);
                            }
//                            selectStartTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, false);//Yes 24 hour time


                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();


                }
            }
        });


// initialize it and attach a listener
        picker.setListener(this)
                .setDays(30)
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

        funcList();

        c_dash_userhand_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extra = getIntent().getExtras();
                if (extra != null) {
                    if (extra.containsKey("hand_color")) {
                        initiatePopupWindow(extra.getString("hand_color"), extra.getString("nail_color"), extra.getString("nail_shape"));
                    } else {
                        initiatePopupWindow(ConstantClass.HAND_COLOR, ConstantClass.NAIL_POlish_COLOR, ConstantClass.NAIL_SHAPE);

                    }
                }
            }
        });

        currentString = getIntent().getStringExtra("startTime");
        currentString2 = getIntent().getStringExtra("endTime");
        startTime = new String[0];
        endTime = new String[0];
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat requestime = new SimpleDateFormat("hh:mm aa");
        Date dt = null, dt2 = null;

        try {
            dt = sdf.parse(currentString);
            dt2 = sdf.parse(currentString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        salonTime.setText("Timing : " + requestime.format(dt) + " - " + requestime.format(dt2));

    }


    private void compareTime(int selectedHour, int selectedMinute) {

        if ((currentString != null) && (currentString2 != null)) {
            startTime = currentString.split(":");
            endTime = currentString2.split(":");


            int compareHour = Integer.parseInt(startTime[0]);
            int compareMint = Integer.parseInt(startTime[1]);

            int compareEndHour = Integer.parseInt(endTime[0]);
            int compareEndMint = Integer.parseInt(endTime[1]);

            if (compareHour == selectedHour) {
                if ((compareMint < selectedMinute) || (compareMint == selectedMinute)) {

                    setTime(selectedHour, selectedMinute);
                } else {
                    alertDialog("Please Enter Time According to Salon Timing", activity);
                }
            } else if ((compareEndHour == selectedHour)) {
                if ((compareEndMint > selectedMinute) || (compareEndMint == selectedMinute)) {

                    setTime(selectedHour, selectedMinute);
                } else {
                    alertDialog("Please Enter Time According to Salon Timing", activity);
                }
            } else if ((compareHour < selectedHour) && (compareEndHour > selectedHour)) {
                if ((compareEndMint < selectedMinute) || (compareEndMint == selectedMinute)) {
                    setTime(selectedHour, selectedMinute);

                } else {
                    alertDialog("Please Enter Time According to Salon Timing", activity);
                }
            } else {
                alertDialog("Please Enter Time According to Salon Timing", activity);
            }
        }
    }

    private void funcList() {
        ConstantClass.ListFunc(listSkinColor, listNailColor, listNailShape);
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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            if (extras.containsKey("from")) {
                confirm_booking.setText(getString(R.string.updateDateTime));
            } else {
                confirm_booking.setText(getString(R.string.cofirmBooking));
            }
        }

        confirm_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateTime = st_year + "-" + getmonthCount(dateSelected) + "-" + st_date
                        + " " + st_time;

                if (confirm_booking.getText().toString().equalsIgnoreCase(getString(R.string.updateDateTime))) {

                    acceptReject(getIntent().getIntExtra("appo_id", 0), "", ConstantClass.STATUS_REQUESTED_BY_CUSTOMER, dateTime);
                } else
                    createAppointMent(dateTime);
            }
        });
        salonBookingDate.setText(st_day + "," + st_date + " " + st_month + " " + st_year);


    }


    public void initiatePopupWindow(String handcolor, String nailColor, String nailShape) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            final View layout = inflater.inflate(R.layout.custom_pop_up_to_view_hand_image,
                    (ViewGroup) this.findViewById(R.id.mainPop));
            // create a 300px width and 470px height PopupWindow
            final PopupWindow pw = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            // display the popup in the center
            pw.showAtLocation(layout, Gravity.TOP, 0, 0);
            final RelativeLayout apply = layout.findViewById(R.id.mainLAyout);
            final ImageView handImage = layout.findViewById(R.id.hand_image);
            final ImageView close = layout.findViewById(R.id.close);

            apply.setBackgroundColor(Color.parseColor(nailColor + ""));
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pw.dismiss();
                }
            });

            skinColor(handImage, handcolor, nailShape);
//            nailShape(handImage, nailShape);
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

    private void skinColor(ImageView handImage, String handcolor, String nailshape) {

        int handvalue = 0;
        int nailvalue = 0;
        for (int i = 0; i < listSkinColor.size(); i++) {

            if (handcolor.equals(listSkinColor.get(i)))
                if (handcolor.equals(listSkinColor.get(i)))
                    handvalue = i;
            for (int j = 0; j < listNailShape.size(); j++) {
                if (nailshape.equals(listNailShape.get(j)))
                    nailvalue = j;

            }
        }

        handImage.setImageResource(handSamples[handvalue][nailvalue]);

//        if (handcolor.equalsIgnoreCase(listSkinColor.get(0))) {
//            handImage.setImageResource(handSamples[0][0]);
//            nailShape(handImage, nailColor);
//
//        } else if (handcolor.equalsIgnoreCase(listSkinColor.get(1))) {
//
//            nailShape(handImage, nailColor);
//            handImage.setImageResource(handSamples[1][0]);
//        } else if (handcolor.equalsIgnoreCase(listSkinColor.get(2))) {
//            nailShape(handImage, nailColor);
//            handImage.setImageResource(handSamples[2][0]);
//
//        } else if (handcolor.equalsIgnoreCase(listSkinColor.get(3))) {
//            nailShape(handImage, nailColor);
//            handImage.setImageResource(handSamples[3][0]);
//        } else if (handcolor.equalsIgnoreCase(listSkinColor.get(4))) {
//            nailShape(handImage, nailColor);
//            handImage.setImageResource(handSamples[4][0]);
//        }
    }


    public void acceptReject(int appo_id, String paymentMode, String status, String dateTime) {

        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Requesting appointment....");
        progressDoalog.show();

        /*Create handle for the RetrofitInstance interface*/
        Customer_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Customer_APIs.class);
        Call<RescheduleAppointment> call = service.appointmentUpddateByCustomer("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"), appo_id + "", dateTime, status, "", "", paymentMode);
        call.enqueue(new Callback<RescheduleAppointment>() {
            @Override
            public void onResponse(Call<RescheduleAppointment> call, Response<RescheduleAppointment> response) {


                progressDoalog.dismiss();
                if (response.body() != null) {
                    if (response.isSuccessful()) {


                        if (response.body().getStatus()) {

                            if (status.equals("open")) {


                            } else dismisFunc();

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
            public void onFailure(Call<RescheduleAppointment> call, Throwable t) {
                progressDoalog.dismiss();
            }
        });
    }

    private void dismisFunc() {
        Intent intent = new Intent(activity, C_DashboardAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
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

        if ((selectedHour == 0) || (selectedHour == 1) || (selectedHour == 2) || (selectedHour == 3) ||
                (selectedHour == 4) || (selectedHour == 5) || (selectedHour == 6) ||
                (selectedHour == 7) || (selectedHour == 8)
                || (selectedHour == 9)) {
            if ((selectedMinute == 0) || (selectedMinute == 1) || (selectedMinute == 2) || (selectedMinute == 3) ||
                    (selectedMinute == 4) || (selectedMinute == 5) || (selectedMinute == 6) ||
                    (selectedMinute == 7) || (selectedMinute == 8)
                    || (selectedMinute == 9)) {
                st_time = "0" + selectedHour + ":" + "0" + selectedMinute;
            } else {
                st_time = "0" + selectedHour + ":" + selectedMinute;
            }
        } else if ((selectedMinute == 0) || (selectedMinute == 1) || (selectedMinute == 2) || (selectedMinute == 3) ||
                (selectedMinute == 4) || (selectedMinute == 5) || (selectedMinute == 6) ||
                (selectedMinute == 7) || (selectedMinute == 8)
                || (selectedMinute == 9)) {
            st_time = selectedHour + ":" + "0" + selectedMinute;
        } else {
            st_time = selectedHour + ":" + selectedMinute;
        }
        bookinCard.setVisibility(View.VISIBLE);
        confirm_booking.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.slide_in_right);
        Animation animation2 = AnimationUtils.loadAnimation(activity, R.anim.slide_in_left);
        bookinCard.setAnimation(animation);
        confirm_booking.setAnimation(animation2);

        near_salon_name.setText(getIntent().getStringExtra("salon_name"));
        near_salon_address.setText(getIntent().getStringExtra("salon_address"));
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            if (extra.containsKey("hand_color")) {
                lay_skin_color.setBackgroundColor(Color.parseColor(extra.getString("hand_color")));
                lay_nail_color.setBackgroundColor(Color.parseColor(extra.getString("nail_color")));

                Drawable unwrappedDrawable = AppCompatResources.getDrawable(activity, R.drawable.circle_view);
                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                DrawableCompat.setTint(wrappedDrawable, Color.parseColor(extra.getString("nail_color")));

                ln_nailColor.setBackground(wrappedDrawable);
                nail_shape.setText("Nail Shape  " + extra.getString("nail_shape"));
                nailShape(c_dash_userhand_image, extra.getString("nail_shape"));
                skinColor(c_dash_userhand_image, extra.getString("hand_color"), extra.getString("nail_shape"));
            } else {

                lay_skin_color.setBackgroundColor(Color.parseColor(ConstantClass.HAND_COLOR));
                lay_nail_color.setBackgroundColor(Color.parseColor(ConstantClass.NAIL_POlish_COLOR));

                Drawable unwrappedDrawable = AppCompatResources.getDrawable(activity, R.drawable.circle_view);
                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                DrawableCompat.setTint(wrappedDrawable, Color.parseColor(ConstantClass.NAIL_POlish_COLOR));

                ln_nailColor.setBackground(wrappedDrawable);
                nail_shape.setText("Nail Shape  " + ConstantClass.NAIL_SHAPE);
                nailShape(c_dash_userhand_image, ConstantClass.NAIL_SHAPE);
                skinColor(c_dash_userhand_image, ConstantClass.HAND_COLOR, ConstantClass.NAIL_SHAPE);
            }
        }

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


            if ((selectedHour == 13) || (selectedHour == 14) || (selectedHour == 15) || (selectedHour == 16) ||
                    (selectedHour == 17) || (selectedHour == 18) || (selectedHour == 19) ||
                    (selectedHour == 20) || (selectedHour == 21)) {
                if ((selectedMinute == 0) || (selectedMinute == 1) || (selectedMinute == 2) || (selectedMinute == 3) ||
                        (selectedMinute == 4) || (selectedMinute == 5) || (selectedMinute == 6) ||
                        (selectedMinute == 7) || (selectedMinute == 8)
                        || (selectedMinute == 9)) {
                    salonBookingTime.setText(String.valueOf("0" + (selectedHour - 12)) + ":" + (String.valueOf("0" + selectedMinute) + " PM"));
                    shoTimeSelected.setText(String.valueOf("0" + (selectedHour - 12)) + ":" + (String.valueOf("0" + selectedMinute) + " PM"));
                } else {
                    salonBookingTime.setText(String.valueOf("0" + (selectedHour - 12)) + ":" + (String.valueOf(selectedMinute) + " PM"));
                    shoTimeSelected.setText(String.valueOf("0" + (selectedHour - 12)) + ":" + (String.valueOf(selectedMinute) + " PM"));
                }


            } else {
                if ((selectedMinute == 0) || (selectedMinute == 1) || (selectedMinute == 2) || (selectedMinute == 3) ||
                        (selectedMinute == 4) || (selectedMinute == 5) || (selectedMinute == 6) ||
                        (selectedMinute == 7) || (selectedMinute == 8)
                        || (selectedMinute == 9)) {
                    salonBookingTime.setText(String.valueOf(selectedHour - 12) + ":" + (String.valueOf("0" + selectedMinute) + " PM"));
                    shoTimeSelected.setText(String.valueOf(selectedHour - 12) + ":" + (String.valueOf("0" + selectedMinute) + " PM"));
                } else {
                    salonBookingTime.setText(String.valueOf(selectedHour - 12) + ":" + (String.valueOf(selectedMinute) + " PM"));
                    shoTimeSelected.setText(String.valueOf(selectedHour - 12) + ":" + (String.valueOf(selectedMinute) + " PM"));
                }
            }


        } else if (selectedHour == 12) {
            salonBookingTime.setText("12" + ":" + (String.valueOf(selectedMinute) + " PM"));
            shoTimeSelected.setText("12" + ":" + (String.valueOf(selectedMinute) + " PM"));
        } else if (selectedHour < 12) {
            if (selectedHour != 0) {
                if ((selectedHour == 0) || (selectedHour == 1) || (selectedHour == 2) || (selectedHour == 3) ||
                        (selectedHour == 4) || (selectedHour == 5) || (selectedHour == 6) ||
                        (selectedHour == 7) || (selectedHour == 8)
                        || (selectedHour == 9)) {
                    if ((selectedMinute == 0) || (selectedMinute == 1) || (selectedMinute == 2) || (selectedMinute == 3) ||
                            (selectedMinute == 4) || (selectedMinute == 5) || (selectedMinute == 6) ||
                            (selectedMinute == 7) || (selectedMinute == 8)
                            || (selectedMinute == 9)) {
                        salonBookingTime.setText(String.valueOf("0" + selectedHour) + ":" + (String.valueOf("0" + selectedMinute) + " AM"));
                        shoTimeSelected.setText(String.valueOf("0" + selectedHour) + ":" + (String.valueOf("0" + selectedMinute) + " AM"));
                    } else {
                        salonBookingTime.setText(String.valueOf("0" + selectedHour) + ":" + (String.valueOf(selectedMinute) + " AM"));
                        shoTimeSelected.setText(String.valueOf("0" + selectedHour) + ":" + (String.valueOf(selectedMinute) + " AM"));
                    }


                } else {
                    if ((selectedMinute == 0) || (selectedMinute == 1) || (selectedMinute == 2) || (selectedMinute == 3) ||
                            (selectedMinute == 4) || (selectedMinute == 5) || (selectedMinute == 6) ||
                            (selectedMinute == 7) || (selectedMinute == 8)
                            || (selectedMinute == 9)) {
                        salonBookingTime.setText(String.valueOf(selectedHour) + ":" + (String.valueOf("0" + selectedMinute) + " AM"));
                        shoTimeSelected.setText(String.valueOf(selectedHour) + ":" + (String.valueOf("0" + selectedMinute) + " AM"));
                    } else {
                        salonBookingTime.setText(String.valueOf(selectedHour) + ":" + (String.valueOf(selectedMinute) + " AM"));
                        shoTimeSelected.setText(String.valueOf(selectedHour) + ":" + (String.valueOf(selectedMinute) + " AM"));
                    }
                }
            } else {
                salonBookingTime.setText("12" + ":" + (String.valueOf(selectedMinute) + " AM"));
                shoTimeSelected.setText("12" + ":" + (String.valueOf(selectedMinute) + " AM"));
            }
        }
    }

    private void nailShape(ImageView handImage, String nailshape) {
        if (nailshape.equalsIgnoreCase(listNailShape.get(0))) {
            handImage.setImageResource(handSamples[0][0]);

        } else if (nailshape.equalsIgnoreCase(listNailShape.get(1))) {
            handImage.setImageResource(handSamples[0][1]);
        } else if (nailshape.equalsIgnoreCase(listNailShape.get(2))) {
            handImage.setImageResource(handSamples[0][2]);
        } else if (nailshape.equalsIgnoreCase(listNailShape.get(3))) {
            handImage.setImageResource(handSamples[0][3]);
        } else if (nailshape.equalsIgnoreCase(listNailShape.get(4))) {
            handImage.setImageResource(handSamples[0][4]);
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


//    public static androidx.appcompat.app.AlertDialog alertDialog(String text,Context activity) {
//        return new androidx.appcompat.app.AlertDialog.Builder(activity)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setTitle("!Oops")
//                .setMessage(text)
//                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        dialog.dismiss();
//                    }
//
//                })
//                .show();
//}

}
