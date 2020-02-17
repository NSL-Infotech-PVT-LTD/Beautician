package com.wellgel.london.Customer.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.wellgel.london.APIs.Customer_APIs;
import com.wellgel.london.Customer.SerializeModelClasses.C_PlaceOrderSerial;
import com.wellgel.london.Provider.ModelSerialized.RescheduleAppointment;
import com.wellgel.london.R;
import com.wellgel.london.UtilClasses.ConstantClass;
import com.wellgel.london.UtilClasses.PreferencesShared;
import com.wellgel.london.UtilClasses.Retrofit.RetrofitClientInstance;

import org.json.JSONObject;

import java.util.Calendar;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wellgel.london.UtilClasses.ConstantClass.PUBLISHABLE_KEY;

public class C_MakePaymentAct extends AppCompatActivity {

    EditText cardNumberEditText, cardDate, cardCVV, cardHolder;
    protected Card cardToSave;
    TextView validateCard;
    Stripe stripe;
    private int month, year;
    private ProgressDialog progressDoalog;
    private C_MakePaymentAct activity;
    private PreferencesShared shared;
    private TextView textView, productName, productQuantity, productAddress;
    private AlertDialog dialogMultiOrder;
    private String datePattern = "\\d{2}/\\d{4}", currentString;


    String backString = "";
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__make_payment);

        init();


    }

    private void init() {
        activity = this;
        backString = "yes";
        shared = new PreferencesShared(activity);
        cardNumberEditText = findViewById(R.id.cardNumberEditText);
        cardDate = findViewById(R.id.carDate);
        cardCVV = findViewById(R.id.cardCVV);
        cardHolder = findViewById(R.id.cardHolder);
        textView = findViewById(R.id.textView);
        validateCard = findViewById(R.id.validateCard);
        stripe = new Stripe(this);

        extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("appo_id")) {
                textView.setText(getString(R.string.confimBookigText));
                validateCard.setText("Confirm Booking");
            } else {
                validateCard.setText(getString(R.string.continues));
                textView.setText(getString(R.string.Thnku));
            }
        }
        validateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePattern = "\\d{2}/\\d{4}";
                currentString = cardDate.getText().toString();
                if (cardNumberEditText.getText().toString().trim().isEmpty()) {
                    cardNumberEditText.setError(getString(R.string.invalidField));
                } else if (cardDate.getText().toString().trim().isEmpty()) {
                    cardDate.setError(getString(R.string.invalidField));
                } else if (!currentString.matches(datePattern)) {
                    cardDate.setError(getString(R.string.invalidDate));

                } else if (cardCVV.getText().toString().trim().isEmpty()) {
                    cardCVV.setError(getString(R.string.invalidField));
                } else {

                    String[] separated = currentString.split("/");

                    if ((!separated[0].isEmpty()) && (!separated[1].isEmpty())) {
                        month = Integer.parseInt(separated[0].trim());

                        year = Integer.parseInt(separated[1].trim());
                    }


                    buy();

                }

            }

        });
        cardNumberEditText.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (cardNumberEditText.getText().length() == 19)
                    cardDate.requestFocus();
                return false;
            }
        });

        cardDate.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (cardDate.getText().toString().matches(datePattern))
                    cardCVV.requestFocus();
                return false;

            }
        });

        cardCVV.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (cardCVV.getText().length() == 3)
                    cardHolder.requestFocus();
                return false;
            }
        });


        checkCard(cardNumberEditText);

        checkDate(cardDate);
    }

    private void buy() {
        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Validate your card....");
        progressDoalog.setCancelable(false);
        progressDoalog.show();


        cardToSave = new Card(
                cardNumberEditText.getText().toString(), //card number
                month, //expMonth
                year,//expYear
                cardCVV.getText().toString());//cvc

        boolean validation = cardToSave.validateCard();
        if (validation) {
            new Stripe(this).createToken(
                    cardToSave,
                    PUBLISHABLE_KEY,
                    new TokenCallback() {
                        @Override
                        public void onError(Exception error) {
                            progressDoalog.dismiss();
                            Log.d("Stripe", error.toString());
                        }

                        @Override
                        public void onSuccess(Token token) {
                            //  charge(token);

                            progressDoalog.dismiss();
                            Bundle extras = getIntent().getExtras();
                            if (extras != null) {
                                if (extras.containsKey("appo_id")) {
                                    bookingConfirm(getIntent().getIntExtra("appo_id", 0), getIntent().getStringExtra("dateTime"), token.getId());

                                }
                            }
                            else
                                placeOrder(Integer.parseInt(shared.getString("order_id")), token.getId());

                        }
                    });
        } else if (!cardToSave.validateNumber()) {
            progressDoalog.dismiss();

            Toast.makeText(activity, "" + getString(R.string.invalidCardDetail), Toast.LENGTH_SHORT).show();
        } else if (!cardToSave.validateExpiryDate()) {
            progressDoalog.dismiss();
            Toast.makeText(activity, "" + getString(R.string.invalidCardDetail), Toast.LENGTH_SHORT).show();
            Log.d("Stripe", "The expiration date that you entered is invalid");
        } else if (!cardToSave.validateCVC()) {
            progressDoalog.dismiss();
            Toast.makeText(activity, "" + getString(R.string.invalidCardDetail), Toast.LENGTH_SHORT).show();
            Log.d("Stripe", "The CVC code that you entered is invalid");
        } else {
            Log.d("Stripe", "The card details that you entered are invalid");
            Toast.makeText(activity, "" + getString(R.string.invalidCardDetail), Toast.LENGTH_SHORT).show();
            progressDoalog.dismiss();
        }
    }

    public void bookingConfirm(int appo_id, String dateTime, String token) {

        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Confirm appointment....");
        progressDoalog.show();

        /*Create handle for the RetrofitInstance interface*/
        Customer_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Customer_APIs.class);
        Call<RescheduleAppointment> call = service.appointmentUpddateByCustomer("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"), appo_id + "", dateTime, "accepted", "", token, "pay_now");
        call.enqueue(new Callback<RescheduleAppointment>() {
            @Override
            public void onResponse(Call<RescheduleAppointment> call, Response<RescheduleAppointment> response) {


                progressDoalog.dismiss();
                if (response.body() != null) {
                    if (response.isSuccessful()) {


                        if (response.body().getStatus()) {

                            dismisFunc();

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

    private void checkCard(EditText cardNumberEditText) {
        cardNumberEditText.addTextChangedListener(new TextWatcher() {

            private static final int TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
            private static final int TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
            private static final int DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
            private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
            private static final char DIVIDER = ' ';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // noop
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    s.replace(0, s.length(), buildCorrectString(getDigitArray(s, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER));

                }
                if (s.length() == 19) {
                    cardDate.requestFocus();
                }
            }

            private boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
                boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
                for (int i = 0; i < s.length(); i++) { // check that every element is right
                    if (i > 0 && (i + 1) % dividerModulo == 0) {
                        isCorrect &= divider == s.charAt(i);
                    } else {
                        isCorrect &= Character.isDigit(s.charAt(i));
                    }
                }
                return isCorrect;
            }

            private String buildCorrectString(char[] digits, int dividerPosition, char divider) {
                final StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < digits.length; i++) {
                    if (digits[i] != 0) {
                        formatted.append(digits[i]);
                        if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                            formatted.append(divider);
                        }
                    }
                }

                return formatted.toString();
            }

            private char[] getDigitArray(final Editable s, final int size) {
                char[] digits = new char[size];
                int index = 0;
                for (int i = 0; i < s.length() && index < size; i++) {
                    char current = s.charAt(i);
                    if (Character.isDigit(current)) {
                        digits[index] = current;
                        index++;
                    }
                }
                return digits;
            }
        });
    }

    private void checkDate(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "MMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int j, int i1, int i2) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 6) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int mon = Integer.parseInt(clean.substring(0, 2));
                        int year = Integer.parseInt(clean.substring(2, 6));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        clean = String.format("%02d%02d", mon, year);
                    }

                    clean = String.format("%s/%s",
                            clean.substring(0, 2),
                            clean.substring(2, 6));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    editText.setText(current);
                    editText.setSelection(sel < current.length() ? sel : current.length());

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
    }


    @Override
    public void onBackPressed() {

        if (extras != null) {
            if (extras.containsKey("appo_id")) {
                finish();
            }
        } else if (backString.equalsIgnoreCase("yes")) {
            backString = "";
            Toast.makeText(activity, "Press again to exit", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(activity, C_DashboardAct.class));
            finish();
        }
    }

    public void placeOrder(int id, String token) {
        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setMessage("Make a payment....");
        progressDoalog.setCancelable(false);
        progressDoalog.show();

        // initialize file here

        /*Create handle for the RetrofitInstance interface*/
        Customer_APIs service = RetrofitClientInstance.getRetrofitInstance().create(Customer_APIs.class);
        Call<C_PlaceOrderSerial> call = service.placeOrder("application/x-www-form-urlencoded", "Bearer " + shared.getString("token"), id + "", token);
        call.enqueue(new Callback<C_PlaceOrderSerial>() {
            @Override
            public void onResponse(Call<C_PlaceOrderSerial> call, Response<C_PlaceOrderSerial> response) {
                progressDoalog.dismiss();


                if (response.body() != null) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {

                            displayDialog();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(3000
                                        );
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    dialogMultiOrder.dismiss();
                                    startActivity(new Intent(activity, C_DashboardAct.class));
                                    finish();
                                    shared.setString(ConstantClass.CART_SIZE, "");
                                }
                            }).start();
                        } else {
                        }

                    }
                } else {

                }
            }


            @Override
            public void onFailure(Call<C_PlaceOrderSerial> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(activity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void displayDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View content = inflater.inflate(R.layout.c_custom_payment_done, null);
        builder.setView(content);

        dialogMultiOrder = builder.create();

        dialogMultiOrder.show();
        dialogMultiOrder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //setDataFromSharedPreferences();
    }
}
