package com.zph.programmer.springboot.utils;

import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 */
@Slf4j
public class GroovyScript extends Script {
    @Override
    public Object run() {
        Method[] methods = GroovyScript.class.getDeclaredMethods();
        StringBuilder sb = new StringBuilder();
        for (Method method : methods) {
            sb.append(method);
        }
        return sb.substring(0, sb.length() - 1);
    }


    public static Object ifCondition(boolean condition, Object trueResult, Object falseResult) {
        return condition ? trueResult : falseResult;
    }

    public static int find(String search, String target) {
        return target.indexOf(search)+1;
    }//注意find页面说明 0表示没找到

    public static boolean include(String target, String search){
        String[] texts = search.split(";");
        for(String s : texts){
            if(StringUtils.isNotBlank(target) && target.equals(s)){
                return true;
            }
        }
        return false;

    }

    public static boolean notinclude(String target, String search){
        String[] texts = search.split(";");
        for(String s : texts){
            if(StringUtils.isNotBlank(target) && target.equals(s)){
                return false;
            }
        }
        return true;
    }

    public static boolean intext(String target, String... texts) {
        for(String t :texts){
            if(target.equals(t)){
                return true;
            }
        }
        return false;
    }

    //public static Double abs(Double number) {
    //    return Math.abs(number);
    //}
    public static BigDecimal abs(BigDecimal number) {
        if(number ==null){
            return null;
        }
        return number.abs();
    }

    public static BigDecimal sqr(BigDecimal number) {
        if(number ==null){
            return null;
        }
        return number.pow(2);
    }
    public static BigDecimal round(BigDecimal number,Integer num) {
        if(number ==null){
            return null;
        }
        if(num ==null){
            num = 0;
        }
        return number.setScale(num, BigDecimal.ROUND_HALF_UP);
    }
    public static BigDecimal sqrtBigDecimal(BigDecimal number) {
        if(number ==null){
            return null;
        }
        if(number.compareTo(BigDecimal.ZERO)<0){
            return null;
        }
        BigDecimal num2 = BigDecimal.valueOf(2);
        int precision = 100;
        MathContext mc = new MathContext(precision, RoundingMode.HALF_UP);
        BigDecimal deviation = number;
        int cnt = 0;
        while (cnt < precision) {
            deviation = (deviation.add(number.divide(deviation, mc))).divide(num2, mc);
            cnt++;
        }
        deviation = deviation.setScale(2, BigDecimal.ROUND_HALF_UP);
        return deviation;
    }

    public static Double sqrt(BigDecimal number) {
        if(number ==null){
            return null;
        }
        if(number.compareTo(BigDecimal.ZERO)<0){
            return null;
        }
        DecimalFormat df=new DecimalFormat(".##");
        return Double.valueOf(df.format(Math.sqrt(number.doubleValue())));
    }
    public static Double log(BigDecimal number,BigDecimal base) {
        if(number ==null || base == null){
            return null;
        }
        if(number.compareTo(BigDecimal.ZERO)<=0){
            return null;
        }
        if(base.compareTo(BigDecimal.ZERO)<=0){
            return null;
        }
        DecimalFormat df=new DecimalFormat(".##");
        return Double.valueOf(df.format(Math.log(number.doubleValue()) / Math.log(base.doubleValue())));
    }

    public static Double log10(BigDecimal number) {
        if(number ==null){
            return null;
        }
        if(number.compareTo(BigDecimal.ZERO)<=0){
            return null;
        }
        DecimalFormat df=new DecimalFormat(".##");
        return Double.valueOf(df.format(Math.log10(number.doubleValue())));
    }

    public static Double ln(BigDecimal number) {
        if(number ==null){
            return null;
        }
        if(number.compareTo(BigDecimal.ZERO)<0){
            return null;
        }
        DecimalFormat df=new DecimalFormat(".##");
        return Double.valueOf(df.format(Math.log(number.doubleValue())));
    }

    public static Double exp(BigDecimal number) {
        if(number ==null){
            return null;
        }
        DecimalFormat df=new DecimalFormat(".##");
        return Double.valueOf(df.format(Math.exp(number.doubleValue())));
    }

    public static Double sin(BigDecimal number) {
        if(number ==null){
            return null;
        }
        DecimalFormat df=new DecimalFormat(".##");
        return Double.valueOf(df.format(Math.sin(Math.toRadians(number.doubleValue()))));
    }

    public static Double cos(BigDecimal number) {
        if(number ==null){
            return null;
        }
        DecimalFormat df=new DecimalFormat(".##");
        return Double.valueOf(df.format(Math.cos(Math.toRadians(number.doubleValue()))));
    }

    public static Double tan(BigDecimal number) {
        if(number ==null){
            return null;
        }
        DecimalFormat df=new DecimalFormat(".##");
        return Double.valueOf(df.format(Math.tan(Math.toRadians(number.doubleValue()))));
    }

