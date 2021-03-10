package com.zph.programmer.springboot.exception;


import com.zph.programmer.api.dto.BaseResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zengpenghui
 * @date 2020/11/24 11:39
 * @Description
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 返回状态码--系统异常
     */
    public static final int STATUS_ERROR = 999;

    /**
     * 返回状态码--参数错误
     */
    public static final int PARAM_ERROR = 996;

    /**
     * 默认错误提示消息
     */
    public static final String DEFAULT_ERROR_MESSAGE = "系统繁忙，请稍后重试";

    public static final String PARAM_ERROR_MESSAGE = "参数错误！";


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public BaseResponseDto<String> resolveConstraintViolationException(ConstraintViolationException ex) {

        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        String errorMessage = constraintViolations.stream().map(x -> x.getMessage()).collect(Collectors.joining(","));
        return BaseResponseDto.fail(PARAM_ERROR, errorMessage, null);

    }

    /**
     * 方法参数校验
     */
    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
    @ResponseBody
    public BaseResponseDto<String> handleParameterVerificationException(Exception e) {
        log.error(" handleParameterVerificationException has been invoked", e);
        String msg = null;
        /// BindException
        if (e instanceof BindException) {
            // getFieldError获取的是第一个不合法的参数(P.S.如果有多个参数不合法的话)
            FieldError fieldError = ((BindException) e).getFieldError();
            if (fieldError != null) {
                msg = fieldError.getDefaultMessage();
            }
            /// MethodArgumentNotValidException
        } else if (e instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            // getFieldError获取的是第一个不合法的参数(P.S.如果有多个参数不合法的话)
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                msg = fieldError.getDefaultMessage();
            }
            /// ValidationException 的子类异常ConstraintViolationException
        } else if (e instanceof ConstraintViolationException) {
            /*
             * ConstraintViolationException的e.getMessage()形如
             *     {方法名}.{参数名}: {message}
             *  这里只需要取后面的message即可
             */
            msg = e.getMessage();
            if (msg != null) {
                int lastIndex = msg.lastIndexOf(':');
                if (lastIndex >= 0) {
                    msg = msg.substring(lastIndex + 1).trim();
                }
            }
            /// ValidationException 的其它子类异常
        } else {
            msg = "处理参数时异常";
        }
        return BaseResponseDto.fail(PARAM_ERROR, msg, e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResponseDto<String> resolveException(Exception ex) {
        log.error("Exception:", ex);
        return BaseResponseDto.fail(STATUS_ERROR, DEFAULT_ERROR_MESSAGE, ex.getMessage());
    }

}
