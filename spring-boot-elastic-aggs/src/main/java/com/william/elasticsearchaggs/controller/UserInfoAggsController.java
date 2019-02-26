package com.william.elasticsearchaggs.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.william.elasticsearchaggs.service.UserInfoMaxMinAvgAggsSerivce;

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
	private UserInfoMaxMinAvgAggsSerivce service;

	@GetMapping("/usercount")
	public ResponseEntity createIndex() throws IOException {
		return new ResponseEntity(service.userInfoAvgAge(), HttpStatus.OK);
	}
	
}
