package com.william.elasticsearch.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhrasePrefixQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
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
 */
@Service
public class BookSearchApiService {

	@Autowired
	private EsConfig esconfig;

    @Autowired
    private RestHighLevelClient client;
	
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
		return response;
    }
    
    /**
     * @Title: search
     * @Description: 搜索,组合查询
     * @param bookRequest
     * @return
     * @throws IOException
     */
	public SearchResponse search(BookRequest bookRequest) throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		// 设置索引，索引可以为多个
		searchRequest.indices(esconfig.getIndex());
		// 设置类型，类型也可以为多个
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
		TermsQueryBuilder termbuild = QueryBuilders.termsQuery("type", "java");
		MatchQueryBuilder matbuild = QueryBuilders.matchQuery("type", "java");
		MatchQueryBuilder matbuild2 = QueryBuilders.matchQuery("edition", 3);
		// 按照时间范围查询
		RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("publishDate")
				.from("2008-08-01").includeLower(false)	// 相当于>
				.to("2012-06-15").includeUpper(true);	// 相当于<=
		
		MatchQueryBuilder matbuild3 = QueryBuilders.matchQuery("title", "Spring")
				// 启用模糊查询
				.fuzziness(Fuzziness.AUTO)
				// 匹配查询前缀长度
				.prefixLength(3)
				// 设置最大扩展选项以控制查询的模糊过程
				.maxExpansions(20);
		
		BoolQueryBuilder boolbuild = new BoolQueryBuilder();
		boolbuild
		.must(matbuild)
		.must(matbuild2)
		.must(rangeQueryBuilder);
//		.must(matbuild3);
		build.query(boolbuild);
		searchRequest.source(build);
		SearchResponse response = this.client.search(searchRequest);
		return response;
	}
	
	/**
	 * @Title: searchMulti
	 * @Description: TODO
	 * @param bookRequest
	 * @return
	 * @throws IOException
	 */
	public SearchResponse searchMulti(BookRequest bookRequest) throws IOException {
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
		QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery("", "","");
		
		BoolQueryBuilder boolbuild = new BoolQueryBuilder();
		boolbuild.must(queryBuilder);
		build.query(boolbuild);
		searchRequest.source(build);
		SearchResponse response = this.client.search(searchRequest);
		return response;
		
	}
	
	
	public SearchResponse search1(BookRequest bookRequest) throws IOException {
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
		
		MatchQueryBuilder matbuild3 = QueryBuilders.matchQuery("title", "Spring")
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
		return response;
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
	public SearchResponse zhuheSearch(BookRequest bookRequest) throws IOException {
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
		
		TermQueryBuilder tqb1 = QueryBuilders.termQuery("title", bookRequest.getTitle());
		TermQueryBuilder tqb2 = QueryBuilders.termQuery("edition", bookRequest.getEdition());
		TermQueryBuilder tqb3 = QueryBuilders.termQuery("wordCount", bookRequest.getWordCount());

		BoolQueryBuilder matbuild3 = QueryBuilders.boolQuery()
				.must(tqb1)
				.mustNot(tqb2)
				.should(tqb3);
		
		build.query(matbuild3);
		searchRequest.source(build);
		SearchResponse response = this.client.search(searchRequest);
		return response;
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
		// 设置查询条件
		MatchPhrasePrefixQueryBuilder pqb = QueryBuilders.matchPhrasePrefixQuery("title", "Elastic");

		BoolQueryBuilder matbuild3 = QueryBuilders.boolQuery()
				.must(pqb);
		
		build.query(matbuild3);
		searchRequest.source(build);
		SearchResponse response = this.client.search(searchRequest);
		List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
        for (SearchHit searchHit : response.getHits()) {
            list.add(searchHit.getSourceAsMap());
        }
        long count = response.getHits().getTotalHits();
		return ResponseVo.success(list, count);
	}
	
	
	private List<Map<String, Object>> searchFunction(SearchResponse response) {
		long totalHits = response.getHits().getTotalHits();
		List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
        for (SearchHit searchHit : response.getHits()) {
            list.add(searchHit.getSourceAsMap());
            return list;
        }
        return list;
    }
	
}
