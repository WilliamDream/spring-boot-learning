package com.william.elasticsearch.controller;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.william.elasticsearch.model.BookRequest;
import com.william.elasticsearch.model.ResponseVo;
import com.william.elasticsearch.service.BookSearchApiService;

/**
 * @author: william
 * @Description: TODO
 * @date: 2018年12月11日 下午10:01:53
 * @version: v1.0.0
 */
@RequestMapping("/booksearch")
@RestController
public class BookSearchApiController {

	@Autowired
	private BookSearchApiService service;
	
	/**
	 * @Title: searchByBookid
	 * @Description: 通过ID查询
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/searchbyid")
	public ResponseEntity searchByBookid(@RequestBody String id) throws IOException {
		SearchResponse response = service.searchByid(id);
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	/**
	 * @Title: searchBook
	 * @Description: 综合查询，带有分页，排序，范围，条件，过滤等
	 * @param bookRequest
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/searchall")
	public ResponseEntity searchBook(@RequestBody BookRequest bookRequest) throws IOException {
		return new ResponseEntity(service.search(bookRequest), HttpStatus.OK);
	}
	
	@PostMapping("/searchBook1")
	public ResponseEntity searchBook1(@RequestBody BookRequest bookRequest) throws IOException {
		if(bookRequest.getTitle()==null||bookRequest.getTitle().isEmpty()) {
			return new ResponseEntity("title不能为空", HttpStatus.BAD_REQUEST);
		}
		ResponseVo response = service.search1(bookRequest);
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @Title: multiMatch
	 * @Description: 多字段匹配
	 * @param bookRequest
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/multiMatch")
	public ResponseEntity multiMatch(@RequestBody BookRequest bookRequest) throws IOException {
		if(bookRequest.getMatchText()==null||bookRequest.getMatchText().isEmpty()) {
			return new ResponseEntity("matchText不能为空", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(service.multiMatch(bookRequest), HttpStatus.OK);
	}
	
	/**
	 * 请求json格式
	 * {
		"pageindex":"1",	
		"pagesize":"4",
		"title":"Spring"
		}
	 * 
	 * @Title: likesearch
	 * @Description: 根据title模糊查询
	 * @param bookRequest
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/likesearch")
	public ResponseEntity likesearch(@RequestBody BookRequest bookRequest) throws IOException {
		if(bookRequest.getTitle()==null||bookRequest.getTitle().isEmpty()) {
			return new ResponseEntity("title不能为空", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(service.likeSearch(bookRequest), HttpStatus.OK);
	}
	
	/**
	 * 
	 * @Title: zhuheSearch
	 * @Description: 组合查询
	 * @param bookRequest
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/zhuheSearch")
	public ResponseEntity zhuheSearch(@RequestBody BookRequest bookRequest) throws IOException {
		if(bookRequest.getTitle()==null||bookRequest.getTitle().isEmpty()) {
			return new ResponseEntity("title不能为空", HttpStatus.BAD_REQUEST);
		}
		if(bookRequest.getEdition()==null) {
			return new ResponseEntity("edition不能为空", HttpStatus.BAD_REQUEST);
		}
		if(bookRequest.getWordCount()==null) {
			return new ResponseEntity("wordCount不能为空", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(service.zhuheSearch(bookRequest), HttpStatus.OK);
	}
	
	/**
	 * {
		"title":"Elasticsearch Action"
		}
	 * @Title: prefixQuery
	 * @Description: 根据前缀进行匹配查询
	 * @param bookRequest
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/prefixQuery")
	public ResponseEntity prefixQuery(@RequestBody BookRequest bookRequest) throws IOException {
		if(bookRequest.getTitle()==null||bookRequest.getTitle().isEmpty()) {
			return new ResponseEntity("title不能为空", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(service.prefixQuery(bookRequest), HttpStatus.OK);
	}
	
	/**
	 * {
		"field":"title",
		"matchText":"mysql*"
		}
	 * @Title: reqexpMatch
	 * @Description: 正则匹配查询,正则匹配feild字段
	 * @param bookRequest
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/reqexpMatch")
	public ResponseEntity reqexpMatch(@RequestBody BookRequest bookRequest) throws IOException {
		if(bookRequest.getField()==null||bookRequest.getField().isEmpty()) {
			return new ResponseEntity("field不能为空", HttpStatus.BAD_REQUEST);
		}
		if(bookRequest.getMatchText()==null||bookRequest.getMatchText().isEmpty()) {
			return new ResponseEntity("matchText不能为空", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(service.reqexpMatch(bookRequest), HttpStatus.OK);
	}
	

}
