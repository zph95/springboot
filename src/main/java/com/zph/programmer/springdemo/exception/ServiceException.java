package com.zph.programmer.springdemo.exception;

import java.io.Serializable;

public class ServiceException extends Exception implements Serializable {
    private static final long serialVersionUID = 3698010602725812052L;

    public ServiceException() {
    }

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(Throwable e) {
        super(e);
    }

    public ServiceException(String msg, Throwable e) {
        super(msg, e);
    }
}
