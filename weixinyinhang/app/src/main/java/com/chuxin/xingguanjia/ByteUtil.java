package com.chuxin.xingguanjia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;

/**
 * Created by yupeiquan on
 */
public class ByteUtil {


    public static String MD5(byte[] strTemp) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte b = md[i];
                str[k++] = hexDigits[b >> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {

            return null;
        }
    }
    public static String MD5(String strTemp) {
        return MD5(strTemp.getBytes());
    }

    public static String MD5(File file) {
        return MD5(readFormFile(file.getAbsolutePath()));
    }

    public static byte[] readFormFile(String filepath){
        File file=new File(filepath);
        if (!file.exists()){
            throw new NullPointerException("读取 文件为空");
        }
        try {
            FileInputStream fis =new FileInputStream(file);
            byte[] buff =new byte[fis.available()];
            fis.read(buff);
            fis.close();
            return buff;
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        return null;
    }
}
