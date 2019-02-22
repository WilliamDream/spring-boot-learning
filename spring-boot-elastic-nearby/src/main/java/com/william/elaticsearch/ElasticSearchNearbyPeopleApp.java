package com.william.elaticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author: william
 * @Description: 基于ES geo实现搜索附近的人
 * @date: 2019年2月21日 下午18:27:30
 * @version: v1.0.0
 */
@SpringBootApplication
public class ElasticSearchNearbyPeopleApp {
	 public static void main( String[] args ){
		 SpringApplication.run(ElasticSearchNearbyPeopleApp.class, args);
	 }
}
