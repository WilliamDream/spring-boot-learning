package com.william.elasticsearch.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.william.elasticsearch.service.BookBulkQueryService;

/**
 * @author: william
 * @Description: 批量操作API
 * @date: 2019年2月21日 下午5:19:37
 * @version: v1.0.0
 */

@RestController
@RequestMapping("bookbulk")
public class BookBulkQueryController {

	@Autowired
	private BookBulkQueryService service;
	/***
	 * bulk api
	 * @Title: bulkOperate
	 * @Description: 批量操作接口，可是多个新增、修改、删除同时操作
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/bulkOperate")
	public ResponseEntity prefixQuery() throws IOException {
		return new ResponseEntity(service.bulkOperate(), HttpStatus.OK);
	}
	
	
	
}
