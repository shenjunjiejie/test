package com.lirongyuns.happyrupees.annonation;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * View注解器
 */
public class ViewInjector
{

    public static void inject(Activity activity) {
        Class<?> activityClass = activity.getClass();
        View view = contentView(activity, activity.getLayoutInflater(), null);
        if (view != null) {
            activity.setContentView(view);
            findView(activity, activity, null);
            bindClickEvent(activity, activity, null);
        }
    }

    /**
     * 注入view holder
     * @param holder 视图持有者
     * @param view 视图
     */
    public static void inject(Object holder, View view) {
        Class<?> holderClass = holder.getClass();
        findView(holder, view, null);
        bindClickEvent(holder, view, null);
    }

    public static View contentView(Object holder, LayoutInflater inflater, ViewGroup container) {
        ContentView contentView = (ContentView) getAnnotation(holder.getClass(), ContentView.class);
        View view = null;
        if (contentView!=null) {
            int resourceId = contentView.value();
            view = inflater.inflate(resourceId, container, false);
        }

        return view;
    }

    /**
     * 获取ContentView的Annotation
     * 递归查找，适应多层嵌套组装View注解
     * 只有 {@link androidx.appcompat.app.AppCompatActivity} 的子类可以递归
     * 其他holder只找一层
     * @param holderClass 视图持有者Class
     * @return {@link ContentView} 对象，用于获取布局id
     */
    public static <A extends Annotation> A getAnnotation(Class<?> holderClass, Class<A> aClass) {
        A a = holderClass.getAnnotation(aClass);
        if (a != null) return a;
        Class<?> superClass = holderClass.getSuperclass();
        if (superClass == null) return null;
        if (!superClass.getName().contains("Activity")) return null;
        if (superClass.getName().equals("android.support.v7.app.AppCompatActivity")) return null;
        return getAnnotation(superClass, aClass);
    }

    /**
     * 控件绑定
     * @param holder 视图持有者
     * @param view 视图
     * @param holderClass 持有者的实际class，可能为holder的父类
     */
    private static void findView(Object holder, Object view, Class<?> holderClass) {
        if (holderClass == null) holderClass = holder.getClass();
        Field[] fields = holderClass.getDeclaredFields();

        for (Field field : fields) {
            try {
                if (field.isAnnotationPresent(FindView.class)) {
                    FindView bindView = field.getAnnotation(FindView.class);
                    field.setAccessible(true);
                    if (view instanceof View) {
                        View mView  = ((View) view).findViewById(bindView.value());
                        field.set(holder, mView);
                    }
                    if (view instanceof Activity) {
                        View mView = ((Activity) view).findViewById(bindView.value());
                        field.set(holder, mView);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        Class<?> superClass = holderClass.getSuperclass();
        if (superClass == null) return;
        if (!superClass.getName().contains("Activity")) return;
        if (superClass.getName().equals("android.support.v7.app.AppCompatActivity")) return;
        //递归查找，非Activity的holder不支持递归查找
        findView(holder, view, superClass);
    }

    /**
     * 点击事件绑定
     * @param holder 视图持有者
     * @param view 视图
     * @param holderClass 持有者的实际class，可能为holder的父类
     */
    private static void bindClickEvent(Object holder, Object view, Class<?> holderClass) {
        if (holderClass == null) holderClass = holder.getClass();
        Method[] methods = holderClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Click.class)) {
                Click click = method.getAnnotation(Click.class);
                int[] idArray = click.value();
                if (idArray.length>0) {
                    View.OnClickListener clickListener = v -> {
                        try {
                            method.setAccessible(true);
                            method.invoke(holder, v);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    };
                    for (int id : idArray) {
                        if (view instanceof View) {
                            ((View) view).findViewById(id).setOnClickListener(clickListener);;
                        }
                        if (view instanceof Activity) {
                            ((Activity) view).findViewById(id).setOnClickListener(clickListener);;
                        }
                    }
                }
            }
        }

        Class<?> superClass = holderClass.getSuperclass();
        if (superClass == null) return;
        if (!superClass.getName().contains("Activity")) return;
        if (superClass.getName().equals("android.support.v7.app.AppCompatActivity")) return;
        //递归查找，非Activity的holder不支持递归查找
        bindClickEvent(holder, view, superClass);
    }

}
