package com.chuxin.xingguanjia;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity implements View.OnClickListener {
    String orderNum=null;
    EditText url,sign ;
    static TextView mText;
    static ScrollView scroll;
    public static String notify_action="com.chuxin.notify_action";

    TextView param ;
    Button save;
     BillReceived billReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        url=findViewById(R.id.notify_url);
        mText=findViewById(R.id.text);
        sign=findViewById(R.id.sign);
        param =findViewById(R.id.param);
        param.setText("post 参数 msg=&money=156&time=123459222&sign=sign&md5=md5(msg+msgid+time+sign)");
        save=findViewById(R.id.save);
        scroll=findViewById(R.id.scroll);
        save.setOnClickListener(this);
        updateView();

        //注册广播
        billReceived = new BillReceived();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.wxchat_notify");
        registerReceiver(billReceived, intentFilter);
    }


    public void updateView( ) {
        SharedPreferences preferences =getSharedPreferences("config",MODE_PRIVATE);
        url.setText(preferences.getString("url",""));
        sign.setText(preferences.getString("sign",""));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(billReceived!=null){
            unregisterReceiver(billReceived);
        }
    }




    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(url.getText())){
            Toast.makeText(this, "请填写回调地址", Toast.LENGTH_SHORT).show();
            return;
        }
        String urlStr =url.getText().toString();
        String signStr =sign.getText().toString();
        SharedPreferences preferences =getSharedPreferences("config",MODE_PRIVATE);
         SharedPreferences.Editor edit =preferences.edit();
         edit.putString("url",urlStr);
         edit.putString("sign",signStr);
         edit.commit();
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
    }

    //自定义接受订单通知广播
    class BillReceived extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("msg");
            sendmsg(msg);
        }
    }
    public static Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            String txt=msg.getData().getString("log");
            if(mText!=null){
                if(mText.getText()!=null){
                    if(mText.getText().toString().length()>10000){
                        mText.setText("日志定时清理完成..."+"\n\n"+txt);
                    }else{
                        mText.setText(txt+"\n\n"+ mText.getText().toString());
                        scroll.scrollTo(0, 0);
                    }

                }else{
                    mText.setText(txt);
                    scroll.scrollTo(0, 0);
                }
            }
            super.handleMessage(msg);
        }

    };
    public static void sendmsg(String txt) {
        Message msg = new Message();
        msg.what = 1;
        Bundle data = new Bundle();
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = dateFormat.format(date);
        data.putString("log", d + ":" + "  结果:" + txt);
        msg.setData(data);
        try {
            handler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
