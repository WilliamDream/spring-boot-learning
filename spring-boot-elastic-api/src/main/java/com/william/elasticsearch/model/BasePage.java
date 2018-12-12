package com.william.elasticsearch.model;

import lombok.Data;

/**
 * @author: chaiz
 * @Description: TODO
 * @date: 2018年12月12日 上午11:37:41
 * @version: v1.0.0
 */
//@Data
public class BasePage {

	/** 第几页 */
	protected int pageindex;
	/** 页面大小 */
	protected int pagesize;
	
	
	public int getPageindex() {
		return pageindex;
	}
	public void setPageindex(int pageindex) {
		this.pageindex = pageindex;
	}
	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	

	
}
