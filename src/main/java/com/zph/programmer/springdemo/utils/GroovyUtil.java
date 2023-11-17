package com.zph.programmer.springdemo.utils;


import com.google.common.collect.Maps;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.runtime.InvokerHelper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author songshuaijin
 * @date 2020/1/9
 */
@Slf4j
public class GroovyUtil {

    private static final Map<String, String> FUNC_MAP = Maps.newHashMap();
    static {
        FUNC_MAP.put("min",null);
        FUNC_MAP.put("max",null);
        FUNC_MAP.put("avg",null);
        FUNC_MAP.put("mean",null);
        FUNC_MAP.put("sum",null);
        FUNC_MAP.put("lowQuartile",null);
        FUNC_MAP.put("highQuartile",null);
        FUNC_MAP.put("std",null);
        FUNC_MAP.put("var",null);
    }

    private static String handelScript(String expression) {
        expression = expression.replace("≥", ">>");
        expression = expression.replace("≤", "<<");
        expression = expression.replace(">=", ">>");
        expression = expression.replace("<=", "<<");
        expression = expression.replace("!=", "<>");

        expression = expression.replace("=", "==");
        expression = expression.replace("^", " ** ");
        expression = expression.replace("<>", " != ");
        expression = expression.replace(">>", ">=");
        expression = expression.replace("<<", "<=");
        expression = expression.replace(" and ", " && ");
        expression = expression.replace(" or ", " || ");
        expression = expression.replace("not(", "!(");
        expression = expression.replace("if(", "ifCondition(");
        return expression;
    }




    public static Object getResultBySimpleExpression(String expression) throws Exception{
        expression = handelScript(expression);
        Binding binding = new Binding();
        CompilerConfiguration cfg = new CompilerConfiguration();
        cfg.setScriptBaseClass(GroovyScript.class.getName());
        GroovyShell shell = new GroovyShell(cfg);
        Script script = shell.parse(expression);
        Object object = InvokerHelper.createScript(script.getClass(), binding).run();
        //运行完，记得将内部的缓存清理
        shell.getClassLoader().clearCache();
        return object;
    }


    public static String getExpressString2(List<String> expressionList){
        StringBuilder sb = new StringBuilder();
        String head ="def te_%1$s(){  try { %2$s } catch(Exception ex) { return 0; }}; alist.add(te_%1$s());";

        sb.append("List<Object> alist = new ArrayList<Object>();");
        sb.append("def groovy_express_list = [];");
        for(int i=0;i<expressionList.size() ;i++){
            sb.append(String.format(head, i, expressionList.get(i)));
        }
        sb.append("return alist;");
        return sb.toString();
    }

    public static String getExpressString(List<String> expressionList){
        StringBuilder sb = new StringBuilder();
        //此处可根据极值处理动态生成
        String head ="def te_%1$s(){  try { %2$s } catch(Exception ex) { return 'RESULT_ERROR'; }}; groovy_express_list.add(te_%1$s());";
        sb.append("def groovy_express_list = [];");
        for(int i=0;i<expressionList.size() ;i++){
            sb.append(String.format(head, i, expressionList.get(i)));
        }
        sb.append("return groovy_express_list;");
        return sb.toString();
    }

    /**
     * 1. 获得字符串中指定开始和结束字符串中间的所有可识别的表达式
     * <%([\s\S]+?)%>
     *
     * @param str 字符串
     * @param s   开始 开始与结束现支持 <% %>  与 [ ] ; 如果需自定义其他格式，需检查是否有特殊字符，另加 \\ 处理
     * @param e   结束
     * @return list
     */
    public static List<String> getAllRuleExpressions(String str, String s, String e) {
        String regex = String.format("\\%s([\\s\\S]+?)\\%s", s, e);
        Pattern pattern = Pattern.compile(regex);

        List<String> list = new ArrayList<>();
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            // group(0)或group()将会返回整个匹配的字符串（完全匹配）；group(i)则会返回与分组i匹配的字符
            // group(0) 为 s+content+e, group(1) 为第一组，即中间部分
            String content = matcher.group(1);
            list.add(content);
        }
        return list;
    }


    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }

    public static void main(String[] args) {
        try {
            System.out.println(getResultBySimpleExpression("1+2>2 and min([1.0,2.0,3.0])>0"));
        }catch (Exception e){
            log.error("exception: ", e);
        }

    }

}
