package com.ucash_test.lirongyunindialoan.annonation;

import android.graphics.Color;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Title {
    int[] value() default -1;
    String title() default "";
    int textColor() default Color.WHITE;
    int bgcolor() default Color.BLUE;
    int backColor() default Color.WHITE;
}
