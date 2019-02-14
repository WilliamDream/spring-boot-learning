package com.william.elasticsearch.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.william.elasticsearch.model.BookRequest;
import com.william.elasticsearch.model.ResponseVo;
import com.william.elasticsearch.service.BookSearchScrollApiService;

/**
 * @author: william
 * @Description: 滚动搜索Api
 * @date: 2019年2月14日 上午9:28:09
 * @version: v1.0.0
 */

@RestController
@RequestMapping("/booksearchscroll")
public class BookSearchScrollApiController {

	@Autowired
	private BookSearchScrollApiService service;
	
	@PostMapping("/searchScrollBook")
	public ResponseEntity searchScrollBook(@RequestBody BookRequest bookRequest) throws IOException {
		ResponseVo response = service.searchScroll(bookRequest);
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	
}
