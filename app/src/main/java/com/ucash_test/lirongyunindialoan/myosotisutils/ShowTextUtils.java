package com.ucash_test.lirongyunindialoan.myosotisutils;

import android.content.Context;
import android.widget.Toast;

public class ShowTextUtils {
    public static void show(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_LONG).show();
    }
}
