package com.william.elaticsearch.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.william.elaticsearch.service.SearchNearbyPeopleService;

/**
 * @author: chaiz
 * @Description: 基于GEO模拟微信搜索附近的人
 * @date: 2019年2月22日 下午4:01:57
 * @version: v1.0.0
 */

@RestController
@RequestMapping("nearby")
public class SearchNearbyPeopleController {
	
	@Autowired
	private SearchNearbyPeopleService service;
	
	@GetMapping("/createmapping")
	public ResponseEntity createIndex() throws IOException {
		return new ResponseEntity(service.createIndex(), HttpStatus.OK);
	}
	
	@GetMapping("/createdata")
	public ResponseEntity createData() throws IOException {
		return new ResponseEntity(service.createBulkData(22.548732,113.941607, 500), HttpStatus.OK);
	}
	
	
	@PostMapping("/searchnearby")
	public ResponseEntity searchNearby() throws IOException {
		return new ResponseEntity(service.searchNearby(22.548732,113.941607, 5000, 10, null), HttpStatus.OK);
	}
	
	
	
	
}
