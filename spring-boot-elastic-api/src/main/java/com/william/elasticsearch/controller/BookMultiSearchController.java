package com.william.elasticsearch.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.william.elasticsearch.model.BookRequest;
import com.william.elasticsearch.model.ResponseVo;
import com.william.elasticsearch.service.BookMultiSearchService;

/**
 * @author: william
 * @Description: TODO
 * @date: 2019年2月14日 下午5:51:38
 * @version: v1.0.0
 */

@RestController
@RequestMapping("/bookmultisearch")
public class BookMultiSearchController {

	@Autowired
	private BookMultiSearchService service;
	
	@PostMapping("/multiSearch")
	public ResponseEntity searchScrollBook(@RequestBody BookRequest bookRequest) throws IOException {
		if(bookRequest.getEdition()==null) {
			return new ResponseEntity("edition不能为空", HttpStatus.BAD_REQUEST);
		}
		ResponseVo response = service.multiSearch(bookRequest);
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
}
