package com.william.elasticsearch.service;

import java.io.IOException;

import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.william.elasticsearch.conf.EsConfig;
import com.william.elasticsearch.model.BookRequest;

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
	
    /**
     * @Title: search
     * @Description: 搜索
     * @param bookRequest
     * @return
     * @throws IOException
     */
	public SearchResponse search(BookRequest bookRequest) throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引，索引可以为多个
		searchRequest.indices(esconfig.getIndex());
		// 设置类型
		searchRequest.types(esconfig.getType());
		SearchSourceBuilder build = new SearchSourceBuilder();
		//设置查询起始
		int  index = (bookRequest.getPageindex()-1)*bookRequest.getPagesize();
//		build.from(index);	// 查询起始  (pageIndex-1)*pageSize
//		build.size(bookRequest.getPagesize());	// 每页大小
		// 设置自动过滤
		String[] includes = new String[] {"author","*e"};
    	String[] excludes = new String[] {"publishDate"};
		FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, Strings.EMPTY_ARRAY);
//		build.fetchSource(fetchSourceContext);
		// 设置排序规则
		build.sort("publishDate", SortOrder.DESC);
		// 设置超时时间
		build.timeout(TimeValue.timeValueMillis(2000));
		// 设置查询条件
//		build.query(QueryBuilders.matchAllQuery());
		MatchPhraseQueryBuilder matbuild = QueryBuilders.matchPhraseQuery("type", "java");
		MatchPhraseQueryBuilder matbuild2 = QueryBuilders.matchPhraseQuery("edition", 3);
		RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("publishDate").from("2008-08-01").includeLower(false).to("2014-06-15");
		BoolQueryBuilder boolbuild = new BoolQueryBuilder();
		boolbuild.must(matbuild).must(matbuild2).must(rangeQueryBuilder);
		build.query(boolbuild);
		searchRequest.source(build);
		
		SearchResponse response = this.client.search(searchRequest);
		return response;
	}
	
	
}
