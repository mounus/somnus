package com.example.sd;


import com.alibaba.fastjson.JSON;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.BasicCredentials;
import com.aliyuncs.auth.BasicSessionCredentials;
import com.aliyuncs.auth.STSAssumeRoleSessionCredentialsProvider;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesRequest;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.example.entiy.excel.MatornExcel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import jdk.nashorn.internal.objects.NativeDebug;
import lombok.*;
import net.sf.json.JSONArray;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;

import java.util.*;
import java.util.List;

import static com.example.util.BASE64.decryptBASE64;
import static com.example.util.HolidyUtil.getCount;
import static com.example.util.HolidyUtil.getNewEndtime;
import static com.example.util.MonthUtil.*;

public class ww {


    public static void main(String[] args) {

        //__________冒泡排序
        int[] ages = {21, 27, 31, 19, 50, 32, 16, 25};
        System.out.println(Arrays.toString(ages));
        //控制比较轮数
        for (int i = 1; i < ages.length; i++) {
            //每轮比较多少
            for (int j = 0; j < ages.length - i; j++) {
                if (ages[j] > ages[j + 1]) {
                    int tmp = 0;
                    tmp = ages[j];
                    ages[j] = ages[j + 1];
                    ages[j + 1] = tmp;
                }
            }
        }
        System.out.println(Arrays.toString(ages));
        //_______________________


    }


}
