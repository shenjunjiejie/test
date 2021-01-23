package com.lirongyuns.happyrupees.utils;

import android.app.Activity;
import android.view.View;

import com.lirongyuns.happyrupees.annonation.Click;
import com.lirongyuns.happyrupees.annonation.SetActivity;
import com.lirongyuns.happyrupees.annonation.SetView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AnnotationUtils {

    public static void inject(final Object object){
        try {
            if(object instanceof Activity){
                inject((Activity) object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void inject(final Activity activity) throws Exception {
        if(activity.getClass().isAnnotationPresent(SetActivity.class)){
            SetActivity setActivity = activity.getClass().getAnnotation(SetActivity.class);
            int id = setActivity.value();
            if(id < 0){
                throw new Exception(activity.getClass().getName()+"注入失败，id不能为空");
            }
            else{
                activity.setContentView(id);
                injectViewFromActivity(activity);
                injectClickFromActivity(activity);
            }
        }
    }

    private static void injectViewFromActivity(Activity activity) throws Exception {
        Field[] fields = activity.getClass().getDeclaredFields();
        View v = null;
        for(Field field : fields) {
            if(field.isAnnotationPresent(SetView.class)){
                SetView setView = field.getAnnotation(SetView.class);
                int id = setView.value();
                if(id < 0) {
                    throw new Exception(field.getType().getSimpleName()+" "+field.getName()+"注入失败，id不能为空");
                }
                else{
                    field.setAccessible(true);
                    v = activity.findViewById(id);
                }
                field.set(activity,v);

            }
        }
    }

    private static void injectClickFromActivity(Activity activity) throws Exception {
        View v = null;
        Method[] methods = activity.getClass().getDeclaredMethods();
        for(Method method : methods) {
            if(method.isAnnotationPresent(Click.class)){
                Click click = method.getAnnotation(Click.class);
                for(int id : click.value()){
                    if(id < 0){
                        throw new Exception(method.getName()+"注入失败，id不能为空");
                    }
                    else{
                        method.setAccessible(true);
                        v = activity.findViewById(id);
                        v.setOnClickListener((view)-> {
                            try {
                                method.invoke(activity,view);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
//                int id = click.value()[0];


            }
        }
    }

//    private void findRequester(Activity activity) throws Exception{
//        Method[] methods = activity.getClass().getDeclaredMethods();
//        for(Method method : methods) {
//            if(method.isAnnotationPresent(POST.class)){
//                POST post = method.getAnnotation(POST.class);
//                String path = post.value();
//                if(post.equals("")){
//                    throw new Exception(method.getName()+"注入失败，path不能为空");
//                }
//                else{
//                    method.setAccessible(true);
//
////                    v = activity.findViewById(post);
////                    v.setOnClickListener((view)-> {
////                        try {
////                            method.invoke(activity,view);
////                        } catch (IllegalAccessException e) {
////                            e.printStackTrace();
////                        } catch (InvocationTargetException e) {
////                            e.printStackTrace();
////                        }
////                    });
//                }
//            }
//        }

//    }

//    private static void findView(Object holder, Object view, Class<?> holderClass) {
//        if (holderClass == null) holderClass = holder.getClass();
//        Field[] fields = holderClass.getDeclaredFields();
//
//        for (Field field : fields) {
//            try {
//                if (field.isAnnotationPresent(SetView.class)) {
//                    SetView bindView = field.getAnnotation(SetView.class);
//                    field.setAccessible(true);
//                    if (view instanceof View) {
////                        LogUtil.i("View ID = "+bindView.value());
//                        View mView  = ((View) view).findViewById(bindView.value());
////                        LogUtil.i("View"+(mView==null?"==":"!=")+"null");
//                        field.set(holder, mView);
//                    }
//                    if (view instanceof Activity) {
////                        LogUtil.i("View ID = "+bindView.value());
//                        View mView = ((Activity) view).findViewById(bindView.value());
////                        LogUtil.i("View "+(mView==null?"==":"!=")+" null");
//                        field.set(holder, mView);
//                    }
//                }
//            } catch (IllegalAccessException e) {
////                LogUtil.e("fail to findView");
//                e.printStackTrace();
//            }
//        }
//
//        Class<?> superClass = holderClass.getSuperclass();
//        if (superClass == null) return;
//        if (!superClass.getName().contains("Activity")) return;
//        if (superClass.getName().equals("android.support.v7.app.AppCompatActivity")) return;
//        //递归查找，非Activity的holder不支持递归查找
//        findView(holder, view, superClass);
//    }

//    private static void bindClickEvent(Object holder, Object view, Class<?> holderClass) {
//        if (holderClass == null) holderClass = holder.getClass();
//        Method[] methods = holderClass.getDeclaredMethods();
//        for (Method method : methods) {
//            if (method.isAnnotationPresent(Click.class)) {
//                Click click = method.getAnnotation(Click.class);
//                int[] idArray = click.value();
//                if (idArray.length>0) {
//                    View.OnClickListener clickListener = v -> {
//                        try {
//                            method.setAccessible(true);
//                            method.invoke(holder, v);
//                        } catch (IllegalAccessException | InvocationTargetException e) {
//                            e.printStackTrace();
//                        }
//                    };
//                    for (int id : idArray) {
//                        if (view instanceof View) {
//                            ((View) view).findViewById(id).setOnClickListener(clickListener);;
//                        }
//                        if (view instanceof Activity) {
//                            ((Activity) view).findViewById(id).setOnClickListener(clickListener);;
//                        }
//                    }
//                }
//            }
//        }
//
//        Class<?> superClass = holderClass.getSuperclass();
//        if (superClass == null) return;
//        if (!superClass.getName().contains("Activity")) return;
//        if (superClass.getName().equals("android.support.v7.app.AppCompatActivity")) return;
//        //递归查找，非Activity的holder不支持递归查找
//        bindClickEvent(holder, view, superClass);
//    }

//    public static void inject(Object holder, View view) {
//        Class<?> holderClass = holder.getClass();
//        findView(holder, view, null);
//        bindClickEvent(holder, view, null);
//    }
}
