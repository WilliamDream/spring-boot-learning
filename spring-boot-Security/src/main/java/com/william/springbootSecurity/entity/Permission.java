/**
 * 
 */
package com.william.springbootSecurity.entity;

import java.util.Date;

/**
 * @author: william
 * @Description: 权限实体
 * @date: 2018年8月15日 下午1:57:33
 * @version: v1.0.0
 */
public class Permission {

	private int perId;
	
	private String name;
	
	private int parentId;
	
	private String url;
	
	private String icon;
	
	private String desc;
	
	private int sort;
	
	private Date cttm;

	public int getPerId() {
		return perId;
	}

	public void setPerId(int perId) {
		this.perId = perId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public Date getCttm() {
		return cttm;
	}

	public void setCttm(Date cttm) {
		this.cttm = cttm;
	} 
	
	
	
}
