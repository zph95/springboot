package com.zph.programmer.springdemo.exception;

import java.io.Serializable;

public class CacheException extends Exception implements Serializable {

    private static final long serialVersionUID = 3698010602725812052L;

    public CacheException() {
    }

    public CacheException(String msg) {
        super(msg);
    }

    public CacheException(Throwable e) {
        super(e);
    }

    public CacheException(String msg, Throwable e) {
        super(msg, e);
    }
}