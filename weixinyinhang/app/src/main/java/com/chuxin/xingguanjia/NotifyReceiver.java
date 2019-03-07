package com.chuxin.xingguanjia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class NotifyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        if ("com.chuxin.xingguanjia_notify".equals(intent.getAction())){
            String msg =intent.getStringExtra("msg");
            String talker=intent.getStringExtra("talker");
            SharedPreferences preferences =context.getSharedPreferences("config",Context.MODE_PRIVATE);
            String url =(preferences.getString("url",""));
            String sign=(preferences.getString("sign",""));
            String time =System.currentTimeMillis()+"";
            String md5 =ByteUtil.MD5(msg+talker+time+sign);

            final Map<String,String> map =new HashMap<>();
            map.put("msg",msg);
            map.put("talker",talker);
            map.put("time",time);
            map.put("sign",sign);
            map.put("md5",md5);
            Toast.makeText(context, "上传通知"+url+ map, Toast.LENGTH_SHORT).show();
            if (TextUtils.isEmpty(url) ) return;
            StringRequest request =new StringRequest(Request.Method.POST, url.trim(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(context, "上传成功"+response, Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "上传失败 "+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    return map;
                }
            };
            MyApplication.addQueue(request,context);
        }
    }
}
