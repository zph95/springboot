package com.zph.programmer.api.enums;

import lombok.Getter;

public enum RestCallLogStatus {

    Start(0,"Rest请求开始"),
    End(1,"Rest请求结束"),
    ;
    @Getter
    Integer code;
    @Getter
    String message;

    RestCallLogStatus(Integer code, String message ){
        this.code=code;
        this.message=message;
    }
}
