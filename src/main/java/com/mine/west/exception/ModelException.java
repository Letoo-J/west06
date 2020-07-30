package com.mine.west.exception;

import lombok.Getter;

@Getter
public class ModelException extends Exception {
    private int code;

    public ModelException(ExceptionInfo exceptionInfo) {
        super(exceptionInfo.getMessage());
        this.code = exceptionInfo.getCode();
    }
}
