package com.lirongyuns.happyrupees.window;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.lirongyuns.happyrupees.R;
import com.lirongyuns.happyrupees.annonation.ViewInjector;
import com.lirongyuns.happyrupees.utils.ViewUtil;

/**
 * popWindow滑动窗口
 */
public abstract class SlideWindow
{
    public static final int SHOW_TOP = 48;
    public static final int SHOW_LEFT = 3;
    public static final int SHOW_RIGHT = 5;
    public static final int SHOW_BOTTOM = 80;
    public static final int SHOW_CENTER = 17;

    private Activity activity;//绑定的Activity
    private View slideView;//slideView
    private int showType;//显示方式
    private PopupWindow popupWindow;//window

    public SlideWindow(Activity activity)
    {
        this(activity, SHOW_CENTER);
    }

    public SlideWindow(Activity activity, int showType)
    {
        this.activity = activity;
        this.showType = showType;
        initView();//初始化SizerView
        initPopWindow();//初始化PopWindow
        onCreate();
    }

    /**
     * 初始化视图
     */
    private void initView()
    {
        slideView = ViewInjector.contentView(this, LayoutInflater.from(activity), null);
        ViewInjector.inject(this, slideView);
    }

    /**
     * 初始化PopWindow
     */
    private void initPopWindow()
    {
        switch (showType)
        {
            case SHOW_TOP:
                int width = activity.getResources().getDisplayMetrics().widthPixels;//屏幕宽度
                popupWindow = new PopupWindow(slideView, width, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setAnimationStyle(R.style.pop_show_top);
                break;
            case SHOW_BOTTOM:
                int width2 = activity.getResources().getDisplayMetrics().widthPixels;//屏幕宽度
                popupWindow = new PopupWindow(slideView, width2, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setAnimationStyle(R.style.pop_show_bottom);
                break;
            case SHOW_LEFT:
                int height = activity.getResources().getDisplayMetrics().heightPixels;//屏幕高度
                int width_l = activity.getResources().getDisplayMetrics().widthPixels - ViewUtil.dip2px(getActivity(), 50);//屏幕宽度
                popupWindow = new PopupWindow(slideView, width_l, height);
                popupWindow.setAnimationStyle(R.style.pop_show_left);
                break;
            case SHOW_RIGHT:
                int height2 = activity.getResources().getDisplayMetrics().heightPixels;//屏幕高度
                int width_r = activity.getResources().getDisplayMetrics().widthPixels - ViewUtil.dip2px(getActivity(), 50);;//屏幕宽度
                popupWindow = new PopupWindow(slideView, width_r, height2);
                popupWindow.setAnimationStyle(R.style.pop_show_right);
                break;
            case SHOW_CENTER:
                int width3 = activity.getResources().getDisplayMetrics().widthPixels;//屏幕宽度
                popupWindow = new PopupWindow(slideView, width3, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setAnimationStyle(R.style.pop_show_center);
                break;

        }

        popupWindow.setFocusable(true);//否则外面的对象可点击
        ColorDrawable drawable = new ColorDrawable(-00000);
        popupWindow.setBackgroundDrawable(drawable);//背景透明
        //点击外部popueWindow消失
        popupWindow.setOutsideTouchable(true);
        //设置可以顶到状态栏与底部虚拟按键
        popupWindow.setClippingEnabled(false);
        //popupWindow消失屏幕变为不透明
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha = 1.0f;
                activity.getWindow().setAttributes(lp);
                onWindowDismiss();//window关闭给子类使用
            }
        });
    }

    /**
     * 获得Activity，Context
     * @return
     */
    public Activity getActivity()
    {
        return  activity;
    }

    /**
     * 显示window
     */
    public void show()
    {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.5f;
        activity.getWindow().setAttributes(lp);
        int offsetY = 0;
        if (hasNavigationBar()) {
            if ((showType!=SHOW_TOP&&showType!=SHOW_CENTER)) {
                offsetY = getNavigationBarHeight();
            }
        }

        popupWindow.showAtLocation(activity.getWindow().getDecorView(), showType, 0, offsetY);
    }

    /**
     * 获取虚拟功能键高度
     * @return
     */
    private int getNavigationBarHeight() {
        Resources resources = activity.getResources();
        int resourceId=resources.getIdentifier("navigation_bar_height","dimen","android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 获取是否有虚拟按键
     * 通过判断是否有物理返回键反向判断是否有虚拟按键
     */
    private boolean hasNavigationBar() {
        boolean hasMenuKey = ViewConfiguration.get(activity)
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);
        // 做任何你需要做的,这个设备有一个导航栏
        return !hasMenuKey & !hasBackKey;
    }

    /**
     * 显示window
     */
    public void showInRightContent(int offsetX, int offsetY)
    {
        popupWindow.showAtLocation(activity.getWindow().getDecorView(),
                Gravity.RIGHT|Gravity.TOP, 0, offsetY);
    }

    /**
     * 显示window
     */
    public void showInLeftContent(int offsetX, int offsetY)
    {
        popupWindow.showAtLocation(activity.getWindow().getDecorView(),
                Gravity.LEFT|Gravity.TOP, 0, offsetY);
    }

    /**
     * 关闭window
     */
    public void dismiss()
    {
        popupWindow.dismiss();
    }

    /**
     * 初始化数据
     */
    protected abstract void onCreate();


    protected void onWindowDismiss(){}
}

