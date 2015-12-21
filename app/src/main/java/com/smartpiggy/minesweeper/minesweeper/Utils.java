package com.smartpiggy.minesweeper.minesweeper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
/**
 * Created by eyang on 12/20/15.
 */
//Utility class which provide some helper methods
public class Utils {
    public static void showDialog(Context context, String message){
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.app_name))
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static Drawable getDrawable(Context context, int id){
        int apiLevel = android.os.Build.VERSION.SDK_INT;
        if (apiLevel >= android.os.Build.VERSION_CODES.LOLLIPOP){
            return context.getResources().getDrawable(id, null);
        }else {
            return context.getResources().getDrawable(id);
        }
    }

    public static int getColor(Context context, int id) {
        int apiLevel = android.os.Build.VERSION.SDK_INT;
        if (apiLevel >= 23) {
            return context.getResources().getColor(id, null);
        } else {
            return context.getResources().getColor(id);
        }
    }
}
