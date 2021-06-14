package com.zph.programmer.springboot.exception;

import java.io.Serializable;

public class BusinessException extends Exception implements Serializable {
    private static final long serialVersionUID = 36982314123423452L;

    public BusinessException() {
    }

    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException(Throwable e) {
        super(e);
    }

    public BusinessException(String msg, Throwable e) {
        super(msg, e);
    }
}
