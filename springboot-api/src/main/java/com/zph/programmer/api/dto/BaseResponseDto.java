package com.zph.programmer.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BaseResponseDto<T> {

    private final static  String success="success";

    private final static  String fail="fail";

    private final static  String error="error";

    /**
     * 包含一个整数类型的HTTP响应状态码。
     */
    private Integer code;

    /**
     * 包含文本："success"，"fail"或"error"。
     * HTTP状态响应码在500-599之间为"fail"，在400-499之间为"error"，
     * 其它均为"success"（例如：响应状态码为1XX、2XX和3XX）。
     */
    private String status;

    /**
     * 当状态值为"fail"和"error"时有效，用于显示错误信息。
     * 参照国际化（il8n）标准，它可以包含信息号或者编码，可以只包含其中一个，或者同时包含并用分隔符隔开。
     */
    private String message;

    /**
     * 当状态值为"fail"和"error"时有效，用于显示更多错误信息。主要用于分析问题使用
     */
    private Object moreInfo;

    /**
     * 包含响应的body。当状态值为"fail"或"error"时，data仅包含错误原因或异常名称
     */
    private T data;

    public BaseResponseDto(Integer code,String status,String message, T data){
        this.code=code;
        this.status=status;
        this.data=data;
        this.message=message;
    }

    public static <T> BaseResponseDto<T> success(Integer code, T data) {
        return new BaseResponseDto<>(code, success, null, data);
    }

    public static <T> BaseResponseDto<T> fail(Integer code, String message, T data) {
        return new BaseResponseDto<>(code, fail, message, data);
    }

    public static <T> BaseResponseDto<T> error(Integer code, String message, T data) {
        return new BaseResponseDto<>(code, error, message, data);
    }

    public boolean isSuccess() {
        return success.equals(status);
    }
}
