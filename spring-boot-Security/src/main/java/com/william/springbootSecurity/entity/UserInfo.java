/**
 * 
 */
package com.william.springbootSecurity.entity;

import java.util.Date;

/**
 * @author: william
 * @Description: 用户个人信息实体
 * @date: 2018年8月15日 下午2:38:23
 * @version: v1.0.0
 */
public class UserInfo {

	private Integer userId;
	
	private String userName;
	
	private int gender;
	
	private String idCard;
	
	private String email;
	
	private String address;
	
	private String birthday;
	
	private Date cttm;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public Date getCttm() {
		return cttm;
	}

	public void setCttm(Date cttm) {
		this.cttm = cttm;
	}
	
	
}
