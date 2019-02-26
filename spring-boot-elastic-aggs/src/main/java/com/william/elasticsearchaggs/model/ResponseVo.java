package com.william.elasticsearchaggs.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;


/**
 * 
 * @author: william
 * @Description: 返回数据封装实体类
 * @date: 2018年2月13日 下午2:48:47
 * @version: v1.0.0
 */
@Data
public class ResponseVo {

	private String errmsg;

	private int errcode;
	
	private long count;

	private List<Map<String, Object>> data;
	
	private float consumetime;

	public ResponseVo() {
		
	}
	
	/**
	 * 成功
	 */
	public static ResponseVo success(List<Map<String, Object>> data, long count, float consumetime) {
		String errmsg = "请求返回成功";
		ResponseVo responseVo = new ResponseVo();
		responseVo.setErrcode(0);
		responseVo.setErrmsg(errmsg);
		responseVo.setCount(count);
		responseVo.setData(data);
		responseVo.setConsumetime(consumetime);
		return responseVo;
	}
	
	
	/**
	 * 失败
	 */
	public static ResponseVo error(String errorInfo) {
		String errmsg = "请求返回错误";
		ResponseVo responseVo = new ResponseVo();
		responseVo.setErrcode(1);
		responseVo.setErrmsg(errmsg);
		responseVo.setData(new ArrayList<>());
		return responseVo;
	}


	
}
