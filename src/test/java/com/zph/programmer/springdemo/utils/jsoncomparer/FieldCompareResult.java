

package com.zph.programmer.springdemo.utils.jsoncomparer;

import lombok.Data;

@Data
public class FieldCompareResult {
    private String currentNode;
    private String message;
    private Boolean isMatch;
    private String fieldName;

    public FieldCompareResult(String currentNode, String message, Boolean isMatch, String fieldName) {
        this.currentNode = currentNode;
        this.message = message;
        this.isMatch = isMatch;
        this.fieldName = fieldName;
    }

    @Override
    public String toString() {
        return "FieldCompareResult{" +
                "错误信息='" + message + '\'' +
                ", 是否匹配=" + isMatch +
                ", 节点名称='" + fieldName + '\'' +
                '}';
    }
}
