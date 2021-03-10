package com.zph.programmer.springboot.utils;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.time.LocalDate;
import java.util.List;

@Slf4j
public class MybatisGeneratorPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        //添加domain的import
        topLevelClass.addImportedType("lombok.Data");
        //添加domain的注解
        topLevelClass.addAnnotation("@Data");

        topLevelClass.addImportedType("javax.validation.constraints.*");
        //添加Accessors的import
        topLevelClass.addImportedType("lombok.experimental.Accessors");
        //添加的注解
        topLevelClass.addAnnotation("@Accessors(chain = true)");
        // 获取表注释
        String remarks = introspectedTable.getRemarks();
        //添加domain的注释
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * " + remarks);
        topLevelClass.addJavaDocLine("* Created by Mybatis Generator on " + LocalDate.now());
        topLevelClass.addJavaDocLine("*/");

        return true;
    }

    //@Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        //Mapper文件的注释
        interfaze.addJavaDocLine("/**");
        interfaze.addJavaDocLine("* Created by Mybatis Generator on " +  LocalDate.now());
        interfaze.addJavaDocLine("*/");
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        //获取列长度
        int length = introspectedColumn.getLength();
        int scale = introspectedColumn.getScale();
        log.info(introspectedColumn.getActualColumnName() + ": length=" + length + ";scale= " + scale);
        // 获取列注释
        String remarks = introspectedColumn.getRemarks();
        field.addJavaDocLine("/**");
        field.addJavaDocLine(" * " + remarks);
        field.addJavaDocLine(" */");
        if (introspectedColumn.isStringColumn()) {
            length = introspectedColumn.getLength();
            field.addAnnotation("@Size(max = " + length + ")");
        }
        if (!introspectedColumn.isNullable()) {
            field.addAnnotation("@NotNull");
        }
        if (introspectedColumn.getJdbcTypeName().equals("DECIMAL")) {
            field.addAnnotation("@Digits(integer = " + length + ",fraction = " + scale + ")");
        }
        return true;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        //不生成getter
        return false;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        //不生成setter
        return false;
    }

    @Override
    public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
        sqlMap.setMergeable(true);//覆盖xml
        return super.sqlMapGenerated(sqlMap, introspectedTable);
    }

    @Override
    public boolean clientSelectAllMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;//去掉selectAll
    }

    @Override
    public boolean sqlMapSelectAllElementGenerated(XmlElement var1, IntrospectedTable var2){
        return false;//去掉selectAll
    }
}
