package com.william.elasticsearch.service;

import java.io.IOException;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.william.elasticsearch.conf.EsConfig;

/**
 * @author: william
 * @Description: TODO
 */
@Service
public class BookSearchApiService {

	@Autowired
	private EsConfig esconfig;

    @Autowired
    private RestHighLevelClient client;
	
	public SearchResponse search() throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices(esconfig.getIndex());
		searchRequest.types(esconfig.getType());
		SearchSourceBuilder build = new SearchSourceBuilder();
		build.query(QueryBuilders.matchAllQuery());
		searchRequest.source(build);
		
		SearchResponse response = this.client.search(searchRequest);
		return response;
	}
}
