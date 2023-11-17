package com.zph.programmer.springdemo.enums;

import lombok.Getter;

public enum RestCallLogStatusEnum {

    Start(0, "Rest请求开始"),
    End(1, "Rest请求结束"),
    ;
    @Getter
    Integer code;
    @Getter
    String message;

    RestCallLogStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
