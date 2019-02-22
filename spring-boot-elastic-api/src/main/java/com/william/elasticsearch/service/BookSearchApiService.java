package com.william.elasticsearch.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhrasePrefixQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.RegexpQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.william.elasticsearch.conf.EsConfig;
import com.william.elasticsearch.model.BookRequest;
import com.william.elasticsearch.model.ResponseVo;

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
	
//    @Autowired
//    private TransportClient tranClient;
    
    /**
     * @Title: searchByid
     * @Description: 根据ID查询
     * @param id
     * @return
     * @throws IOException
     */
    public SearchResponse searchByid(String id) throws IOException {
    	SearchRequest searchRequest = new SearchRequest();
		// 设置索引，索引可以为多个
		searchRequest.indices(esconfig.getIndex());
		// 设置类型，类型也可以为多个
		searchRequest.types(esconfig.getType());
		SearchSourceBuilder build = new SearchSourceBuilder();
    	QueryBuilder builder = (QueryBuilder) QueryBuilders.idsQuery().addIds(id);
		
		build.query(builder);
		searchRequest.source(build);
		SearchResponse response = this.client.search(searchRequest);
		// 也可以使用TransportClient客户端
//		ActionFuture<SearchResponse> searchresponse = this.tranClient.search(searchRequest);
//		SearchResponse res = searchresponse.actionGet();
		return response;
    }
    
    /**
     * @Title: search
     * @Description: 搜索,组合查询
     * @param bookRequest
     * @return
     * @throws IOException
     */
	public ResponseVo search(BookRequest bookRequest) throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引，索引可以为多个
		searchRequest.indices(esconfig.getIndex());
		// 设置类型，类型也可以为多个
		searchRequest.types(esconfig.getType());
		SearchSourceBuilder build = new SearchSourceBuilder();
		//设置查询起始
		int  index = (bookRequest.getPageindex()-1)*bookRequest.getPagesize();
		build.from(index);	// 查询起始  (pageIndex-1)*pageSize
		build.size(bookRequest.getPagesize());	// 每页大小
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
		MatchQueryBuilder matbuild = QueryBuilders.matchQuery("title", bookRequest.getTitle());
		MatchQueryBuilder matbuild1 = QueryBuilders.matchQuery("edition", bookRequest.getEdition());
		// 按照时间范围查询
		RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("publishDate")
				.from(bookRequest.getStartdate()).includeLower(false)	// 相当于>
				.to(bookRequest.getEnddate()).includeUpper(true);		// 相当于<=
		BoolQueryBuilder boolbuild = new BoolQueryBuilder();	//等价于：BoolQueryBuilder boolbuild = QueryBuilders.boolQuery();
		boolbuild
		.must(matbuild)			//  and 
//		.mustNot(matbuild1)		//  not
		.must(rangeQueryBuilder);
		build.query(boolbuild);
		searchRequest.source(build);
		SearchResponse response = this.client.search(searchRequest);
		List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
        for (SearchHit searchHit : response.getHits()) {
            list.add(searchHit.getSourceAsMap());
        }
        long count = response.getHits().getTotalHits();
        return ResponseVo.success(list, count);
	}
	
	/**
	 * @Title: multiMatch
	 * @Description: 多字段匹配
	 * @param bookRequest
	 * @return
	 * @throws IOException
	 */
	public ResponseVo multiMatch(BookRequest bookRequest) throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引，索引可以为多个
		searchRequest.indices(esconfig.getIndex());
		// 设置类型，类型也可以为多个
		searchRequest.types(esconfig.getType());
		SearchSourceBuilder build = new SearchSourceBuilder();
		//设置查询起始
