package com.wellgel.london.UtilClasses;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ConstantClass {
    public static String PROVIDE_PROFILE = "p_profile";
    public static String PROVIDE_EMAIL = "p_email";
    public static String PROVIDE_NAME = "p_name";
    public static String PROVIDE_NUMBER = "p_number";
    public static String PROVIDE_ADDRESS = "p_address";


    public static String isCustomerLoggeIn = "isCus";
    public static String isProviderLoggeIn = "isPro";
    public static String USER_NAME = "user_naem";
    public static String USER_Number = "USER_Number";
    public static String USER_Address = "USER_Address";
    public static String USER_Profile = "USER_Profile";
    public static String USER_Country = "USER_Country";
    public static String USER_Email = "USER_Email";
    public static boolean isProduct;
    public static boolean FromAddress = false;

    public static String ROLL_PLAY = "rollplay";
    public static String ROLL_CUSTOMER = "1";
    public static String ROLL_PROVIDER = "2";
    public static String SOCIAL_NAME = "";
    public static String SOCIAL_EMAIL = "";
    public static String SOCIAL_PASSWORD = "";
    public static String SOCIAL_PROFILE = "";
    public static String DEVICE_TOKEN = "";
    public static String DEVICETYPE = "android";

    //Appointment constant

    public static String NAIL_SHAPE = "";
    public static String NAIL_POlish_COLOR = "";
    public static String HAND_COLOR = "";
    public static String SALON_ID = "";
    public static String FIREBASE_TOKEN = "";
    public static String STATUS_REQUESTED = "requested";
    public static String STATUS_OPEN="Open";
    public static String STATUS_ACCEPTED="Accepted";
    public static String STATUS_REJECTED="Rejected";
    public static String STATUS_CANCEL="Cancel";
    public static String CART_SIZE="cart_size";


    public static void ListFunc(ArrayList<String> listSkinColor, ArrayList<String> listNailColor, ArrayList<String> listNailShape) {
        listNailColor.add(0, "#FFFFFF");
        listNailColor.add(1, "#CC66CC");
        listNailColor.add(2, "#333366");
        listNailColor.add(3, "#009999");
        listNailColor.add(4, "#CC00CC");
        listNailColor.add(5, "#0033FF");
        listNailColor.add(6, "#99FFFF");
        listNailColor.add(7, "#CCFF99");
        listNailColor.add(8, "#006633");
        listNailColor.add(9, "#CC9900");
        listNailColor.add(10, "#33FF00");
        listNailColor.add(11, "#669966");
        listNailColor.add(12, "#666666");
        listNailColor.add(13, "#00FFCC");
        listNailColor.add(14, "#993333");
        listNailColor.add(15, "#990099");
        listNailColor.add(16, "#9999FF");


        listSkinColor.add(0, "#F2D9B7");
        listSkinColor.add(1, "#EFB38A");
        listSkinColor.add(2, "#A07561");
        listSkinColor.add(3, "#795D4C");
        listSkinColor.add(4, "#3D2923");


        listNailShape.add(0, "square");
        listNailShape.add(1, "round");
        listNailShape.add(2, "oval");
        listNailShape.add(3, "stillete");
        listNailShape.add(4, "pointed");
    }
}
