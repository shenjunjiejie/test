package com.lirongyuns.happyrupees.utils;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ViewUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 获取状态栏高度
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    /**
     * 获取虚拟功能键高度
     * @param context
     * @return
     */
    public static int getVirtualBarHeight(Context context) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }

    /**
     * 获取屏幕宽
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager manager = ((Activity)context).getWindowManager();
        manager.getDefaultDisplay().getMetrics(dm);

        return dm.widthPixels;
    }

    /**
     * 获取屏幕高
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager manager = ((Activity)context).getWindowManager();
        manager.getDefaultDisplay().getMetrics(dm);

        return dm.heightPixels;
    }

    /**
     * 添加验证码倒计时机制
     * @param view
     * @return
     */
    public static CountDownTimer addCodeCountDownTimer(final TextView view)
    {
        CountDownTimer timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                view.setText((millisUntilFinished/1000)+"s");
            }

            @Override
            public void onFinish() {
                view.setEnabled(true);
                view.setText("发送验证码");
            }
        };

        return timer;
    }

    /**
     * 问控件添加阴影效果
     * @param context
     * @param v
     */
    public static void addShadow(Context context, View v)
    {
        addShadow(context, v, dip2px(context, 5), dip2px(context,5));
    }

    /**
     * 添加阴影效果
     * @param context
     * @param v
     * @param radius
     * @param corner
     */
    public static void addShadow(Context context, View v, float radius, float corner)
    {
//        new CrazyShadow.Builder()
//                .setContext(context)
//                .setDirection(CrazyShadowDirection.ALL)
//                .setShadowRadius(radius)
//                .setCorner(corner)
//                .setBackground(Color.parseColor("#ffffff"))
//                .setImpl(CrazyShadow.IMPL_DRAW)
//                .action(v);

    }

    /**
     * 设置SwipeRefreshLoadLayout加载颜色
     */
//    public static void setSwipeColorScheme(SwipeRefreshLayout layout)
//    {
//        layout.setColorSchemeResources(R.color.sort_first,R.color.sort_third,
//                R.color.mainColor, R.color.sort_second);
//    }


    /**
     * 为控件添加InputSelector
     * @param view
     * @param textViews
     */
    public static void addInputSelector(final View view, final TextView... textViews)
    {
        TextWatcher watcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean isEnable = true;
                for (TextView editText : textViews)
                {
                    Object tag = editText.getTag();
                    String tagStr = tag==null ? "" : (String) tag;
                    String editString = editText.getText().toString();
                    isEnable = parserTag(editString, tagStr);
                    if (!isEnable)
                        break;
                }
                view.setEnabled(isEnable);
            }

            /**
             * 解析tag内容
             * @param tagString
             * @return
             */
            private boolean parserTag(String editString, String tagString)
            {
                if (editString.equals(""))
                    return false;

                if (tagString.equals(""))
                    return true;

                if (tagString.indexOf(">=")==0)
                {
                    String temp = tagString.replace(">=", "");
                    int length = Integer.parseInt(temp);
                    if (editString.length()>=length)
                        return true;
                    else
                        return false;
                }
                else if (tagString.indexOf(">")==0)
                {
                    String temp = tagString.replace(">", "");
                    int length = Integer.parseInt(temp);
                    if (editString.length()>length)
                        return true;
                    else
                        return false;
                }
                else if (tagString.indexOf("=")==0)
                {
                    String temp = tagString.replace("=", "");
                    int length = Integer.parseInt(temp);
                    if (editString.length()==length)
                        return true;
                    else
                        return false;
                }

                //其他tag
                return true;
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };

        for (TextView editText : textViews)
        {
            editText.addTextChangedListener(watcher);
        }
    }

    /**
     * 视图显示隐藏的切换器
     * 通常与单选按钮一起使用
     * @param <T>
     */
    static class ViewDisplaySwitcher<T extends View>
    {
        private List<T> viewList;

        public  ViewDisplaySwitcher()
        {
            viewList = new ArrayList<>();
        }

        public void put(T view)
        {
            viewList.add(view);
        }

        public void switchTo(T view)
        {
            for (T v :viewList)
            {
                if (v.equals(view))
                    v.setVisibility(View.VISIBLE);
                else
                {
                    if(v.getVisibility()== View.VISIBLE)
                        v.setVisibility(View.GONE);
                }
            }
        }
    }

}
