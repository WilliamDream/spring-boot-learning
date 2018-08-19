/**
 * 
 */
package com.william.springbootSecurity.entity;

import java.util.Date;

/**
 * @author: william
 * @Description: 角色实体
 * @date: 2018年8月15日 下午1:54:31
 * @version: v1.0.0
 */
public class Role {

	private int roleId;
	
	private String roleName;
	
	private String remarks;
	
	private Date cttm;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getCttm() {
		return cttm;
	}

	public void setCttm(Date cttm) {
		this.cttm = cttm;
	}
	
	 
}