//		int  index = (bookRequest.getPageindex()-1)*bookRequest.getPagesize();
//		build.from(index);	// 查询起始  (pageIndex-1)*pageSize
//		build.size(bookRequest.getPagesize());	// 每页大小
		// 设置排序规则
		build.sort("publishDate", SortOrder.DESC);
		// 设置超时时间
		build.timeout(TimeValue.timeValueMillis(2000));
		//QueryBuilders.multiMatchQuery(Object text, String... fieldNames);		text要匹配的内容，fieldNames要匹配那些字段
		QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(bookRequest.getMatchText(), "title","desc");
		
		BoolQueryBuilder boolbuild = new BoolQueryBuilder();
		boolbuild.must(queryBuilder);
		build.query(boolbuild);
		searchRequest.source(build);
		SearchResponse response = this.client.search(searchRequest);
		List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
        for (SearchHit searchHit : response.getHits()) {
            list.add(searchHit.getSourceAsMap());
        }
        long count = response.getHits().getTotalHits();
		return ResponseVo.success(list, count);
	}
	
	
	public ResponseVo search1(BookRequest bookRequest) throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引，索引可以为多个
		searchRequest.indices(esconfig.getIndex());
		// 设置类型，类型也可以为多个
		searchRequest.types(esconfig.getType());
		SearchSourceBuilder build = new SearchSourceBuilder();
		//设置查询起始
		int  index = (bookRequest.getPageindex()-1)*bookRequest.getPagesize();
		build.from(index);	// 查询起始  (pageIndex-1)*pageSize
		build.size(bookRequest.getPagesize());	// 每页大小
		// 设置排序规则
		build.sort("publishDate", SortOrder.DESC);
		// 设置超时时间
		build.timeout(TimeValue.timeValueMillis(2000));
		// 设置查询条件
//		build.query(QueryBuilders.matchAllQuery());
		
		MatchQueryBuilder matbuild3 = QueryBuilders.matchQuery("title", bookRequest.getTitle())
				// 启用模糊查询
				.fuzziness(Fuzziness.AUTO)
				// 匹配查询前缀长度
				.prefixLength(6)
				// 设置最大扩展选项以控制查询的模糊过程
				.maxExpansions(30);
		
		BoolQueryBuilder boolbuild = new BoolQueryBuilder();
		boolbuild
//		.must(matbuild)
//		.must(matbuild2)
//		.must(rangeQueryBuilder)
		.must(matbuild3);
		build.query(boolbuild);
		searchRequest.source(build);
		SearchResponse response = this.client.search(searchRequest);
		List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
        for (SearchHit searchHit : response.getHits()) {
            list.add(searchHit.getSourceAsMap());
        }
        long count = response.getHits().getTotalHits();
		return ResponseVo.success(list, count);
	}
	
	/**
	 * @Title: likeSearch
	 * @Description: 根据title进行模糊查询，
	 * @param bookRequest
	 * @return
	 * @throws IOException
	 */
	public ResponseVo likeSearch(BookRequest bookRequest) throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引，索引可以为多个
		searchRequest.indices(esconfig.getIndex());
		// 设置类型，类型也可以为多个
		searchRequest.types(esconfig.getType());
		SearchSourceBuilder build = new SearchSourceBuilder();
		//设置查询起始
		int  index = (bookRequest.getPageindex()-1)*bookRequest.getPagesize();
		build.from(index);	// 查询起始  (pageIndex-1)*pageSize
		build.size(bookRequest.getPagesize());	// 每页大小
		// 设置排序规则
		build.sort("publishDate", SortOrder.DESC);
		// 设置超时时间
		build.timeout(TimeValue.timeValueMillis(2000));
		// 设置查询条件
		// 使用wildcardQuery必须将关键词转换为小写
		WildcardQueryBuilder matbuild3 = QueryBuilders.wildcardQuery("title", "*"+bookRequest.getTitle().toLowerCase()+"*");
		
		BoolQueryBuilder boolbuild = new BoolQueryBuilder();
		boolbuild.must(matbuild3);
		build.query(boolbuild);
		searchRequest.source(build);
		SearchResponse response = this.client.search(searchRequest);
		List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
        for (SearchHit searchHit : response.getHits()) {
            list.add(searchHit.getSourceAsMap());
        }
        long count = response.getHits().getTotalHits();
		return ResponseVo.success(list, count);
	}
	
	/**
	 * @Title: zhuheSearch
	 * @Description: 组合查询
	 * @param bookRequest
	 * @return
	 * @throws IOException
	 */
	public ResponseVo zhuheSearch(BookRequest bookRequest) throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引，索引可以为多个
		searchRequest.indices(esconfig.getIndex());
		// 设置类型，类型也可以为多个
		searchRequest.types(esconfig.getType());
		SearchSourceBuilder build = new SearchSourceBuilder();
