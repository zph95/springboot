package com.zph.programmer.springboot.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class MiscUtils {
    private static final Random rand = new Random();

    public static String randStr(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int num = rand.nextInt(62);
            buf.append(str.charAt(num));
        }
        return buf.toString();
    }

    public static int randInt(int min, int max) {
        return rand.nextInt(max - min + 1) + min;
    }

    public static String joinStr(String join, String... strings) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; ++i) {
            if (i == strings.length - 1) {
                sb.append(strings[i]);
            } else {
                sb.append(strings[i]).append(join);
            }
        }
        return sb.toString();
    }



    public static String formatTime(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    public static String formatTime(long time, String format) {
        return new SimpleDateFormat(format).format(new Timestamp(time));
    }

    public static String randIp() {
        return joinStr(".", String.valueOf(randInt(1, 223)),
                String.valueOf(randInt(1, 254)),
                String.valueOf(randInt(1, 254)),
                String.valueOf(randInt(0, 255)));
    }

    public static String randMac() {
        byte[] macAddress = new byte[6];

        rand.nextBytes(macAddress);
        macAddress[0] = (byte) (macAddress[0] & (byte) 254);

        StringBuilder sb = new StringBuilder(18);
        for (byte b : macAddress) {
            if (sb.length() > 0) {
                sb.append(":");
            }
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    public static String genUniqueID() {
        return DigestUtils.sha1Hex(System.currentTimeMillis()
                + randStr(randInt(16, 32)));
    }

    public static void sleepInSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return 形如 yyyyMMddHHmmssSSS-Z0000019558195832297 的(38位)保证唯一的递增的序列号字符串，
     * 主要用于数据库的主键，方便基于时间点的跨数据库的异步数据同步。
     * 前半部分是currentTimeMillis，后半部分是nanoTime（正数）补齐20位的字符串，
     * 如果通过System.nanoTime()获取的是负数，则通过nanoTime = nanoTime+Long.MAX_VALUE+1;
     * 转化为正数或零。
     */
    public static String getTimeMillisSequence() {
//        long nanoTime = System.nanoTime();
        // 生成32位递增字符串
//        String preFix="";
//        if (nanoTime<0){
//            preFix="A";//负数补位A保证负数排在正数Z前面,解决正负临界值(如A9223372036854775807至Z0000000000000000000)问题。
//            nanoTime = nanoTime+Long.MAX_VALUE+1;
//        }else{
//            preFix="Z";
//        }
//        String nanoTimeStr = String.valueOf(nanoTime);

//        int difBit=String.valueOf(Long.MAX_VALUE).length()-nanoTimeStr.length();
//        for (int i=0;i<difBit;i++){
//            preFix = preFix+"0";
//        }
//        nanoTimeStr = preFix+nanoTimeStr;
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS"); //24小时制
        String timeMillisSequence = sdf.format(System.currentTimeMillis());
//		String timeMillisSequence=new Random().nextInt(1000000000)+1000000+"";

        return timeMillisSequence;
    }

}
