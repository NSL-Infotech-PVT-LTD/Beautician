package com.wellgel.london.UtilClasses;

import android.content.Context;
import android.content.DialogInterface;

public class AlertClas {
    public static androidx.appcompat.app.AlertDialog alertDialog(String text, Context activity) {
        return new androidx.appcompat.app.AlertDialog.Builder(activity)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("!Oops")
                .setMessage(text)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }

                })
                .show();
    }

}
