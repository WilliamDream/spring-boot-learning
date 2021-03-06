package com.william.elasticsearchaggs.service;

import java.io.IOException;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.min.MinAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.william.elasticsearchaggs.conf.EsConfig;

/**
 * @author: william
 * @Description: TODO
 * @date: 2019年2月28日 下午3:30:14
 * @version: v1.0.0
 */
public class BooksAggsService {

	@Autowired
	private EsConfig esconfig;
	
	@Autowired
	private RestHighLevelClient client;
	
	/**
	 * @Title: getUserAgeSum
	 * @Description: 获取年龄之和
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public SearchResponse getUserAgeSum() throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引，索引可以为多个
		searchRequest.indices(esconfig.getIndex());
		// 设置类型，类型也可以为多个
		searchRequest.types(esconfig.getType());
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.size(0);	// 不返回文档，只返回聚合结果
		//对age进行分组查询：select age,count(*) from wechat group by age 
		SumAggregationBuilder sumbuild = AggregationBuilders.sum("age_sum").field("age");
		//不需要解释
		sourceBuilder.explain(false);
		//不需要原始数据
		sourceBuilder.fetchSource(false);
		//不需要版本
		sourceBuilder.version(false);
		
		sourceBuilder.aggregation(sumbuild);
		searchRequest.source(sourceBuilder);
		System.out.println(searchRequest.source().toString());
		SearchResponse response = this.client.search(searchRequest);
		return response;
	}
	
	/**
	 * select gender,avg(age) as province_count from userinfo group by gender
	 * @Title: UserInfoAvgAge
	 * @Description: 获取平均年龄
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("deprecation")
	public SearchResponse getUserAvgAge() throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引，索引可以为多个
		searchRequest.indices(esconfig.getIndex());
		// 设置类型，类型也可以为多个
		searchRequest.types(esconfig.getType());
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.size(0);	// 不返回文档，只返回聚合结果
		AvgAggregationBuilder termsbuild = AggregationBuilders.avg("avg_age").field("age");
		//不需要解释
		sourceBuilder.explain(false);
		//不需要原始数据
		sourceBuilder.fetchSource(false);
		//不需要版本
		sourceBuilder.version(false);
		
		sourceBuilder.aggregation(termsbuild);
		searchRequest.source(sourceBuilder);
		System.out.println(searchRequest.source().toString());
		SearchResponse response = this.client.search(searchRequest);
		return response;
	}
	
	/**
	 * select gender,count(*) from wechat group by gender
	 * @Title: getUserCountGpGender
	 * @Description: 根据性别进行分组
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public SearchResponse getUserCountGpGender() throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引，索引可以为多个
		searchRequest.indices(esconfig.getIndex());
		// 设置类型，类型也可以为多个
		searchRequest.types(esconfig.getType());
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		//对gender进行分组查询：select gender,count(*) from wechat group by gender 
		TermsAggregationBuilder termsbuild = AggregationBuilders.terms("user_count").field("gender");
		sourceBuilder.size(0);	// 不返回文档，只返回聚合结果
		//不需要解释
		sourceBuilder.explain(false);
		//不需要原始数据
		sourceBuilder.fetchSource(false);
		//不需要版本
		sourceBuilder.version(false);
		
		sourceBuilder.aggregation(termsbuild);
		searchRequest.source(sourceBuilder);
		System.out.println(searchRequest.source().toString());
		SearchResponse response = this.client.search(searchRequest);
		return response;
	}
	

	
	/**
	 * @Title: userInfoGroupByAge
	 * @Description: 获取最大最小年龄
	 * @return
	 * @throws IOException
	 */
	public SearchResponse getMaxMinAge() throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引，索引可以为多个
		searchRequest.indices(esconfig.getIndex());
		// 设置类型，类型也可以为多个
		searchRequest.types(esconfig.getType());
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		MinAggregationBuilder minbuild = AggregationBuilders.min("min_age").field("age");
		MaxAggregationBuilder maxbuild = AggregationBuilders.max("max_age").field("age");
		sourceBuilder.size(0);	// 不返回文档，只返回聚合结果
		//不需要解释
		sourceBuilder.explain(false);
		//不需要原始数据
		sourceBuilder.fetchSource(false);
		//不需要版本
		sourceBuilder.version(false);
		
		sourceBuilder.aggregation(minbuild).aggregation(maxbuild);
		searchRequest.source(sourceBuilder);
		System.out.println(searchRequest.source().toString());
		SearchResponse response = this.client.search(searchRequest);
		
		return response;
	}
	
}
