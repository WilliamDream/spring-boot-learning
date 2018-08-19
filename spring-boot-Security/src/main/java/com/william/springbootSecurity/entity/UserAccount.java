/**
 * 
 */
package com.william.springbootSecurity.entity;

import java.util.Date;

/**
 * @author: william
 * @Description: 用户账号实体
 * @date: 2018年8月15日 下午2:44:57
 * @version: v1.0.0
 */
public class UserAccount {

	private int accId;
	
	private String account;
	
	private String pwd;
	
	private String nickName;
	
	private String email;
	
	private String phone;
	
	private boolean status;
	
	private Date cttm;

	public int getAccId() {
		return accId;
	}

	public void setAccId(int accId) {
		this.accId = accId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Date getCttm() {
		return cttm;
	}

	public void setCttm(Date cttm) {
		this.cttm = cttm;
	}
	
	
	
}