    public static Double cot(BigDecimal number) {
        if(number ==null){
            return null;
        }
        if(number.compareTo(BigDecimal.ZERO)==0){
            return null;
        }
        DecimalFormat df=new DecimalFormat(".##");
        return Double.valueOf(df.format(Math.cos(Math.toRadians(number.doubleValue()))/Math.sin(Math.toRadians(number.doubleValue()))));
    }
    public static String dateadd(String dateString, String interval, int number) {
        if (StringUtils.isBlank(dateString)) {
            return null;
        }
        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyyMMdd"));
        switch (interval) {
            case "y":
                date = date.plus(number, ChronoUnit.YEARS);
                break;
            case "q":
                date = date.plus((long) number * 3L, ChronoUnit.MONTHS);
                break;
            case "m":
                date = date.plus(number, ChronoUnit.MONTHS);
                break;
            case "w":
                date = date.plus(number, ChronoUnit.WEEKS);
                break;
            case "d":
                date = date.plus(number, ChronoUnit.DAYS);
                break;
            default: return null;

        }
        return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static BigDecimal min(List<BigDecimal> list){
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
        return Collections.min(list);
    }
    public static BigDecimal max(List<BigDecimal> list){
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
        return Collections.max(list);
    }
    public static BigDecimal sum(List<BigDecimal> list){
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
        return list.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public static BigDecimal avg(List<BigDecimal> list){
        log.info("求平均值：{} ", list);
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
        return list.stream().reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(list.size()), 2,
               RoundingMode.HALF_UP);
    }
    /*
      中位数
     */
    public static BigDecimal mean(List<BigDecimal> list){
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
        Collections.sort(list);
        int size = list.size();
        if(size % 2 == 1){
            return list.get((size-1)/2);
        }else {
            return  (list.get(size/2-1).add(list.get(size/2))).divide(BigDecimal.valueOf(2),2, BigDecimal.ROUND_HALF_UP);
        }

    }
    public static BigDecimal mean(BigDecimal num){
        return num;
    }
    public static BigDecimal avg(BigDecimal num){
        return num;
    }
    public static BigDecimal min(BigDecimal num){
        return num;
    }
    public static BigDecimal max(BigDecimal num){
        return num;
    }
    public static BigDecimal var(BigDecimal num){
        return num;
    }
    public static BigDecimal std(BigDecimal num){
        return num;
    }
    public static BigDecimal lowQuartile(BigDecimal num){
        return num;
    }
    public static BigDecimal highQuartile(BigDecimal num){
        return num;
    }

    /*
      方差
     */
    public static BigDecimal var(List<BigDecimal> list){
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
        BigDecimal average = list.stream().reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(list.size()), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal sum = BigDecimal.ZERO;
        for(BigDecimal num :list){
            sum = sum.add(((num.subtract(average)).multiply(num.subtract(average))));
        }
        return sum.divide(BigDecimal.valueOf(list.size()), 2, BigDecimal.ROUND_HALF_UP);

    }

    /*
      标准差
     */
    public static Double std(List<BigDecimal> list){
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
        BigDecimal num = var(list);
        if(num!=null) {
            DecimalFormat df = new DecimalFormat(".##");
            return Double.valueOf(df.format(Math.sqrt(num.doubleValue())));
        }
        else{
            return null;
        }
    }

    /*
     1/4 Quartile 从小到大
    */
    public static BigDecimal lowQuartile(List<BigDecimal> list){
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
        Collections.sort(list);
        int len = list.size();
        if(len==1){
            return list.get(0);
        }
        BigDecimal num = BigDecimal.valueOf(1).add(BigDecimal.valueOf(len).subtract(BigDecimal.ONE).multiply(BigDecimal.valueOf(0.25)));
        int index = num.intValue();
        return list.get(index-1).add((num.subtract(BigDecimal.valueOf(index))).multiply(list.get(index).subtract(list.get(index-1))));

    }


    /*
     3/4 Quartile 从小到大
    */
    public static BigDecimal highQuartile(List<BigDecimal> list){
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
        Collections.sort(list);
        int len = list.size();
        if(len==1){
            return list.get(0);
        }
        BigDecimal num = BigDecimal.valueOf(1).add(BigDecimal.valueOf(len).subtract(BigDecimal.ONE).multiply(BigDecimal.valueOf(0.75)));
        int index = num.intValue();
        return list.get(index-1).add((num.subtract(BigDecimal.valueOf(index))).multiply(list.get(index).subtract(list.get(index-1))));

    }



    public static Object original(Object object) {
        return object;
    }

    public static void main(String[] args) {
        List<BigDecimal> list = new ArrayList<>();
        list.add(null);
        list.add(BigDecimal.valueOf(2));
        String a="32232";
        System.out.println(a.replace("(",""));

        System.out.println(Math.tan(30));
        System.out.println(log10(BigDecimal.valueOf(3)));
        System.out.println(ln(BigDecimal.valueOf(3)));
        System.out.println(dateadd("20191231","d",-1));
    }
}
