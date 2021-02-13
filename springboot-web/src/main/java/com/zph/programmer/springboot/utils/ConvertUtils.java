package com.zph.programmer.springboot.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 * @Author: zengpenghui
 * @Date: 2019/5/5 18:18
 * @Version 1.0
 */
public class ConvertUtils {
    public static String getStringValue(Object str, String defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        if (str instanceof String) {
            return (String) str;
        }

        if (str instanceof Integer) {
            return str.toString();
        }
        if (str instanceof Date) {
            return str.toString();
        }
        if (str instanceof Long) {
            return str.toString();
        }
        if (str instanceof LocalDateTime) {
            return ((LocalDateTime) str).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (str instanceof Double) {
            return String.valueOf(((Double) str).intValue());
        }
        if (str instanceof BigDecimal) {
            return ((BigDecimal) str).toPlainString();
        }
        return defaultValue;
    }

    public static String doubleToString(Object object, String defaultValue) {

        if (object instanceof Double) {
            Double number = (Double) object;
            DecimalFormat df = new DecimalFormat("0");
            return df.format(number);
        } else {
            return defaultValue;
        }

    }

    public static String getStringValue2(Object str, String defaultValue) {
        String str2 = getStringValue(str, defaultValue);
        if (StringUtils.isNotBlank(str2)) {
            str2 = str2.replaceAll("(\r\n|\r|\n|\n\r)", "").trim();
        }
        return str2;
    }

    public static Date getDateValue(Object day, Date defaultValue) {
        Calendar c = new GregorianCalendar(1900, Calendar.JANUARY, 0); //excel当中日期是距离1900年1月1日的天数
        Date d = c.getTime();
        if (day != null) {
            Double date = (Double) day;
            return DateUtils.addDays(d, date.intValue() - 1);
        } else {
            return defaultValue;
        }
    }

    public static Double getDoubleValue(Object num, Double defaultValue) {
        if (num instanceof Double) {
            return (Double) num;
        } else {
            return defaultValue;
        }
    }

    //length用户要求产生字符串的长度
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Double day = 43544.0;
        //Wed Mar 20 00:00:00 CST 2019
        Date date = getDateValue(day, null);
        System.out.println(date);
    }


    public static String replaceHttpHead(String url) {
        if (StringUtils.isBlank(url)) {
            return url;
        }
        if (url.startsWith("http:")) {
            return url.replace("http:", "");
        }
        if (url.startsWith("https:")) {
            return url.replace("https:", "");
        }
        return url;
    }

}
