package com.william.elasticsearchaggs.model;

import lombok.Data;

/**
 * @author: william
 * @Description: TODO
 * @date: 2018年8月30日 下午13:39:55
 * @version: v1.0.0
 */

@Data
public class Book {

	private String id;
	
	private String title;
	
	private String author;
	
	private String type;
	
	private int edition;
	
	private int wordCount;
	
	private String publishDate;
	
	private String desc;

	/**
	 * @param id
	 * @param title
	 * @param author
	 * @param wordCount
	 * @param publishDate
	 */
	public Book(String id, String title, String author, Integer wordCount, String publishDate) {
		super();
		this.id = id;
		this.title = title;
		this.author = author;
		this.wordCount = wordCount;
		this.publishDate = publishDate;
	}
	
	
}
