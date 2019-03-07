package com.chuxin.xingguanjia;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import de.robv.android.xposed.XposedBridge;


public class CommonUtil {
	
	private static final String FILEPATH = "/xpLog.txt";
	private static final String CONFIGFILEPATH= Environment.getExternalStorageDirectory()+"/shouqianba/file";
    private static boolean debug=false;

	 
	public static void writeStringtoFile(String str){

		File file = new File(Environment.getExternalStorageDirectory() + FILEPATH);

		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
                XposedBridge.log(e);
			}
		}
		try {
			FileOutputStream ops =new FileOutputStream(file,true);
			ops.write(str.getBytes());
			ops.flush();
			ops.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
            XposedBridge.log(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
            XposedBridge.log(e);
		}
		
	}
		 
         public  static  boolean checkItem(List<String> list, String s){
			 if(s==null) return false;
			 for (String str:list){
				 if(str.equals(s)){
					 return true;
				 }
			 }
			 return false;
		 }


	public static void writeConfigtoFile(String name , String value){
		try {
			File file =new File(CONFIGFILEPATH+ File.separator+name+".con");
			if (!file.exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			FileOutputStream ops =new FileOutputStream(file,false);
			ops.write(value.getBytes());
			ops.flush();
			ops.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		CommonUtil.log(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			CommonUtil.log(e.getMessage());
		}
	}

	public static void log(String message) {
	 	SimpleDateFormat dateFormat =new SimpleDateFormat( "yy-MM-DD hh:mm:ss");
        message =dateFormat.format(new Date())+" : "+message +"\r\n";
		XposedBridge.log(message);
	    writeStringtoFile(message);
	}

	public static String readConfigFromFile(String name , String defaul){
		try {
			File file =new File(CONFIGFILEPATH+ File.separator+name+".con");
			if (!file.exists()){
				return defaul;
			}
			FileInputStream fin = new FileInputStream(file);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			defaul =new String(buffer, "UTF-8");
			fin.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			CommonUtil.log(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			CommonUtil.log(e.getMessage());
		}
		return defaul;
	}

	/**
	 * 当前运行的activity
	 * @param context
	 * @return
     */
	public   static String getRunningActivityName(Context context){
		ActivityManager activityManager=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
		return runningActivity;
	}

    public static void log(Exception error) {
		log(" error"+error.getMessage()+error.getCause());
	}


    /**
	 * 字符串 SHA 加密
	 *
	 * @param strText
	 * @return
	 */
	private String SHA(final String strText)
	{
		// 返回值
		String strResult = null;

		// 是否是有效字符串
		if (strText != null && strText.length() > 0)
		{
			try
			{
				// SHA 加密开始
				// 创建加密对象 并傳入加密類型
				MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
				// 传入要加密的字符串
				messageDigest.update(strText.getBytes());
				// 得到 byte 類型结果
				byte byteBuffer[] = messageDigest.digest();

				// 將 byte 轉換爲 string
				StringBuffer strHexString = new StringBuffer();
				// 遍歷 byte buffer
				for (int i = 0; i < byteBuffer.length; i++)
				{
					String hex = Integer.toHexString(0xff & byteBuffer[i]);
					if (hex.length() == 1)
					{
						strHexString.append('0');
					}
					strHexString.append(hex);
				}
				// 得到返回結果
				strResult = strHexString.toString();
			}
			catch (NoSuchAlgorithmException e)
			{
				e.printStackTrace();
			}
		}

		return strResult;
	}

	public static void copyFolder(File newFile, File oldFile ) {
		try {
			FileOutputStream fos =new FileOutputStream(newFile);
			FileInputStream fis =new FileInputStream(oldFile);
			byte[] buff =new byte[100*1024];
			int length=0;
			while ((length=fis.read(buff))!=-1){
				fos.write(buff,0,length);
				fos.flush();
			}
			fis.close();
			buff=null;
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}



    /**
     *
     * @param i 秒
     */
    public static void sleep(int i) {
		Random ran =new Random();
        for (int j=i;j>0;j--){
            sleep1000ms(ran);
        }
    }

    private static int   sleep1000ms(Random ran) {
      int i=0;
        SystemClock.sleep(200);
		CommonUtil.log(i+"");
        i+= ran.nextInt();
        SystemClock.sleep(200);
        i+= ran.nextInt();
        SystemClock.sleep(200);
		CommonUtil.log(i+"");
        i+= ran.nextInt();
        SystemClock.sleep(200);
        i+= ran.nextInt();
		CommonUtil.log(i+"");
        SystemClock.sleep(200);
        i+= ran.nextInt();
        return i;
    }

 

	public static String getBase64String(File file) {

		try {
			FileInputStream inStream =new FileInputStream(file);
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while( (len=inStream.read(buffer)) != -1){
				outStream.write(buffer, 0, len);
			}
			outStream.close();
			inStream.close();
			buffer =outStream.toByteArray();
		 return new String(Base64.encode(buffer,Base64.DEFAULT),"utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
      return null;
	}
 
}

