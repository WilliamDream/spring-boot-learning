package com.william.exception;


/**
 * @Auther: williamdream
 * @Date: 2019/7/29 17:30
 * @Description: 配置错误
 */
public class ConfigException extends RuntimeException{
	
	public ConfigException(String message){
		super(message);
	}
}
