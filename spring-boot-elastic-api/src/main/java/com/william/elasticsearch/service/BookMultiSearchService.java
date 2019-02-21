package com.william.elasticsearch.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.MultiSearchResponse.Item;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.william.elasticsearch.conf.EsConfig;
import com.william.elasticsearch.model.BookRequest;
import com.william.elasticsearch.model.ResponseVo;

/**
 * @author: william
 * @Description: TODO
 * @date: 2019年2月14日 下午5:51:51
 * @version: v1.0.0
 */
@Service
public class BookMultiSearchService {

	@Autowired
	private EsConfig esconfig;

    @Autowired
    private RestHighLevelClient client;
    
    @Autowired
    private TransportClient tranClient;
    
	
	public ResponseVo multiSearch(BookRequest bookRequest) throws IOException {
		
		MultiSearchRequest multiRequest = new MultiSearchRequest();
		SearchRequest firstSearchRequest = new SearchRequest();   
		firstSearchRequest.indices(esconfig.getIndex());
		firstSearchRequest.types(esconfig.getType());
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery("title", "Python"));
		firstSearchRequest.source(searchSourceBuilder);
		multiRequest.add(firstSearchRequest); 
		
		SearchRequest secondSearchRequest = new SearchRequest();
		secondSearchRequest.indices(esconfig.getIndex());
		secondSearchRequest.types(esconfig.getType());
		searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery("title", "java"));
		secondSearchRequest.source(searchSourceBuilder);
		multiRequest.add(secondSearchRequest);
		// 此处要传Header
		MultiSearchResponse response = this.client.multiSearch(multiRequest);
		List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
		for (Item item : response) {
			long count = item.getResponse().getHits().getTotalHits();
			System.out.println(count);
			for (SearchHit searchHit : item.getResponse().getHits()) {
			    list.add(searchHit.getSourceAsMap());
			}
			
		}

		return ResponseVo.success(list, 2);
	}
	
}
