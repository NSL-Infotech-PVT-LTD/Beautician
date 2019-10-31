package com.wellgel.london.Customer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wellgel.london.APIs.Customer_APIs;
import com.wellgel.london.Customer.SerializeModelClasses.EcomAppoDetailSerial;
import com.wellgel.london.Provider.ModelSerialized.RescheduleAppointment;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.PreferencesShared;
import com.wellgel.london.UtilClasses.Retrofit.RetrofitClientInstance;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ecom_AppointlistDetail extends AppCompatActivity {

    private Ecom_AppointlistDetail activity;
    private PreferencesShared shared;
    private TextView resc_date, resc_time, near_salon_name, salonBookingDate, salonBookingTime, near_salon_address, priceText;
    private TextView booking_reject, booking_accept, pay_at_salon, pay_now;
    private ImageView c_booking_detail_navi, image_accept_reject_loader;
    private ProgressDialog progressDoalog;
    private Button reschedulingBTN;
    private LinearLayout lay_pay, lay_accep_reject, layout_for_reschedule;
    Date dt, avilDate;
    private boolean accept;
    private ArrayList<String> listSkinColor = new ArrayList<>();
    private ArrayList<String> listNailColor = new ArrayList<>();
    private ArrayList<String> listNailShape = new ArrayList<>();
    private int[][] handSamples = new int[][]{new int[]{R.drawable.square_fair_one, R.drawable.square_round_fair_one, R.drawable.round_fair_one, R.drawable.pointed_fair_one, R.drawable.pointed_round_fair_one},
            new int[]{R.drawable.square_fair_two, R.drawable.square_round_fair_two, R.drawable.round_fair_two, R.drawable.pointed_fair_two, R.drawable.pointed_round_fair_two},
            new int[]{R.drawable.square_medium, R.drawable.square_round_medium, R.drawable.round_medium, R.drawable.pointed_medium, R.drawable.pointed_round_medium},
            new int[]{R.drawable.square_black_one, R.drawable.square_round_black_one, R.drawable.round_black_one, R.drawable.pointed_black_one, R.drawable.pointed_round_black_one},
            new int[]{R.drawable.square_black_two, R.drawable.square_round_black_two, R.drawable.round_black_two, R.drawable.pointed_black_two, R.drawable.pointed_round_black_two}};
    private TextView nail_shape;
    private LinearLayout lay_nail_color, lay_skin_color;
    private CircleImageView c_dash_userhand_image;
    private RelativeLayout ln_nailColor;
    private LinearLayout bottomLayout;
    private TextView statusText, booking_confirmed;
    String[] availableTime = null;

    @Override
    public void onBackPressed() {

        if (accept) {
            accept = false;
            lay_accep_reject.setVisibility(View.VISIBLE);
            lay_pay.setVisibility(View.GONE);
            Animation animation2 = AnimationUtils.loadAnimation(activity, R.anim.slide_in_bottom);
            lay_accep_reject.setAnimation(animation2);
            Animation animation = AnimationUtils.loadAnimation(activity, R.anim.slide_out_bottom);
            lay_pay.setAnimation(animation);

        } else
            super.onBackPressed();
    }

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
        resc_date = findViewById(R.id.resc_date);
        resc_time = findViewById(R.id.resc_time);
        reschedulingBTN = findViewById(R.id.reschedulingBTN);
        booking_accept = findViewById(R.id.booking_accept);
        booking_reject = findViewById(R.id.booking_reject);
        pay_at_salon = findViewById(R.id.pay_at_salon);
        pay_now = findViewById(R.id.pay_now);
        image_accept_reject_loader = findViewById(R.id.image_accept_reject_loader);
        resc_date = findViewById(R.id.resc_date);
        resc_time = findViewById(R.id.resc_time);
        lay_nail_color = findViewById(R.id.lay_nail_color);
        lay_skin_color = findViewById(R.id.lay_skin_color);
        nail_shape = findViewById(R.id.nail_shape);
        ln_nailColor = findViewById(R.id.nail_back);
        c_dash_userhand_image = findViewById(R.id.c_dash_userhand_image);
        statusText = findViewById(R.id.statusText);
        bottomLayout = findViewById(R.id.booto);
        booking_confirmed = findViewById(R.id.booking_confirmed);
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

        booking_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                accept = true;
                lay_accep_reject.setVisibility(View.GONE);

                Animation animation2 = AnimationUtils.loadAnimation(activity, R.anim.slide_out_bottom);
                lay_accep_reject.setAnimation(animation2);

                Animation animation = AnimationUtils.loadAnimation(activity, R.anim.slide_in_bottom);
                lay_pay.setAnimation(animation);
                lay_pay.setVisibility(View.VISIBLE);

            }
        });
        funcList();


    }

    private void funcList() {
        ConstantClass.ListFunc(listSkinColor, listNailColor, listNailShape);

//        listNailColor.add(0, "#FFFFFF");
//        listNailColor.add(1, "#CC66CC");
//        listNailColor.add(2, "#333366");
//        listNailColor.add(3, "#009999");
//        listNailColor.add(4, "#CC00CC");
//        listNailColor.add(5, "#0033FF");
//        listNailColor.add(6, "#99FFFF");
//        listNailColor.add(7, "#CCFF99");
//        listNailColor.add(8, "#006633");
//        listNailColor.add(9, "#CC9900");
//        listNailColor.add(10, "#33FF00");
//        listNailColor.add(11, "#669966");
//        listNailColor.add(12, "#666666");
//        listNailColor.add(13, "#00FFCC");
//        listNailColor.add(14, "#993333");
//        listNailColor.add(15, "#990099");
//        listNailColor.add(16, "#9999FF");
//
//
//        listSkinColor.add(0, "#F2D9B7");
//        listSkinColor.add(1, "#EFB38A");
//        listSkinColor.add(2, "#A07561");
//        listSkinColor.add(3, "#795D4C");
//        listSkinColor.add(4, "#3D2923");
//
//
//        listNailShape.add(0, "square");
//        listNailShape.add(1, "round");
//        listNailShape.add(2, "oval");
//        listNailShape.add(3, "stillete");
//        listNailShape.add(4, "pointed");

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


    public void confirmedBookingPopUp(String name, String availTime, String nailColr, String nailShape, String handColor) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            final View layout = inflater.inflate(R.layout.custom_request_confirm,
                    (ViewGroup) this.findViewById(R.id.mainPop));
            // create a 300px width and 470px height PopupWindow
            final PopupWindow pw = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
            // display the popup in the center
            pw.showAtLocation(layout, Gravity.TOP, 0, 0);
            TextView back_home = layout.findViewById(R.id.back_home);
            TextView respond = layout.findViewById(R.id.respond);
            TextView request = layout.findViewById(R.id.request);

            respond.setVisibility(View.GONE);
            request.setText(getString(R.string.customerServiceDetail));

            near_salon_name = layout.findViewById(R.id.near_salon_name);
            nail_shape = layout.findViewById(R.id.nail_shape);
            salonBookingDate = layout.findViewById(R.id.salonBookingDate);
            salonBookingTime = layout.findViewById(R.id.salonBookingTime);
            lay_nail_color = layout.findViewById(R.id.lay_nail_color);
            lay_skin_color = layout.findViewById(R.id.lay_skin_color);
            c_dash_userhand_image = layout.findViewById(R.id.c_dash_userhand_image);
            ln_nailColor = layout.findViewById(R.id.nail_back);

            near_salon_name.setText(name);
//                                near_salon_address.setText(response.body().getData().get(0).getCustomerDetails().get(0).getAddress());
            String[] separated = availTime.split(" ");
            lay_skin_color.setBackgroundColor(Color.parseColor(handColor + ""));
            lay_nail_color.setBackgroundColor(Color.parseColor(nailColr + ""));
            salonBookingDate.setText(parseDateToddMMyyyy(separated[0]));


            Drawable unwrappedDrawable = AppCompatResources.getDrawable(activity, R.drawable.circle_view);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor(nailColr + ""));

            ln_nailColor.setBackground(wrappedDrawable);

