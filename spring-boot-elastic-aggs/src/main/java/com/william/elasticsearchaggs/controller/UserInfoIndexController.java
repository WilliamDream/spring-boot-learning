package com.william.elasticsearchaggs.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.william.elasticsearchaggs.service.UserInfoGenerateDataService;

/**
 * @author: william
 * @Description: TODO
 * @date: 2019年2月26日 下午7:05:37
 * @version: v1.0.0
 */
@RestController
@RequestMapping("/userindex")
public class UserInfoIndexController {

	@Autowired
	private UserInfoGenerateDataService service;
	
	@GetMapping("/createmapping")
	public ResponseEntity createIndex() throws IOException {
		return new ResponseEntity(service.createMapping(), HttpStatus.OK);
	}
	
}
