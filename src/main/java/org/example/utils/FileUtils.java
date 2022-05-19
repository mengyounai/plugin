package org.example.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class FileUtils {

    //读取图片地址并存储
    public static File imageFileSave(String imageurl) {
        File imageFile = null;
        int index = imageurl.lastIndexOf("/");
        int end = imageurl.lastIndexOf("g");
        String imgName = imageurl.substring(index + 1, end + 1);
        try {
            long start1 = System.currentTimeMillis();
            //定义一个URL对象，就是你想下载的图片的URL地址
            URL url = new URL(imageurl);
            //打开连接
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
            conn.setRequestProperty("User-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36");
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为10秒
            conn.setConnectTimeout(10 * 1000);
            //通过输入流获取图片数据
            InputStream is = conn.getInputStream();
            long end1 = System.currentTimeMillis();
            System.out.println("读取数据所花的时间：" + (end1 - start1));
            long start2 = System.currentTimeMillis();
//            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            byte[] data = readInputStream(is);
            //创建一个文件对象用来保存图片，默认保存当前工程根目录，起名叫H.jpg
            imageFile = new File("./images/" + imgName);
            //创建输出流
            FileOutputStream outStream = new FileOutputStream(imageFile);
            //写入数据
            outStream.write(data);
            //关闭输出流，释放资源
            outStream.close();
            long end2 = System.currentTimeMillis();
            System.out.println("存取数据所花的时间：" + (end2 - start2));
        } catch (Exception e) {
            System.out.println("获取图片出错");
        }
        return imageFile;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[6024 * 10];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }
}
