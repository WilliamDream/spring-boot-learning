package com.william.elaticsearch.model;

import lombok.Data;

/**
 * @author: william
 * @Description: 微信用户类
 * @date: 2019年2月21日 下午18:30:57
 * @version: v1.0.0
 */
@Data
public class UserInfo {

	private String wechatno;
	
	private String nickname;
	
	private String gender;
	
	private double lat;
	
	private double lon;
	
	private String birthday;
	
	private int age;
	
	private String province;

	/**
	 * @param wechatno
	 * @param nickname
	 * @param gender
	 * @param lat
	 * @param lon
	 */
	public UserInfo(String wechatno, String nickname, String gender, double lat, double lon, String birthday,int age, String province) {
		super();
		this.wechatno = wechatno;
		this.nickname = nickname;
		this.gender = gender;
		this.lat = lat;
		this.lon = lon;
		this.birthday = birthday;
		this.age = age;
		this.province = province;
	}
	
	
}
