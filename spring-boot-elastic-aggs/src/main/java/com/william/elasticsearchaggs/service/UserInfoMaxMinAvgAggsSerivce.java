package com.william.elasticsearchaggs.service;

import java.io.IOException;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.william.elasticsearchaggs.conf.EsConfig;


/**
 * @author: william
 * @Description: avg,max,min聚合
 * @date: 2019年2月26日 上午10:22:15
 * @version: v1.0.0
 */
@Service
public class UserInfoMaxMinAvgAggsSerivce {

	@Autowired
	private EsConfig esconfig;
	
	@Autowired
	private RestHighLevelClient client;
	
	/**
	 * select province,count(*) as province_count from userinfo group by province
	 * @Title: UserInfoAvgAge
	 * @Description: TODO
	 * @return
	 * @throws IOException 
	 */
	public SearchResponse userInfoAvgAge() throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引，索引可以为多个
		searchRequest.indices(esconfig.getIndex());
		// 设置类型，类型也可以为多个
		searchRequest.types(esconfig.getType());
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		TermsAggregationBuilder termsbuild = AggregationBuilders.terms("province_count").field("province");
		//不需要解释
		sourceBuilder.explain(false);
		//不需要原始数据
		sourceBuilder.fetchSource(false);
		//不需要版本
		sourceBuilder.version(false);
		
		sourceBuilder.aggregation(termsbuild);
		searchRequest.source(sourceBuilder);
		SearchResponse response = this.client.search(searchRequest);
		
		return response;
	}
	
   
	
}
