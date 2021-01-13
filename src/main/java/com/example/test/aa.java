package com.example.test;

import lombok.SneakyThrows;
import org.apache.commons.collections.list.AbstractLinkedList;
import org.apache.jasper.compiler.JspUtil;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.util.BASE64.decryptBASE64;
import static com.example.util.BASE64.encryptBASE64;
import static com.example.util.MonthUtil.*;


public class aa {


    /**
     * BASE64解密
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * BASE64加密
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }


    @SneakyThrows
    public static void main(String[] args) {
       String aa="";
     String data=   Base64.getUrlEncoder().encodeToString(aa.getBytes());

       // String data = encryptBASE64(aa.getBytes());
        System.out.println("sun.misc.BASE64 加密后：" + data);

       // String newData=data.replace("-","+");
       // String newData1=data.replace("_","/");
        System.out.println("newData____________"+data);




      byte[] byteArray = decryptBASE64(data);
       // byte[] byteArray=  Base64.getDecoder().decode(data);

        System.out.println("sun.misc.BASE64 解密后：" + new String(byteArray));

    }
}