//            c_dash_userhand_image.setBackgroundColor(Color.parseColor(nailColr + ""));
            nail_shape.setText("Nail Shape  " + nailShape);
            nailShape(c_dash_userhand_image, nailShape);
            skinColor(c_dash_userhand_image, handColor, nailShape);

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm aa");
            Date dt;
            try {
                dt = sdf.parse(separated[1]);
                salonBookingTime.setText(sdfs.format(dt));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            c_dash_userhand_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    initiatePopupWindow(handColor, nailColr, nailShape);
                }
            });


            back_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismisFunc();
                    pw.dismiss();
                }
            });

            RelativeLayout main_pop_up = layout.findViewById(R.id.main_pop_up);
            main_pop_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dismisFunc();
                    pw.dismiss();
                }
            });

            pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    dismisFunc();
                    pw.dismiss();

                }
            });

        } catch (
                Exception e) {
            e.printStackTrace();
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
                                    String availSeprate = response.body().getData().get(0).getAvailableDatetime() + "";

                                    lay_skin_color.setBackgroundColor(Color.parseColor(response.body().getData().get(0).getSkinColor()));
                                    lay_nail_color.setBackgroundColor(Color.parseColor(response.body().getData().get(0).getNailPolishColor()));

                                    Drawable unwrappedDrawable = AppCompatResources.getDrawable(activity, R.drawable.circle_view);
                                    Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                                    DrawableCompat.setTint(wrappedDrawable, Color.parseColor(response.body().getData().get(0).getNailPolishColor()));

                                    ln_nailColor.setBackground(wrappedDrawable);
                                    nail_shape.setText("Nail Shape  " + response.body().getData().get(0).getNailShape());
                                    nailShape(c_dash_userhand_image, response.body().getData().get(0).getNailShape());
                                    skinColor(c_dash_userhand_image, response.body().getData().get(0).getSkinColor(), response.body().getData().get(0).getNailShape());
                                    c_dash_userhand_image.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            initiatePopupWindow(response.body().getData().get(0).getSkinColor(), response.body().getData().get(0).getNailPolishColor(), response.body().getData().get(0).getNailShape());
                                        }
                                    });


                                    priceText.setText(getString(R.string.currency) + response.body().getData().get(0).getPrice());
                                    String[] separated = currentString.split(" ");
                                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                                    SimpleDateFormat requestime = new SimpleDateFormat("hh:mm");
                                    SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm aa");

                                    if (response.body().getData().get(0).getStatus().equalsIgnoreCase(ConstantClass.STATUS_REQUESTED)) {
                                        statusText.setVisibility(View.VISIBLE);
                                        bottomLayout.setVisibility(View.VISIBLE);
                                        lay_accep_reject.setVisibility(View.GONE);
                                        lay_pay.setVisibility(View.GONE);
                                        layout_for_reschedule.setVisibility(View.GONE);
                                        if ((response.body().getData().get(0).getAvailableDatetime() != null) && (response.body().getData().get(0).getAvailableDatetime() != response.body().getData().get(0).getRequestedDatetime())) {

                                            layout_for_reschedule.setVisibility(View.VISIBLE);
                                            lay_accep_reject.setVisibility(View.VISIBLE);
                                            lay_pay.setVisibility(View.VISIBLE);
                                            if (!availSeprate.equalsIgnoreCase("null")) {
                                                availableTime = availSeprate.split(" ");

                                                resc_date.setText(parseDateToddMMyyyy(availableTime[0]));

                                                try {
                                                    avilDate = sdf.parse(availableTime[1]);

                                                    if (avilDate != null) {
                                                        resc_time.setText(sdfs.format(avilDate));
                                                    }
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            image_accept_reject_loader.setImageDrawable(getResources().getDrawable(R.drawable.ic_loader_half));

                                            reschedulingBTN.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(activity, C_SelectDateAct.class);
                                                    intent.putExtra("salon_name", response.body().getData().get(0).getSalonDetails().get(0).getName());
                                                    intent.putExtra("salon_address", response.body().getData().get(0).getSalonDetails().get(0).getAddress());
                                                    intent.putExtra("hand_color", response.body().getData().get(0).getSkinColor());
                                                    intent.putExtra("nail_color", response.body().getData().get(0).getNailPolishColor());
                                                    intent.putExtra("nail_shape", response.body().getData().get(0).getNailShape());
                                                    intent.putExtra("startTime", response.body().getData().get(0).getSalonDetails().get(0).getBusinessHourStart());
                                                    intent.putExtra("endTime", response.body().getData().get(0).getSalonDetails().get(0).getBusinessHourEnd());
                                                    intent.putExtra("from", "resc");
                                                    intent.putExtra("appo_id", appo_id);
                                                    startActivity(intent);
                                                }
                                            });


                                        } else {
                                            bottomLayout.setVisibility(View.GONE);
                                            statusText.setText("Your Appointment is Awaiting......");

                                        }
                                    } else if (response.body().getData().get(0).getStatus().equalsIgnoreCase(ConstantClass.STATUS_OPEN)) {
                                        statusText.setVisibility(View.VISIBLE);
                                        bottomLayout.setVisibility(View.VISIBLE);
                                        lay_accep_reject.setVisibility(View.VISIBLE);
                                        lay_pay.setVisibility(View.VISIBLE);
                                        layout_for_reschedule.setVisibility(View.GONE);


                                    } else {

                                        if (!availSeprate.equalsIgnoreCase("null")) {
                                            String[] availableTime = availSeprate.split(" ");

                                            resc_date.setText(parseDateToddMMyyyy(availableTime[0]));

                                            try {
                                                avilDate = sdf.parse(availableTime[1]);

                                                if (avilDate != null) {
                                                    resc_time.setText(sdfs.format(avilDate));
                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        statusText.setVisibility(View.VISIBLE);
                                        reschedulingBTN.setVisibility(View.GONE);
                                        lay_accep_reject.setVisibility(View.GONE);
                                        lay_pay.setVisibility(View.GONE);
                                        bottomLayout.setVisibility(View.GONE);
                                        if (response.body().getData().get(0).getStatus().equalsIgnoreCase(ConstantClass.STATUS_ACCEPTED)) {
//                                            booking_confirmed.setTypeface(null, Typeface.BOLD);
                                            booking_confirmed.setTextColor(Color.parseColor("#000000"));

                                            image_accept_reject_loader.setImageDrawable(getResources().getDrawable(R.drawable.ic_loader_full));
                                            String value = "";
                                            if (response.body().getData().get(0).getPaymentMode().equals("pay_now"))
                                                value = "Appointment Already Paid by You";
                                            else if (response.body().getData().get(0).getPaymentMode().equals("pay_now"))
                                                value = "You Have To Paid For This Appointment at Salon";
                                            statusText.setText("Your Appointment Has Been Accepted By Salon " + "\nPayment Mode :" + value);


                                        } else if (response.body().getData().get(0).getStatus().equalsIgnoreCase(ConstantClass.STATUS_REJECTED)) {
                                            image_accept_reject_loader.setImageDrawable(getResources().getDrawable(R.drawable.ic_loader_half));
                                            statusText.setText("Your Appointment Has Been Rejected By Salon");

                                        } else if (response.body().getData().get(0).getStatus().equalsIgnoreCase(ConstantClass.STATUS_CANCEL)) {
                                            image_accept_reject_loader.setImageDrawable(getResources().getDrawable(R.drawable.ic_loader_half));
                                            statusText.setText("You Have Cancelled This Appointment");

                                        }
                                    }

                                    salonBookingDate.setText(parseDateToddMMyyyy(separated[0]));


                                    try {
                                        dt = sdf.parse(separated[1]);
//                                        avilDate = sdf.parse(availableTime[1]);
                                        if (dt != null) {
                                            salonBookingTime.setText(sdfs.format(dt));
                                        }
//                                        if (avilDate != null) {
//                                            resc_time.setText(sdfs.format(avilDate));
//                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    booking_reject.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


                                            acceptReject(response.body().getData().get(0).getSalonDetails().get(0).getName(), "Cancel your appointment....", appo_id, "", "cancel", separated[0] + " " + requestime.format(dt));
                                        }
                                    });

                                    pay_now.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


                                            Intent intent = new Intent(activity, C_MakePaymentAct.class);
                                            intent.putExtra("appo_id", appo_id);
                                            intent.putExtra("dateTime", separated[0] + " " + requestime.format(dt));
                                            startActivity(intent);

                                        }
                                    });
                                    pay_at_salon.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            acceptReject(response.body().getData().get(0).getSalonDetails().get(0).getName(), "Confirm appointment....", getIntent().getIntExtra("appo_id", 0), "pay_at_salon", "accepted", separated[0] + " " + requestime.format(dt));

                                        }
                                    });


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
                                        if (dt != null) {
                                            salonBookingTime.setText(sdfs.format(dt));
                                        }
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

    public void acceptReject(String name, String dialogtext, int appo_id, String paymentMode, String
            status, String dateTime) {

        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage(dialogtext);
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

                            if (response.body().getData().getAppointments().getStatus().equalsIgnoreCase(ConstantClass.STATUS_ACCEPTED))
                                confirmedBookingPopUp(name, response.body().getData().getAppointments().getAvailableDatetime()
                                        , response.body().getData().getAppointments().getNailPolishColor(),
                                        response.body().getData().getAppointments().getNailShape(),
                                        response.body().getData().getAppointments().getSkinColor());

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
