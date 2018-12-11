package com.william.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot整合ES实现CRUD
 *
 */

@SpringBootApplication
public class ElasticSearchApp {
    public static void main( String[] args ){
    	SpringApplication.run(ElasticSearchApp.class, args);
    }
}
