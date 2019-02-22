package com.william.elasticsearch.controller;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.william.elasticsearch.model.Book;
import com.william.elasticsearch.service.BookService;

/**
 * @author: william
 * @Description: TODO
 * @date: 2018年12月10日 下午1:55:50
 * @version: v1.0.0
 */
@RestController
@RequestMapping("/bookindex")
public class BookController {

	@Autowired
	private BookService service;
	
	/**
	 * @Title: createIndex
	 * @Description: 创建索引
	 * @param indexName
	 * @return
	 * @throws IOException
	 */
	@GetMapping("/index")
	public ResponseEntity createIndex(@RequestParam(name = "index") String indexName) throws IOException {
		return new ResponseEntity(service.CreateIndex(indexName), HttpStatus.OK);
	}
	
	/**
	 * @Title: checkIndexExists
	 * @Description: 验证索引库是否存在
	 * @param indexName
	 * @return
	 * @throws IOException
	 */
	@GetMapping("checkindex")
	public ResponseEntity checkIndexExists(@RequestParam(name = "index") String indexName) throws IOException {
		boolean flag = service.checkIndexExists(indexName);
		return new ResponseEntity(flag, HttpStatus.OK);
	}
	
	/**
	 * @Title: delIndex
	 * @Description: 删除索引
	 * @param indexName
	 * @return
	 * @throws IOException
	 */
	@GetMapping("delindex")
	public ResponseEntity delIndex(@RequestParam(name = "index") String indexName) throws IOException {
		return new ResponseEntity(service.deleteIndex(indexName), HttpStatus.OK);
	}
	
	/**
	 * @Title: addBook
	 * @Description: 新增文档
	 * @param book
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/add")
	public ResponseEntity addBook(@RequestBody Book book) throws IOException {
		IndexResponse response = service.addNewBook(book);
		return new ResponseEntity(response,HttpStatus.OK);
	}
	
	/**
     * @Title: getDocById
     * @Description: 通过ID获取文档
     * @param docId
     * @return
     * @throws IOException
     */
	@GetMapping("getDocById")
	public ResponseEntity getDocById(@RequestParam(name = "docid") String docId) throws IOException {
		GetResponse response = service.getDocById(docId);
		return new ResponseEntity(response,HttpStatus.OK);
	}
	/**
	 * @Title: checkDocExists
	 * @Description: 验证文档是否存在
	 * @param docId
	 * @return
	 * @throws IOException
	 */
	@GetMapping("checkDocExists")
	public ResponseEntity checkDocExists(@RequestParam(name = "docid") String docId) throws IOException {
		boolean existsFlag = service.checkDocExists(docId);
		return new ResponseEntity(existsFlag, HttpStatus.OK);
	}
	
	/**
	 * @Title: delDocById
	 * @Description: 删除文档
	 * @param docId
	 * @return
	 * @throws IOException
	 */
	@GetMapping("delDocById")
	public ResponseEntity delDocById(@RequestParam(name = "docid") String docId) throws IOException {
		DeleteResponse response = service.delDocById(docId);
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	
	@PutMapping("/update")
	public ResponseEntity updateDocById(@RequestBody Book book) throws IOException {
		UpdateResponse response = service.updateDocById(book);
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
	@GetMapping("bulkIndex")
	public ResponseEntity bulkIndex(@RequestParam(name = "ids") String ids) throws IOException {
		BulkResponse response = service.bulkIndex(ids);
		return new ResponseEntity(response, HttpStatus.OK);
	}
	
}
