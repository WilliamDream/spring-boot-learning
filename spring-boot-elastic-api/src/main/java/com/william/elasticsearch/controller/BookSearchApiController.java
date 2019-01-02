package com.william.elasticsearch.controller;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.william.elasticsearch.model.BookRequest;
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
	
	@PostMapping("/searchbyid")
	public ResponseEntity searchByBookid(@RequestBody String id) throws IOException {
		SearchResponse response = service.searchByid(id);
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@PostMapping("/searchall")
	public ResponseEntity searchBook(@RequestBody BookRequest bookRequest) throws IOException {
		SearchResponse response = service.search(bookRequest);
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@PostMapping("/searchall1")
	public ResponseEntity searchBook1(@RequestBody BookRequest bookRequest) throws IOException {
		SearchResponse response = service.search1(bookRequest);
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	
	@PostMapping("/likesearch")
	public ResponseEntity likesearch(@RequestBody BookRequest bookRequest) throws IOException {
		SearchResponse response = service.likeSearch(bookRequest);
		return new ResponseEntity(response, HttpStatus.OK);
	}
}
