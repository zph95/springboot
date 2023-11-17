package com.zph.programmer.springdemo.po;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * null
* Created by Mybatis Generator on 2021-02-12
*/
@Data
@Accessors(chain = true)
public class RestCallLogRecord {
    /**
     * null
     */
    private Integer id;

    /**
     * null
     */
    private Integer userId;

    /**
     * null
     */
    private String method;

    /**
     * null
     */
    private String uri;

    /**
     * null
     */
    private String request;

    /**
     * null
     */
    private String response;

    /**
     * null
     */
    private Integer status;

    /**
     * null
     */
    private Long costTime;

    /**
     * null
     */
    private Integer isValid;

    /**
     * null
     */
    private String createdTime;

    /**
     * null
     */
    private String modifiedTime;
}