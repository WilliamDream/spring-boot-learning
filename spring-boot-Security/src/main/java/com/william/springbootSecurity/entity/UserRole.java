/**
 * 
 */
package com.william.springbootSecurity.entity;

/**
 * @author: chaiz
 * @Description: 用户角色实体
 * @date: 2018年8月15日 下午2:00:38
 * @version: v1.0.0
 */
public class UserRole {

	private int id;
	
	private int accId;
	
	private int roleId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAccId() {
		return accId;
	}

	public void setAccId(int accId) {
		this.accId = accId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
	
	
}
