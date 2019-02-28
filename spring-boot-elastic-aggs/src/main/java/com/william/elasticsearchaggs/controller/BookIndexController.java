package com.william.elasticsearchaggs.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.william.elasticsearchaggs.service.BookGenerateDateService;

/**
 * @author: william
 * @Description: TODO
 * @date: 2019年2月28日 下午2:57:32
 * @version: v1.0.0
 */
@RestController
@RequestMapping("/bookindex")
public class BookIndexController {

	@Autowired
	private BookGenerateDateService service;
	
	
	@GetMapping("/createmapping")
	public ResponseEntity createIndex() throws IOException {
		return new ResponseEntity(service.createMapping(), HttpStatus.OK);
	}
	
}
