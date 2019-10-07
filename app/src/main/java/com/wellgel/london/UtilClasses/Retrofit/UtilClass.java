package com.wellgel.london.UtilClasses.Retrofit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.IllegalFormatException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class UtilClass {
    public static UtilClass instance;

    public static UtilClass getInstance() {
        if (instance == null) {
            instance = new UtilClass();
        }
        return instance;
    }

    public static long getDifferenceInHours(long startTime, long endTime) {
        long msDiff = endTime - startTime;
        long hoursDiff = (TimeUnit.MILLISECONDS.toHours(msDiff));

        return hoursDiff;
    }

    /*function to convert the date string*/
    public static String convertDateToString(String dateStr, String formatFrom, String formatTo) {
        String formatedDateStr;
        try {
            if (dateStr != null || !dateStr.equalsIgnoreCase("")) {
                SimpleDateFormat sdf = new SimpleDateFormat(formatFrom, Locale.getDefault());
                Date testDate = null;
                testDate = sdf.parse(dateStr);
                SimpleDateFormat formatter = new SimpleDateFormat(formatTo,Locale.getDefault());
                String newFormat = formatter.format(testDate);
                return newFormat;
            } else
                return null;
        } catch (IllegalFormatException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
