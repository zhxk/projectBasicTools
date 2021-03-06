package com.ks.projectbasictools.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ks.projectbasictools.R;
import com.ks.projectbasictools.base.SimpleView;

/**
 * author：康少
 * date：2019/3/21
 * description：自定义Toast工具
 */
public class ToastKs {
    private static Toast toast;
    /**
     * 将Toast封装在一个方法中，在其它地方使用时直接输入要弹出的内容即可
     */
    public static void show(Context context, String messages) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        //LayoutInflater的作用：对于一个没有被载入或者想要动态载入的界面，都需要LayoutInflater.inflate()来载入，LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化
//        LayoutInflater inflater = context.getLayoutInflater();//调用Activity的getLayoutInflater()
//        View view = inflater.inflate(R.layout.toast_style, null); //加載layout下的布局
        SimpleView simpleView = new SimpleView(context, R.layout.toast_style);
        TextView text = simpleView.findViewById(R.id.tvTextToast);
        text.setText(messages); //toast内容
        if (toast == null) {
            toast = new Toast(context);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android:layout_gravity
        toast.setDuration(Toast.LENGTH_LONG);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
        toast.setView(simpleView); //添加视图文件
        toast.show();
    }
}
