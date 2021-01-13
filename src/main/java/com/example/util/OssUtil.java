package com.example.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import lombok.SneakyThrows;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import static com.example.util.BASE64.decryptBASE64;

public class OssUtil {

    static BASE64Encoder encoder = new sun.misc.BASE64Encoder();
    static BASE64Decoder decoder = new sun.misc.BASE64Decoder();

    /**
     * @description: 上传pdf到oss
     * @param: filePath 文件路径
     * @param: fileName 文件名称
     * @return: url pdf路径
     */
    @SneakyThrows
    public static String getOssUrl(String filePath, String fileName) {
        // endpoint是访问OSS的域名。如果您已经在OSS的控制台上 创建了Bucket，请在控制台上查看域名。
        // 如果您还没有创建Bucket，endpoint选择请参看文档中心的“开发人员指南 > 基本概念 > 访问域名”，
        // 链接地址是：https://help.aliyun.com/document_detail/oss/user_guide/oss_concept/endpoint.html?spm=5176.docoss/user_guide/endpoint_region
        // endpoint的格式形如“http://oss-cn-hangzhou.aliyuncs.com/”，注意http://后不带bucket名称，
        // 比如“http://bucket-name.oss-cn-hangzhou.aliyuncs.com”，是错误的endpoint，请去掉其中的“bucket-name”。
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";
        // accessKeyId和accessKeySecret是OSS的访问密钥，您可以在控制台上创建和查看，
        // 创建和查看访问密钥的链接地址是：https://ak-console.aliyun.com/#/。
        // 注意：accessKeyId和accessKeySecret前后都没有空格，从控制台复制时请检查并去除多余的空格。
        String byteKey = "TFRBSTRHNDU0RlBrVlFQMzR3UlB3TXpV";
        byte[] byteArrayKey = decryptBASE64(byteKey);
        String accessKeyId = new String(byteArrayKey);
        //加密的accessKeySecret
        String byteKeySecret = "S2YyY0JQUVlZa1JkTVlQVFhUbXNVd2RJcUJLdWdG";
        byte[] byteArray = decryptBASE64(byteKeySecret);
        String accessKeySecret = new String(byteArray);
        // Bucket用来管理所存储Object的存储空间，详细描述请参看“开发人员指南 > 基本概念 > OSS基本概念介绍”。
        // Bucket命名规范如下：只能包括小写字母，数字和短横线（-），必须以小写字母或者数字开头，长度必须在3-63字节之间。
        String bucketName = "haoyuexiang";
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 下载object到本地文件夹
        //ossClient.getObject(new GetObjectRequest(bucketName, "OSS上文件全路径，如：cia/merged.pdf"), new File("保存到本地的路径，如：E:/PDFfolder/merged.pdf"));
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String logintime = time.format(new Date());
        String key = "image/pdf/" + logintime + fileName;
        // 上传文件
        PutObjectResult object = ossClient.putObject(bucketName, key, new File(filePath));
        // 设置URL过期时间为1小时

        //判断指定文件是否存在
        // boolean found = ossClient.doesObjectExist(bucketName, "C:\\Users\\MLOONG\\Desktop\\刘芳.pdf");

        Date expiration = new Date(new Date().getTime() + 3600 * 10000);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);

        String newurl = url.toString().substring(0, url.toString().indexOf("?"));

        // 删除文件
        //ossClient.deleteObject(bucketName, "文件的全路径，如：cia/merged.pdf");
        // 关闭client
        ossClient.shutdown();
        return newurl;

    }

    /**
     * @description: 接受pdf的base64 在本地s
     * @param: base64sString pdf的base64
     * @param: fileName 文件名称
     * @return: url pdf路径
     */

    public static String base64StringToPDF(String base64sString, String fileName) {
        BufferedInputStream bin = null;
        FileOutputStream fout = null;
        BufferedOutputStream bout = null;
        String filePath = null;
        try {
            // 将base64编码的字符串解码成字节数组
            byte[] bytes = decoder.decodeBuffer(base64sString);
            // apache公司的API
            // byte[] bytes = Base64.decodeBase64(base64sString);
            // 创建一个将bytes作为其缓冲区的ByteArrayInputStream对象
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            // 创建从底层输入流中读取数据的缓冲输入流对象
            bin = new BufferedInputStream(bais);
            // 指定输出的文件
            String property = System.getProperty("user.dir");
            // filePath=property+"/src/main/resources/pdf";//windows 本机
            filePath = property + "/pdf";//linux 阿里云服务器
            File file = new File(filePath + "/" + fileName);
            // 创建到指定文件的输出流
            fout = new FileOutputStream(file);
            // 为文件输出流对接缓冲输出流对象
            bout = new BufferedOutputStream(fout);

            byte[] buffers = new byte[1024];
            int len = bin.read(buffers);
            while (len != -1) {
                bout.write(buffers, 0, len);
                len = bin.read(buffers);
            }
            // 刷新此输出流并强制写出所有缓冲的输出字节，必须这行代码，否则有可能有问题
            bout.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bin.close();
                fout.close();
                bout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }

    /**
     * 文件夹内文件删除
     *
     * @param dir
     * @return
     */
    public static boolean deleteDir(File dir) {
        if (!dir.exists())
            return false;
        if (dir.isDirectory()) {
            String[] childrens = dir.list();
            System.out.println(JSONObject.toJSON(childrens));
            // 递归删除目录中的子目录下
            for (String child : childrens) {
                // System.out.println(child);
                File file1 = new File(dir, child);
                file1.delete();
            }
        }
        // 目录此时为空，可以删除
        return true;
    }


}
