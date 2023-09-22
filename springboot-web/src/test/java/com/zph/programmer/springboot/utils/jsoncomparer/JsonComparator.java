
package com.zph.programmer.springboot.utils.jsoncomparer;

import cn.hutool.core.util.ArrayUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN;
import static com.fasterxml.jackson.databind.DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS;


public class JsonComparator {


    //region 私有属性
    /**
     * 默认根节点名称
     */
    private static final String DEFAULT_NODE = "root";
    /**
     * jackson 解析工具
     */
    private ObjectMapper objectMapper = new ObjectMapper();
    /**
     * 期待数据文件路径
     */
    private String expectJsonFile;

    /**
     * json 数组元素匹配策略
     */
    private Map<String, List<String>> matchStrategy = new HashMap<>();

    /**
     * 忽略字段
     */
    private String[] ignoreFields;

    /**
     * 比较字段
     */
    private String[] compareFields;

    //private
    //endregion

    //region 构造器

    /**
     * 构造器
     */
    private JsonComparator() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        javaTimeModule.addSerializer(Date.class, new DateSerializer());
        javaTimeModule.addDeserializer(Date.class, new DateDeserializer());
        objectMapper.enable(WRITE_BIGDECIMAL_AS_PLAIN);
        objectMapper.enable(USE_BIG_DECIMAL_FOR_FLOATS);
        objectMapper.registerModule(javaTimeModule);
    }


    /**
     * 构造器
     *
     * @param expectJsonFile 期待数据文件路径
     */
    private JsonComparator(String expectJsonFile) {
        this();
        this.expectJsonFile = expectJsonFile;
    }
    //endregion

    //region 实例化工具

    /**
     * 创建一个对象
     *
     * @return
     */
    public static JsonComparator newInstance() {
        return new JsonComparator();
    }

    /**
     * 创建对象，并设置期待值json文件
     *
     * @param expectJsonFile
     * @return
     */
    public static JsonComparator newInstance(String expectJsonFile) {
        return new JsonComparator(expectJsonFile);
    }
    //endregion

    /**
     * 读取文本文件
     *
     * @param fileName
     * @return
     */
    public static String readFileContent(String fileName) {
        // TODO: 2019-12-9 这种借助ResourceUtils的方式读取文件，不合适
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            File file = ResourceUtils.getFile("classpath:" + fileName);
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
    }

    /**
     * 设置数组，元素匹配策略
     *
     * @param arrayNodeName 数组节点的名称
     * @param strategies    关键字段
     * @return
     */
    public JsonComparator setMatchStrategy(String arrayNodeName,
                                           String... strategies) {
        this.matchStrategy.put(arrayNodeName, Arrays.asList(strategies));
        return this;
    }

    /**
     * 设置期待数据文件
     *
     * @param fileName
     * @return
     */
    public JsonComparator setExpectJsonFile(String fileName) {
        this.expectJsonFile = fileName;
        return this;
    }

    /**
     * 设置忽略字段
     *
     * @param fieldName 字段名称
     * @return
     */
    public JsonComparator setIgnoreFields(String... fieldName) {
        this.ignoreFields = fieldName;
        return this;
    }

    public JsonComparator setCompareFields(String... fields) {
        this.compareFields = fields;
        return this;
    }

    /**
     * 比较json对象
     *
     * @param object
     * @return
     */
    public List<FieldCompareResult> compareTo(Object object) {
        if (this.expectJsonFile.isEmpty()) {
            throw new RuntimeException("未设置期待数据！");
        }
        try {
            String jsonStr = objectMapper.writeValueAsString(object);
            JsonNode sourceNode = objectMapper.readTree(readFileContent(this.expectJsonFile));
            JsonNode targetNode = objectMapper.readTree(jsonStr);
            return compare(sourceNode, targetNode, DEFAULT_NODE);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 对比json对象并断言
     *
     * @param object
     */
    public void compareAssert(Object object) {
        if (this.expectJsonFile.isEmpty()) {
            throw new RuntimeException("未设置期待数据！");
        }
        try {
            String sourceJsonStr = readFileContent(this.expectJsonFile);

            doAssert(object, sourceJsonStr);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 对比json对象并断言
     *
     * @param object
     */
    public void compareAssert(Object object, String sourceJson) {
        try {
            doAssert(object, sourceJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void doAssert(Object object, String sourceJsonStr) throws IOException {
        objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategy() {


            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName) {
                return convertForField(defaultName);
            }

            @Override
            public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
                return defaultName;
            }

            @Override
            public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
                return convertForMethod(method, defaultName);
            }

            private String convertForField(String defaultName) {
                return defaultName;
            }

            private String convertForMethod(AnnotatedMethod method, String defaultName) {

                return defaultName;
            }


        });
        String targetJsonStr = objectMapper.writeValueAsString(object);
        String sourceFormatterJson = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(objectMapper.readValue(sourceJsonStr,
                        Object.class));
        String targetFormatterJson =
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);

        System.out.println(String.format("源数据：\n%s", sourceFormatterJson));
        System.out.println(String.format("目标数据：\n%s", targetFormatterJson));

        JsonNode sourceNode = objectMapper.readTree(sourceJsonStr);
        JsonNode targetNode = objectMapper.readTree(targetJsonStr);
        List<FieldCompareResult> results = compare(sourceNode, targetNode,
                DEFAULT_NODE);
        for (FieldCompareResult result : results) {
            System.out.println(result.getMessage());
        }
        Assert.assertTrue(results.stream().allMatch(x -> x.getIsMatch()));
    }

    /**
     * 比较两个对象
     *
     * @param source
     * @param target
     * @param key
     * @return
     */
    private List<FieldCompareResult> compare(JsonNode source, JsonNode target, String key) {
        JsonNodeType nodeType = source.getNodeType();
        List<FieldCompareResult> results = new ArrayList<FieldCompareResult>();

        if (!key.equals(DEFAULT_NODE) && ArrayUtil.isNotEmpty(this.compareFields) && ArrayUtil.contains(this.compareFields, key)) {
            return results;
        }

        if (StringUtils.isNotEmpty(key) && ArrayUtils.contains(this.ignoreFields, key)) {
            return results;
        }

        //基本类型比较
        if (isPrimitiveType(nodeType)) {
            FieldCompareResult priCompareResult =
                    this.comparePrimitiveType(source, target, key);
            if (priCompareResult != null) {
                results.add(priCompareResult);
            }
        }

        //对象类型比较
        if (nodeType.equals(JsonNodeType.OBJECT)) {
            List<FieldCompareResult> objCompareResult =
                    this.compareObjectNode(source, target);
            results.addAll(objCompareResult);
        }

        //数组类型比较
        if (nodeType.equals(JsonNodeType.ARRAY)) {
            List<FieldCompareResult> arrayCompareResult =
                    this.compareArray(source, target, key);
            results.addAll(arrayCompareResult);
        }
        return results;
    }

    /**
     * 在目标数组中，根据数组匹配策略，查找元素
     *
     * @param sourceNode
     * @param targetList
     * @param strategies
     * @return
     */
    private JsonNode findMatchNode(JsonNode sourceNode, List<JsonNode> targetList, List<String> strategies) {

        for (JsonNode targetNode : targetList) {
            if (isMatch(sourceNode, targetNode, strategies)) {
                return targetNode;
            }
        }
        return null;
    }

    /**
     * 根据传入的匹配策略，判断两个Node节点是否相等
     *
     * @param sourceNode
     * @param targetNode
     * @param strategies
     * @return
     */
    private boolean isMatch(JsonNode sourceNode, JsonNode targetNode,
                            List<String> strategies) {

        List<Boolean> result = new ArrayList<>();
        for (String strategy : strategies) {
            Boolean res =
                    getNodeValueByStrategy(sourceNode, strategy).equals(getNodeValueByStrategy(targetNode, strategy));
            result.add(res);
        }
        return result.stream().allMatch(x -> x.equals(true));
    }

    /**
     * 根据匹配策略，获取节点的值
     *
     * @param jsonNode 节点
     * @param strategy 匹配策略
     * @return
     */
    // TODO: 2019-12-9 :解决获取数组类型节点值
    private JsonNode getNodeValueByStrategy(JsonNode jsonNode, String strategy) {

        List<String> fieldList = StringUtils.isEmpty(strategy) ?
                Collections.emptyList() : Arrays.asList(strategy.split("\\."));
        if (fieldList.size() <= 0) {
            return jsonNode;
        }

        JsonNode currentNode = jsonNode.deepCopy();
        for (String f : fieldList) {
            currentNode = currentNode.get(f);
        }
        return currentNode;
    }

    /**
     * 根据迭代器获取集合数据
     *
     * @param it
     * @param <T>
     * @return
     */
    private <T> List<T> getIteratorData(Iterator<T> it) {
        List<T> result = new ArrayList<T>();
        while (it.hasNext()) {
            result.add(it.next());
        }
        return result;
    }

    //region 比较基本类型

    /**
     * 判断当前节点的类型是否是基本数据类型
     *
     * @param nodeType
     * @return
     */
    private boolean isPrimitiveType(JsonNodeType nodeType) {
        return nodeType == JsonNodeType.BOOLEAN ||
                nodeType == JsonNodeType.STRING ||
                nodeType == JsonNodeType.NUMBER;
    }
    //endregion


    //region 比较对象类型

    /**
     * 比较基本数据类型
     *
     * @param source 源对象（期待值）
     * @param target 目标对象（单元测试的值）
     * @param key    当前比较节点的名称
     * @return 比较结果
     */
    private FieldCompareResult comparePrimitiveType(JsonNode source, JsonNode target,
                                                    String key) {
        FieldCompareResult result = null;
        if (source == null || target == null) {
            result = new FieldCompareResult("", "目标对象或源对象为空", false, key);
        }

        if (source.getNodeType() == JsonNodeType.NUMBER) {
            Number sourceValue = source.numberValue();
            Number targetValue = target.numberValue();

            if (sourceValue instanceof BigDecimal) {
                BigDecimal decimalSource = (BigDecimal) sourceValue;
                BigDecimal decimalTarget = (BigDecimal) sourceValue;
                if (!decimalSource.equals(decimalTarget)) {
                    String msg = String.format("%s属性值不相同！源值：%s，目标值：%s", key, decimalSource.toPlainString(),
                            decimalTarget.toPlainString());
                }
            } else {
                if (!sourceValue.equals(targetValue)) {
                    String msg = String.format("%s属性值不相同！源值：%s，目标值：%s", key, sourceValue,
                            targetValue);
                }
            }
        }

        // TODO: 2019-12-30 处理string类型的数字、日期、布尔
        if (source.getNodeType() == JsonNodeType.STRING) {
            String sourceValue = source.asText();
            String targetValue = target.asText();
            if (!sourceValue.equals(targetValue)) {
                String msg = String.format("%s属性值不相同！源值：%s，目标值：%s", key, sourceValue,
                        targetValue);
                result = new FieldCompareResult("", msg, false, key);
            }
        }

        if (source.getNodeType() == JsonNodeType.BOOLEAN) {
            Boolean sourceValue = source.booleanValue();
            Boolean targetValue = target.booleanValue();
            if (!sourceValue.equals(targetValue)) {
                String msg = String.format("%s属性值不相同！源值：%s，目标值：%s", key, sourceValue,
                        targetValue);
                result = new FieldCompareResult("", msg, false, key);
            }
        }
        if ((source.getNodeType() == JsonNodeType.NULL ||
                target.getNodeType() == JsonNodeType.NULL) &&
                (source.getNodeType() != target.getNodeType())) {
            String sourceValue = source.asText();
            String targetValue = target.asText();
            if (!sourceValue.equals(targetValue)) {
                String msg = String.format("%s属性值不相同！源值：%s，目标值：%s", key, sourceValue,
                        targetValue);
                result = new FieldCompareResult("", msg, false, key);
            }
        }
        return result;
    }
    //endregion

    //region 比较数组类型

    /**
     * 比较对象类型节点，并返回对比结构
     *
     * @param source
     * @param target
     * @return
     */
    private List<FieldCompareResult> compareObjectNode(JsonNode source, JsonNode target) {
        List<FieldCompareResult> result = new ArrayList<FieldCompareResult>();
        List<String> sourceKeys = this.getIteratorData(source.fieldNames());
        List<String> targetKeys = this.getIteratorData(target.fieldNames());

        for (String key : sourceKeys) {
            if (targetKeys.contains(key)) {
                JsonNode sourceNode = source.get(key);
                JsonNode targetNode = target.get(key);
                List<FieldCompareResult> compareResults = compare(sourceNode,
                        targetNode, key);
                result.addAll(compareResults);
            } else {
                result.add(new FieldCompareResult(
                        source.asText(),
                        String.format("目标对象不包含键:%s", key),
                        false,
                        key));
            }
        }
        return result;
    }
    //endregion

    /**
     * 比较数组节点
     *
     * @param sourceArray
     * @param targetArray
     * @return
     */
    private List<FieldCompareResult> compareArray(JsonNode sourceArray, JsonNode targetArray, String key) {

        List<FieldCompareResult> results = new ArrayList<>();

        List<JsonNode> sourceList = this.getIteratorData(sourceArray.elements());
        List<JsonNode> targetList = this.getIteratorData(targetArray.elements());

        if (sourceList.size() != targetArray.size()) {
            String msg = String.format("数组长度不一致,源数组:%s，目标数组：%s",
                    sourceArray.size(), targetArray.size());
            results.add(new FieldCompareResult("", msg, false, ""));
            return results;
        }

        System.out.println(String.format("比较数组:%s", key));

        /*
         *  如果设置了元素匹配策略，则按照设置的策略到目标数组中查找，否则按照自然顺序
         * */

        if (this.matchStrategy.containsKey(key)) {
            List<String> strategies = this.matchStrategy.get(key);
            for (JsonNode sourceNode : sourceList) {
                JsonNode targetNode = findMatchNode(sourceNode, targetList, strategies);
                if (targetNode == null) {
                    //未找到匹配元素

                } else {
                    results.addAll(this.compare(sourceNode, targetNode, key));
                }
            }

        } else {
            for (int i = 0; i < sourceList.size(); i++) {
                JsonNode sourceNode = sourceList.get(i);
                JsonNode targetNode = targetList.get(i);
                results.addAll(this.compare(sourceNode, targetNode, key));
            }
        }
        return results;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
