package com.atguigu.gulimall.commons.utils;

public class AppUtils {

    public static String arrayToStringWithSeprator(String[] arr,String sep){

        StringBuffer sb = new StringBuffer();
        if (arr != null && arr.length > 0){
            for (String s : arr) {
                sb.append(s);
                sb.append(",");
            }
        }
        String s = sb.toString().substring(0,sb.length()-1);

        return s;
    }
}
