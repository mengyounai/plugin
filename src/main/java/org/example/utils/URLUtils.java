package org.example.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

public class URLUtils {

    public static String getImageUrl(String imageurl) {
        String res = "";
        String s = "";
        try {
            System.out.println("获取图片地址中......");
            //定义一个URL对象，就是你想下载的图片的URL地址
            URL url = new URL(imageurl);
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
            //读取为String
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            s = reader.readLine();
            if (s.contains("https")) {
                //转换成JSON格式数据
                JSONObject object = (JSONObject) JSONObject.parse(s);
                //按照数据结构获得想获得的数据
                res = object.getJSONArray("data")
                        .getJSONObject(0)
                        .getJSONObject("urls")
                        .getString("original");
            } else {
                System.out.println("没有此tag的涩图");
            }
            //关闭流
            reader.close();
        } catch (IOException e) {
            System.out.println("获取图片地址出错,转向新涩图地址");
            try {
                URL url = new URL("https://api.nyan.xyz/httpapi/sexphoto/");
                System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
                //读取为String
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                s = reader.readLine();
                if (s.contains("https")) {
                    //转换成JSON格式数据
                    JSONObject object = (JSONObject) JSONObject.parse(s);
                    //按照数据结构获得想获得的数据
                    JSONObject data = object.getJSONObject("data");
                    res = data.getString("url");
                    res =res.substring(2,res.length()-2);
                } 
            } catch (MalformedURLException malformedURLException) {
                System.out.println("新涩图地址获取失败");
                malformedURLException.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return res;
        }
        return res;
    }


    public static InputStream accessImageUrl(String imageurl) {
        //定义一个URL对象，就是你想下载的图片的URL地址
        URL url = null;
        InputStream is = null;
        try {
            System.out.println("访问图片地址中......");
            url = new URL(imageurl);
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
            //打开连接
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
            conn.setRequestProperty("User-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36");
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为10秒
            conn.setConnectTimeout(15 * 1000);
            //通过输入流获取图片数据
            System.out.println("获取图片流......");
            is = conn.getInputStream();
            System.out.println("获取图片流结束......");
        } catch (MalformedURLException | ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    public static void main(String[] args) {
        String url = "https://api.lolicon.app/setu/v2";
        String data = getImageUrl(url);

        //转换成JSON格式数据
        JSONObject object = (JSONObject) JSONObject.parse(data);

        //按照数据结构获得想获得的数据
        String urlres = object.getJSONArray("data")
                .getJSONObject(0)
                .getJSONObject("urls")
                .getString("original");

        System.out.println(data);
        System.out.println(urlres);

    }
}
