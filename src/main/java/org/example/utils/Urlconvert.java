package org.example.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName AsciiConvertUtil
 * @Description
 * @Author xinsen.liao
 * @Date 2020/6/10  16:13
 */
public class Urlconvert {

    public static String urlconvert(String tag) throws UnsupportedEncodingException {
        String encodeStr = URLEncoder.encode(tag,"UTF-8");
//        System.out.println(encodeStr);
//        System.out.println(URLDecoder.decode(encodeStr,"UTF-8"));
        return encodeStr;
    }


    public static void main(String[] args) throws UnsupportedEncodingException {

        String s = "金发";
        urlconvert(s);
    }


}