package com.lirongyuns.happyrupees.view;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class Alert
{
    /**
     * 弹出提示框
     */
    public static void show(Context context, String msg)
    {
        new AlertDialog.Builder(context)
                .setTitle("Message")
                .setCancelable(false)
                .setMessage(msg)
                .setNegativeButton("OK", null).show();
    }

    /**
     * 确认提示框
     */
    public static class Confirm
    {
        public static void show(Context context, String msg, DialogInterface.OnClickListener listener)
        {
            new AlertDialog.Builder(context)
                    .setTitle("Message")
                    .setCancelable(false)
                    .setMessage(msg)
                    .setNegativeButton("cancel", null)
                    .setPositiveButton("confirm", listener)
                    .show();
        }
    }
}
