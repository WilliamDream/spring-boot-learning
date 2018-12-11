package com.william.elasticsearch.model;

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
	
	private Integer wordCount;
	
	private String publishDate;
	
	
}
