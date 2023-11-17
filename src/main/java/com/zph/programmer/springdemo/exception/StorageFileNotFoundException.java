package com.zph.programmer.springdemo.exception;

import java.io.Serializable;

public class StorageFileNotFoundException extends StorageException  {
    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
