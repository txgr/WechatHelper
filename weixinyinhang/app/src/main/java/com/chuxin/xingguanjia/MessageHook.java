package com.chuxin.xingguanjia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by Administrator on 2017/11/30.
 */

 class MessageHook extends XC_MethodHook {
private ClassLoader classLoader;
    private String path;

    Activity activity;
    public MessageHook(Activity activity,ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.activity= activity;
    }

    @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        if (param.args.length == 2 && param.args[1] instanceof Boolean) {
            Boolean isSelf = (Boolean) param.args[1];

            Object o = param.args[0];
            final long msgId = (long) XposedHelpers.getObjectField(o, "field_msgId");

            int type = (int) XposedHelpers.getObjectField(o, "field_type");

            String talker = (String) XposedHelpers.getObjectField(o, "field_talker");


            String msg = (String) XposedHelpers.getObjectField(o, "field_content");
            XposedBridge.log(" talke "+talker+" type "+type);
            if (!(type==318767153||type==1))return;
            XposedBridge.log(type+""+msg);
            JSONObject msgObj =null;
            try {
                msgObj=new XmlToJson.Builder(msg).build().getJSONObject("msg");
            }catch (Exception e){
            }
            if (msgObj==null){
                XposedBridge.log("msgObj"+msgObj);
                return;
            }
            XposedBridge.log(msgObj.toString());
            JSONObject appMsg =msgObj.getJSONObject("appmsg");
            String title =appMsg.getString("title");
            XposedBridge.log(" title ====>"+title);
            if (title.contains("交易提醒")||title.contains("账户变动提醒")){

          /*      JSONObject mmreader = appMsg.getJSONObject("mmreader");
                JSONObject category = mmreader.getJSONObject("category");
                JSONObject item = category.getJSONObject("item");
                String des =item.getString("digest");*/
                String des =appMsg.getString("des");
                String[] list =des.split("\n" );

                XposedBridge.log(" 发送通知 "+des+"\n"+"--talker:"+talker);
                Intent notify =new Intent("com.wxchat_notify");
                notify.putExtra("msg",des);
                activity.sendBroadcast(notify);

                Intent notify2 =new Intent("com.chuxin.xingguanjia_notify");
                notify2.putExtra("msg",des);
                notify2.putExtra("talker",talker);
                activity.sendBroadcast(notify2);
            //    XposedBridge.log(" receiver "+des);


 /*               Map<String,String> map =new HashMap();
                XposedBridge.log(" talker ====>"+talker);
                //gh_4e38a8ec5dda 浦发银行
                if (talker.contains("gh_557414c66e4a")){
                    map.put("bank","中国农业银行");
                    for (String s:list) {
                        if (s.contains("尾号为")) {
                            map.put("bankCard", s.substring(s.indexOf("尾号为") + 3, +s.indexOf("尾号为") + 7));
                        }
                        if (s.contains("账号类型：")) {
                            map.put("accountType", s.split("：")[1].trim());
                        }
                        if (s.contains("交易时间：")) {
                            map.put("time", s.split("：")[1].trim());
                        }
                        if (s.contains("交易金额：")) {
                            map.put("money", s.split("：")[1].trim());
                        }
                        if (s.contains("交易类型：")) {
                            map.put("amount", s.split("：")[1].trim());
                        }
                        if (s.contains("可用余额：")) {
                            map.put("type", s.split("：")[1].trim());
                        }
                    }
                }else if(talker.contains("gh_a977c8acfae7")){
                    map.put("bank","平安银行");

                    for (String s:list) {
                        if (s.contains("您尾号")) {
                            map.put("bankCard", s.substring(s.indexOf("您尾号") + 3, +s.indexOf("您尾号") + 7));
                        }
                        if (s.contains("类型：")) {
                            map.put("accountType", s.split("：")[1].trim());
                        }
                        if (s.contains("时间：")) {
                            map.put("time", s.split("：")[1].trim());
                        }
                        if (s.contains("金额：")) {
                            map.put("money", s.split("：")[1].trim());
                        }
                    }
                }
                XposedBridge.log(" 发送通知 "+new Gson().toJson(map));
                XposedBridge.log(" map "+map);
                Intent notify =new Intent("com.wxchat_notify");
                notify.putExtra("msg",new Gson().toJson(map));
                activity.sendBroadcast(notify);

                Intent notify2 =new Intent("com.chuxin.xingguanjia_notify");
                notify2.putExtra("msg",des);
                notify2.putExtra("money",map.get("money"));
                activity.sendBroadcast(notify2);
                */
 /*           if (title.contains("交易提醒")||title.contains("账户变动提醒")){

                String des =appMsg.getString("des");
                String[] list =des.split("\n" );
                Map<String,String> map =new HashMap();
                XposedBridge.log(" talker ====>"+talker);

                if (talker.contains("gh_557414c66e4a")){
                    map.put("bank","中国农业银行");
                    for (String s:list) {
                        if (s.contains("尾号为")) {
                            map.put("bankCard", s.substring(s.indexOf("尾号为") + 3, +s.indexOf("尾号为") + 7));
                        }
                        if (s.contains("账号类型：")) {
                            map.put("accountType", s.split("：")[1].trim());
                        }
                        if (s.contains("交易时间：")) {
                            map.put("time", s.split("：")[1].trim());
                        }
                        if (s.contains("交易金额：")) {
                            map.put("money", s.split("：")[1].trim());
                        }
                        if (s.contains("交易类型：")) {
                            map.put("amount", s.split("：")[1].trim());
                        }
                        if (s.contains("可用余额：")) {
                            map.put("type", s.split("：")[1].trim());
                        }
                    }
                }else if(talker.contains("gh_a977c8acfae7")){
                    map.put("bank","平安银行");

                    for (String s:list) {
                        if (s.contains("您尾号")) {
                            map.put("bankCard", s.substring(s.indexOf("您尾号") + 3, +s.indexOf("您尾号") + 7));
                        }
                        if (s.contains("类型：")) {
                            map.put("accountType", s.split("：")[1].trim());
                        }
                        if (s.contains("时间：")) {
                            map.put("time", s.split("：")[1].trim());
                        }
                        if (s.contains("金额：")) {
                            map.put("money", s.split("：")[1].trim());
                        }
                    }
                }
                XposedBridge.log(" 发送通知 "+new Gson().toJson(map));
                XposedBridge.log(" map "+map);
                Intent notify =new Intent("com.wxchat_notify");
                notify.putExtra("msg",new Gson().toJson(map));
                activity.sendBroadcast(notify);

                Intent notify2 =new Intent("com.chuxin.xingguanjia_notify");
                notify2.putExtra("msg",des);
                notify2.putExtra("money",map.get("money"));
                activity.sendBroadcast(notify2);*/
            }

        }
    }
    public Context getContext() {
        if (MainHook.activity==null){
            Object activityThread = callStaticMethod(findClass("android.app.ActivityThread", null), "currentActivityThread");
            Context mNotificationContext = (Context) callMethod(activityThread, "getSystemContext");
            return mNotificationContext;
        }
        return  MainHook.activity;
    }
}
