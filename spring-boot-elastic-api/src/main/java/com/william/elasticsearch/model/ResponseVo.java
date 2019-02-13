package com.william.elasticsearch.model;

import java.util.ArrayList;


/**
 * 
 * @author: william
 * @Description: 返回数据封装实体类
 * @date: 2019年2月13日 下午2:48:47
 * @version: v1.0.0
 */
public class ResponseVo {

	private String errmsg;

	private int errcode;
	
	private long count;

	private Object data;
	

	public ResponseVo() {
		
	}
	
	/**
	 * 成功
	 */
	public static ResponseVo success(Object data, long count) {
		String errmsg = "请求返回成功";
		ResponseVo responseVo = new ResponseVo();
		responseVo.setErrcode(0);
		responseVo.setErrmsg(errmsg);
		responseVo.setCount(count);
		responseVo.setData(data);
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



	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	
}
