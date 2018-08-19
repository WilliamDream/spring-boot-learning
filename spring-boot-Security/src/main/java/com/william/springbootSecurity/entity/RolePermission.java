/**
 * 
 */
package com.william.springbootSecurity.entity;

/**
 * @author: william
 * @Description: 角色权限实体
 * @date: 2018年8月15日 下午2:03:14
 * @version: v1.0.0
 */
public class RolePermission {

	private int id;
	
	private int roleId;
	
	private int perId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getPerId() {
		return perId;
	}

	public void setPerId(int perId) {
		this.perId = perId;
	}
	
	
}
