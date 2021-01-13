package com.example.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class ExcelUtil {

    public static HSSFWorkbook createExcel( String sheetName,List<String> titleList, List dataList) throws IllegalAccessException {


        //创建HSSFWorkbook对象
        HSSFWorkbook wb = new HSSFWorkbook();
        //创建sheet对象
        HSSFSheet sheet=wb.createSheet(sheetName);
        //在sheet里创建第一行，这里即是表头
        HSSFRow rowTitle=sheet.createRow(0);



        //写入表头的每一个列
        for (int i = 0; i < titleList.size(); i++) {
            //创建单元格
            rowTitle.createCell(i).setCellValue(titleList.get(i));
        }

        //写入每一行的记录
        for (int i = 0; i < dataList.size(); i++) {
            //创建新的一行，递增
            HSSFRow rowData = sheet.createRow(i+1);
            //通过反射，获取POJO对象
            Class cl = dataList.get(i).getClass();
            //获取类的所有字段
            Field[] fields = cl.getDeclaredFields();

           for (int j = 0; j < fields.length; j++) {//所有字段
           // for (int j = 0; j < dataList.size(); j++) {//通过表头字段长度控制每一行字段的长度
                //设置字段可见，否则会报错，禁止访问
                fields[j].setAccessible(true);
                //创建单元格
               rowData.createCell(j).setCellValue((String) fields[j].get(dataList.get(i)));
               //字段全是String
            }
        }
        return wb;

    }


    //链接url下载图片
    public static void downloadPicture(String urlList, String path) {
        URL url = null;
        try {
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}


