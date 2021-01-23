package com.lirongyuns.happyrupees.presenter;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

/**
 * Tooltip
 * @author Krear 2018/8/15
 */
public class Alert
{
    /**
     * 弹出提示框
     * @param context
     * @param msg
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
