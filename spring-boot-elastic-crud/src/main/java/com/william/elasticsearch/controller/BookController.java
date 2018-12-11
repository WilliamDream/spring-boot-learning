package com.william.elasticsearch.controller;

import javax.websocket.server.PathParam;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.william.elasticsearch.model.Book;
import com.william.elasticsearch.service.BookService;

/**
 * @author: william
 * @Description: TODO
 * @date: 2018年8月30日 下午4:04:21
 * @version: v1.0.0
 */

@RestController
@RequestMapping("/book")
public class BookController {

	@Autowired
	private BookService bookService;
	
	@GetMapping("/{id}")
	public ResponseEntity getBookById(@PathVariable(value="id") String id) {
		GetResponse response = bookService.getBookById(id);
		if(!response.isExists()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response.getSource(),HttpStatus.OK);
	}
	
	@PostMapping("/add")
	public ResponseEntity addBook(@RequestBody Book book) {
		IndexResponse response = null;
		try {
			bookService.addBook(book);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity(response,HttpStatus.OK);
	}
	
	@PutMapping("/edit")
	public ResponseEntity editBook(@RequestBody Book book) {
		UpdateResponse response = null;
		try {
			response = bookService.editBook(book);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(response,HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity delBook(@PathVariable(value="id") String id) {
		DeleteResponse response = bookService.delBook(id);
        return new ResponseEntity(response.getResult().toString(),HttpStatus.OK);
	}
}
