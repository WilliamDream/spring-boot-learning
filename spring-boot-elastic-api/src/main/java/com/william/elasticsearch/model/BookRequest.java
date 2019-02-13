package com.william.elasticsearch.model;

import lombok.Data;

/**
 * @author: chaiz
 * @Description: TODO
 * @date: 2018年12月12日 上午11:45:21
 * @version: v1.0.0
 */
@Data
public class BookRequest extends BasePage{

	private String id;
	
	private String title;
	
	private String author;
	
	private Integer wordCount;
	
	private String publishDate;
	
	private Integer edition;
	
	private String startdate;
	
	private String enddate;
}
