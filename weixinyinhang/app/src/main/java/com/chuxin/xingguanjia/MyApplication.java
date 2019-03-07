package com.chuxin.xingguanjia;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyApplication extends Application {
    private static RequestQueue mRequestQueue;
    @Override
    public void onCreate() {
        super.onCreate();
        //  EMChat.getInstance().init(this);
        mRequestQueue = Volley.newRequestQueue(this);

    }



    /**
     * debugMode == true 时为打开，sdk 会在log里输入调试信息
     * 在做代码混淆的时候需要设置成false
     */
    //  EMChat.getInstance().setDebugMode(true);//在做打包混淆时，要关闭debug模式，避免消耗不必要的资源
    public static void addQueue(Request request, Context context) {
        if (mRequestQueue != null) {
            mRequestQueue.add(request);
        } else {
            mRequestQueue = Volley.newRequestQueue(context);
            mRequestQueue.add(request);
        }
    }

    public String getMetaInfo(String name) {
        try {
            return this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA).metaData.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
