package com.william.elasticsearch.controller;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.william.elasticsearch.service.BookSearchApiService;

/**
 * @author: chaiz
 * @Description: TODO
 * @date: 2018年12月11日 下午10:01:53
 * @version: v1.0.0
 */
@RequestMapping("/booksearch")
@RestController
public class BookSearchApiController {

	@Autowired
	private BookSearchApiService service;
	
	@GetMapping("/searchall")
	public ResponseEntity createIndex() throws IOException {
		SearchResponse response = service.search();
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
}
