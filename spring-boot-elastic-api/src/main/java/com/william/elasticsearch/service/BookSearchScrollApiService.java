package com.william.elasticsearch.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.william.elasticsearch.conf.EsConfig;
import com.william.elasticsearch.model.BookRequest;
import com.william.elasticsearch.model.ResponseVo;

/**
 * @author: william
 * @Description: TODO
 * @date: 2019年2月14日 上午9:30:57
 * @version: v1.0.0
 */
@Service
public class BookSearchScrollApiService {

	@Autowired
	private EsConfig esconfig;

    @Autowired
    private RestHighLevelClient client;
    
    final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
    
	public ResponseVo searchScroll(BookRequest bookRequest) throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引，索引可以为多个
		searchRequest.indices(esconfig.getIndex());
		// 设置类型，类型也可以为多个
		searchRequest.types(esconfig.getType());
		searchRequest.scroll(scroll);
		SearchSourceBuilder build = new SearchSourceBuilder();
		//设置查询起始
//		int  index = (bookRequest.getPageindex()-1)*bookRequest.getPagesize();
//		build.from(index);	// 查询起始  (pageIndex-1)*pageSize
//		build.size(bookRequest.getPagesize());	// 每页大小

		// 设置排序规则
		build.sort("publishDate", SortOrder.DESC);
		// 设置超时时间
		build.timeout(TimeValue.timeValueMillis(2000));
		MatchQueryBuilder matbuild = QueryBuilders.matchQuery("edition", bookRequest.getEdition());
		
		BoolQueryBuilder boolbuild = new BoolQueryBuilder();
		boolbuild.must(matbuild);
		build.size(10000);
		build.query(boolbuild);
		searchRequest.source(build);
		SearchResponse response = this.client.search(searchRequest);
		//读取返回的滚动id，它指向保持活动的搜索上下文，并且在接下来的搜索滚动调用中需要它
		String scrollId = response.getScrollId();
		List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
		SearchHit[] searchHits = response.getHits().getHits();

		for (SearchHit searchHit : response.getHits()) {
		    SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId); 
		    scrollRequest.scroll(scroll);
		    response = client.searchScroll(scrollRequest);
		    scrollId = response.getScrollId();
		    searchHits = response.getHits().getHits();
		    list.add(searchHit.getSourceAsMap());
		}
		ClearScrollRequest clearScrollRequest = new ClearScrollRequest(); 
		clearScrollRequest.addScrollId(scrollId);
		ClearScrollResponse clearScrollResponse = client.clearScroll(clearScrollRequest);
		boolean succeeded = clearScrollResponse.isSucceeded();
        long count = response.getHits().getTotalHits();
		return ResponseVo.success(list, count);
	}
	
	
}
