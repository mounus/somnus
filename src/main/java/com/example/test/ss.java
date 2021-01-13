package com.example.test;


import com.alibaba.fastjson.JSONObject;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import javax.crypto.spec.SecretKeySpec;

import java.io.UnsupportedEncodingException;
import java.security.*;

import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
public class ss {

    /**
     * 获取信息
     */
    public static JSONObject getUserInfo(String encryptedData,String sessionkey,String iv){
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        System.out.println("dataByte__________"+dataByte);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionkey);
        System.out.println("keyByte__________"+keyByte);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        System.out.println("ivByte__________"+ivByte);
        String result=null;
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
              result = new String(resultByte, "UTF-8");
                System.out.println("_________"+JSONObject.parseObject(result));
                return JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("__________"+result);


        return JSONObject.parseObject(result);
    }






    @SneakyThrows
    public static void main(String[] args) {
//        String encryptedData="";
//        String iv="";
//        String sessionKey="";
        String encryptedData="";
        String iv="";
      String sessionKey="";
        JSONObject jsonObject=getUserInfo(encryptedData,sessionKey,iv);
        System.out.println("____________"+jsonObject.getString("phoneNumber"));



    }









}
