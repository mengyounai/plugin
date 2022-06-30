package org.example.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.ktor.http.Url;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;


public class JsonUtil {

    public static String doGet(String imageurl, String charset) throws IOException {

        //定义一个URL对象，就是你想下载的图片的URL地址
        URL url = new URL(imageurl);
        //读取为String
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String s2 = reader.readLine();
        return null;

    }


    /*
     * 接口地址：http://api.tianapi.com/tiangou/index
     * 请求示例：http://api.tianapi.com/tiangou/index?key=APIKEY
     * 返回格式：utf-8 json
     */

    /**
     * 舔狗日记
     */
    public static String getDogDiary() {
//        String url = "http://api.tianapi.com/wanan/index?key=c0d01b7943b1714ecf44398d2c4187c3";
        String url = "http://api.tianapi.com/tiangou/index?key=d726756945d0793c447a40a7ea530b45";
        String common = common(url);
        return common;
    }

    /**
     * 接口地址：http://api.tianapi.com/wanan/index
     * 请求示例：http://api.tianapi.com/wanan/index?key=APIKEY
     * 返回格式：utf-8 json
     */

    /**
     * 晚安心语
     */
    public static String getEvening() {
        String url = "http://api.tianapi.com/wanan/index?key=c0d01b7943b1714ecf44398d2c4187c3";
        String common = common(url);
        return common;
    }

    /**
     * 通用代码
     */
    public static String common(String url) {
        String s2 = "";
        URL oracle=null;
        InputStream is=null;
        try {
            //实例一个url和URLConnection
            oracle = new URL(url);
            //打开链接
            HttpURLConnection yc = (HttpURLConnection) oracle.openConnection();
            yc.setRequestMethod("GET");
            yc.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            //输入流作参数传进InputStreamReader并用BufferedReader接受
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(oracle.openStream()));
            s2 = reader.readLine();
            //记得关闭连接
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject object = (JSONObject) JSONObject.parse(s2);

        System.out.println(object);
        JSONArray newslist1 = object.getJSONArray("newslist");
        JSONObject newslist2 = object.getJSONArray("newslist").getJSONObject(0);
        String s = newslist2.getString("content");

//        System.out.println("日记内容为:" + s);
        return s;
    }


    public static void main(String[] args) {
        String url = "http://api.tianapi.com/wanan/index?key=c0d01b7943b1714ecf44398d2c4187c3";
        String evening = getEvening();
        System.out.println(evening);
        int[] arr = new int[]{1, 2, 7, 4, 5, 8, 7, 3, 9, 6};
        //快速排序
        quickSort(arr, 0, arr.length - 1);
        //遍历数组
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }

//        //类型转化 string转jsonObject 拿到这个json
//        JSONObject object = (JSONObject) JSONObject.parse(body);
//
//        JSONArray records=object.getJSONObject("data").getJSONArray("records");
//        System.out.println(records);
        /*
//        [
//		      {
//		        "orderNo": "ord_010_20220111111111111",
//		        "deleted": 0,
//		        "type": 1
//		      },
//		      {
//		        "orderNo": "ord_010_20220222222222222",
//		        "deleted": 1,
//		        "type": 2
//		      }
//      	]
//      	*/
//
//        JSONObject object0=records.getJSONObject(0);
//        System.out.println(object0);
//		/*
//		{
//			 "orderNo": "ord_010_20220111111111111",
//			 "deleted": 0,
//			 "type": 1
//		}
//		*/
//
//        String orderNo=(String)object0.get("orderNo") ;
//        System.out.println(orderNo);
//        //ord_010_20220111111111111

    }

    private static void quickSort(int[] arr, int i, int i1) {
        if (i < i1) {
            int j = partition(arr, i, i1);
            quickSort(arr, i, j - 1);
            quickSort(arr, j + 1, i1);
        }
    }

    private static int partition(int[] arr, int i, int i1) {
        int pivot = arr[i];
        int j = i;
        for (int k = i + 1; k <= i1; k++) {
            if (arr[k] < pivot) {
                j++;
                swap(arr, j, k);
            }
        }
        swap(arr, i, j);
        return j;
    }

    private static void swap(int[] arr, int j, int k) {
        int temp = arr[j];
        arr[j] = arr[k];
        arr[k] = temp;
    }

}
