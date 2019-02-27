package com.william.elasticsearchaggs.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.william.elasticsearchaggs.service.UserInfoAggsSerivce;

/**
 * @author: william
 * @Description: 用户信息聚合查询服务接口
 * @date: 2019年2月26日 下午7:25:07
 * @version: v1.0.0
 */
@RestController
@RequestMapping("/useraggs")
public class UserInfoAggsController {
	
	@Autowired
	private UserInfoAggsSerivce service;

	/**
	 * @Title: getUserAgeSum
	 * @Description: 获取年龄之和
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/aggs1")
	public ResponseEntity getUserAgeSum() throws IOException {
		return new ResponseEntity(service.getUserAgeSum(), HttpStatus.OK);
	}
	
	/**
	 * select gender,avg(age) as province_count from userinfo group by gender
	 * @Title: UserInfoAvgAge
	 * @Description: 获取平均年龄
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/aggs2")
	public ResponseEntity getUserAvgAge() throws IOException {
		return new ResponseEntity(service.getUserAvgAge(), HttpStatus.OK);
	}
	
	/**
	 * select gender,count(*) from wechat group by gender
	 * @Title: getUserCountGpGender
	 * @Description: 根据性别进行分组
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/aggs3")
	public ResponseEntity getUserCountGpGender() throws IOException {
		return new ResponseEntity(service.getUserCountGpGender(), HttpStatus.OK);
	}
	
	/**
	 * @Title: userInfoGroupByAge
	 * @Description: 获取最大最小年龄
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/aggs4")
	public ResponseEntity getGpGenderSumAge() throws IOException {
		return new ResponseEntity(service.getMaxMinAge(), HttpStatus.OK);
	}
	
}