//		//设置查询起始
//		int  index = (bookRequest.getPageindex()-1)*bookRequest.getPagesize();
//		build.from(index);	// 查询起始  (pageIndex-1)*pageSize
//		build.size(bookRequest.getPagesize());	// 每页大小
		// 设置排序规则
		build.sort("publishDate", SortOrder.DESC);
		// 设置超时时间
		build.timeout(TimeValue.timeValueMillis(2000));
		// 设置查询条件
		/**
		 * QueryBuilders.termQuery("field","");				用于精确查询
		 * QueryBuilders.termsQuery("field","","");			用于精确匹配多个值，相当于sql 语句：select * from books where type in in ("java","python")
		 * 
		 */
		TermQueryBuilder tqb1 = QueryBuilders.termQuery("title", bookRequest.getTitle());
		TermQueryBuilder tqb2 = QueryBuilders.termQuery("edition", bookRequest.getEdition());
		TermQueryBuilder tqb3 = QueryBuilders.termQuery("wordCount", bookRequest.getWordCount());

		BoolQueryBuilder boolbuild = QueryBuilders.boolQuery()
				.must(tqb1)
				/*.mustNot(tqb2)
				.should(tqb3)*/;
		
		build.query(boolbuild);
		searchRequest.source(build);
		SearchResponse response = this.client.search(searchRequest);
		List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
        for (SearchHit searchHit : response.getHits()) {
            list.add(searchHit.getSourceAsMap());
        }
        long count = response.getHits().getTotalHits();
		return ResponseVo.success(list, count);
	}
	
	/**
	 * @Title: prefixQuery
	 * @Description: 根据前缀进行匹配查询
	 * @param bookRequest
	 * @return
	 * @throws IOException
	 */
	public ResponseVo prefixQuery(BookRequest bookRequest) throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引，索引可以为多个
		searchRequest.indices(esconfig.getIndex());
		// 设置类型，类型也可以为多个
		searchRequest.types(esconfig.getType());
		SearchSourceBuilder build = new SearchSourceBuilder();
//		//设置查询起始
//		int  index = (bookRequest.getPageindex()-1)*bookRequest.getPagesize();
//		build.from(index);	// 查询起始  (pageIndex-1)*pageSize
//		build.size(bookRequest.getPagesize());	// 每页大小
		// 设置排序规则
		build.sort("publishDate", SortOrder.DESC);
		// 设置超时时间
		build.timeout(TimeValue.timeValueMillis(2000));
		// 根据前缀进行匹配检索
		MatchPhraseQueryBuilder pqb = QueryBuilders.matchPhraseQuery("title", bookRequest.getTitle());
		// slop参数告诉match_phrase_prefix查询词条之间相隔多远时仍然将文档视为匹配。一个词为一个slop，默认slop为0
		pqb.slop(3)
		/*.maxExpansions(50)*/;
		
		BoolQueryBuilder boolbuild = QueryBuilders.boolQuery()
				.must(pqb);
		
		build.query(boolbuild);
		searchRequest.source(build);
		System.out.println(searchRequest.source());
		SearchResponse response = this.client.search(searchRequest);
		List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
        for (SearchHit searchHit : response.getHits()) {
            list.add(searchHit.getSourceAsMap());
        }
        long count = response.getHits().getTotalHits();
		return ResponseVo.success(list, count);
	}
	
	/**
	 * @Title: reqexpMatch
	 * @Description: 正则匹配查询,正则匹配field
	 * @param bookRequest
	 * @return
	 * @throws IOException
	 */
	public ResponseVo reqexpMatch(BookRequest bookRequest) throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引，索引可以为多个
		searchRequest.indices(esconfig.getIndex());
		// 设置类型，类型也可以为多个
		searchRequest.types(esconfig.getType());
		SearchSourceBuilder build = new SearchSourceBuilder();
		// QueryBuilders.regexpQuery(String field, String regexp);
		RegexpQueryBuilder reqexpbuild = QueryBuilders.regexpQuery(bookRequest.getField(), bookRequest.getMatchText()); 
		build.query(reqexpbuild);
		searchRequest.source(build);
		System.out.println(searchRequest.toString());
		SearchResponse response = this.client.search(searchRequest);
		List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
        for (SearchHit searchHit : response.getHits()) {
            list.add(searchHit.getSourceAsMap());
        }
        long count = response.getHits().getTotalHits();
		return ResponseVo.success(list, count);
	}


	
	
}
