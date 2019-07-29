package com.william.exception;

/**
 * @Auther: williamdream
 * @Date: 2019/7/29 17:35
 * @Description: 解析错误
 */
public class ParseException extends  RuntimeException {

    public ParseException(String message, Throwable cause){
        super(message,cause);
    }
}
