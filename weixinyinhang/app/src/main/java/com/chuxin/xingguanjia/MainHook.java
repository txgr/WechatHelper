package com.chuxin.xingguanjia;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Keep;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
/**
 * Created by Administrator on 2017/12/26.
 */

@Keep
public class MainHook implements IXposedHookLoadPackage {
    public static Activity activity;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName!=null&&"com.newland.satrpos.starposmanager".equals(loadPackageParam.packageName)){
            new XGJHook(loadPackageParam.classLoader).hook();
        }

        if ("com.tencent.mm".equals(loadPackageParam.packageName)) {

            try {
                XposedHelpers.findAndHookMethod(Activity.class, "onCreate", Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        if (param.thisObject.getClass().toString().contains("com.tencent.mm.ui.LauncherUI")) {

                            activity = (Activity) param.thisObject;
                            ClassLoader appClassLoader = activity.getClassLoader();


                            CommonUtil.log("handleLoadPackage: " + loadPackageParam.packageName);
                            try {
                                XposedBridge.hookAllMethods(XposedHelpers.findClass("com.tencent.mm.storage.be", loadPackageParam.classLoader)
                                        , "b", new MessageHook(activity,loadPackageParam.classLoader));
                            } catch (XposedHelpers.ClassNotFoundError e) {
                                CommonUtil.log(e.getMessage());
                            }

                            try {
                                XposedBridge.log("微信Hook成功，当前微信版本:" + getVerName(activity));


                            } catch (XposedHelpers.ClassNotFoundError e) {
                                CommonUtil.log(e.getMessage());
                            }

                        }
                    }
                });
            } catch (Error e) {
                CommonUtil.log(e.getMessage());
            }
        }
    }

        /**
         * 获取版本号名称
         *
         * @param context
         *            上下文
         * @return
         */
        public static String getVerName(Context context) {
            String verName = "";
            try {
                verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
               XposedBridge.log(e);
            }
            return verName;
        }
}
