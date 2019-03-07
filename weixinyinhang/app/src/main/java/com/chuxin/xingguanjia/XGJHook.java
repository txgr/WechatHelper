package com.chuxin.xingguanjia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

class XGJHook {

    public static ClassLoader mClassLoader;

    public XGJHook(ClassLoader classLoader) {
        mClassLoader=classLoader;
    }

    public void hook() {
        XposedHelpers.findAndHookMethod("com.newland.satrpos.starposmanager.receiver.JMessageReceiver", mClassLoader, "onReceive", Context.class, Intent.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
               Intent intent = (Intent) param.args[1];
               Context context = (Context) param.args[0];
                if (intent!=null){
                    XposedBridge.log("inetent action "+intent.getAction());
                  Bundle bundle = intent.getExtras();
                  if (bundle!=null){
                      for (String key:bundle.keySet()){
                          XposedBridge.log("key "+key+"value "+bundle.get(key));
                      }

                  }
                  if (intent.getAction().equals("cn.jpush.android.intent.NOTIFICATION_RECEIVED")){
                    String msg =intent.getStringExtra("cn.jpush.android.EXTRA");
                    String msgid=intent.getStringExtra("cn.jpush.android.MSG_ID");
                    Intent notify =new Intent("com.chuxin.xingguanjia_notify");
                    notify.putExtra("msg",msg);
                    notify.putExtra("msgid",msgid);
                    context.sendBroadcast(notify);
                  }
                }

            }
        });

    }
}
