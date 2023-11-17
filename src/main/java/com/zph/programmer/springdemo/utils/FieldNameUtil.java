package com.zph.programmer.springdemo.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class FieldNameUtil {
    private static final Pattern linePattern = Pattern.compile("_(\\w)");
    private static final Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 下划线转驼峰
     */
    public static String lineToHump(String str) {
        if (!str.contains("_")) {
            return str;
        }
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰转下划线(简单写法})
     */
    public static String humpToLine(String str) {
        return str.replaceAll("[A-Z]", "_$0").toLowerCase();
    }

    /**
     * 驼峰转下划线
     */
    public static String humpToLine2(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


    /**
     * 首字母大写转换
     *
     * @param field
     * @return
     */
    public static String firstUpperCase(String field) {
        if (StringUtils.isNotBlank(field)) {
            char[] cs = field.toCharArray();
            cs[0] -= 32;
            return String.valueOf(cs);
        } else {
            return field;
        }
    }

    public static Object getFieldValueByFieldName(String fieldName, Object object) {
        try {
            Field field = object.getClass().getDeclaredField(FieldNameUtil.lineToHump(fieldName));
            //对private的属性的访问
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            log.error("Exception:", e);
            return null;
        }
    }


    public static void main(String[] args) {
        //String colName1 = lineToHump("cashpbocdpst");
        String colName = lineToHump("cash_pboc_dpst");
        /**
         * cashPbocDpst
         * cash_pboc_dpst
         * cash_pboc_dpst
         */
        System.out.println(colName);
        System.out.println(humpToLine(colName));
        System.out.println(humpToLine2(colName));

        String tableName = lineToHump("a1_gen_corp_bal_sheet");
        /**
         * A1GenCorpBalSheet
         */
        System.out.println(firstUpperCase(tableName) + "Mapper");

        System.out.println(new BigDecimal("3.6787E11").stripTrailingZeros().toPlainString());


    }
}
