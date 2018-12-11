package com.william.elasticsearch.service;

import java.io.IOException;
import java.net.Authenticator.RequestorType;
import java.util.List;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.omg.PortableInterceptor.RequestInfoOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.william.elasticsearch.conf.EsConfig;
import com.william.elasticsearch.model.Book;

/**
 * @author: william
 * @Description: TODO
 */

@Service
public class BookService {

	@Autowired
	private EsConfig esconfig;

    @Autowired
    private RestHighLevelClient client;
    
    /**
     * @throws IOException 
     * @Title: CreateIndex
     * @Description: 创建索引
     */
    public CreateIndexResponse CreateIndex(String indexName) throws IOException {
    	CreateIndexRequest request = new CreateIndexRequest(indexName);
    	request.settings(Settings.builder().put("number_of_shards", 3)	// 分片数
    		.put("number_of_replicas", 1));			// 副本数
    	// 3、设置索引的mappings
    	// 方式一
        request.mapping("_doc",
                "  {\n" +
                "    \"_doc\": {\n" +
                "      \"properties\": {\n" +
                "        \"message\": {\n" +
                "          \"type\": \"text\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }",
                XContentType.JSON);
    	
    	// 方式二
/*    	XContentBuilder  builder = XContentFactory.jsonBuilder();
    	builder.startObject();
    	{
    		builder.startObject("_doc");
    		{
    			builder.startObject("properties");
    			{
    				builder.startObject("message");
    				{
    					builder.field("type");
    				}
    				builder.endObject();
    			}
    			builder.endObject();
    		}
    		builder.endObject();
    	}
    	builder.endObject();
    	request.mapping("_doc", builder);*/
    	CreateIndexResponse response = this.client.indices().create(request);
    	System.out.println(response.isAcknowledged()+"--"+response.isShardsAcknowledged());
    	return response;
    }
    
    
    /**
     * @Title: checkIndexExists
     * @Description: 检查索引是否存在
     * @param indexName
     * @return
     * @throws IOException
     */
    public boolean checkIndexExists(String indexName) throws IOException {
    	GetIndexRequest request = new GetIndexRequest();
    	request.indices(indexName);
		return this.client.indices().exists(request);
    }
    
    /**
     * @Title: deleteIndex
     * @Description: 删除索引
     * @param indexName
     * @return
     * @throws IOException 
     */
    public DeleteIndexResponse deleteIndex(String indexName) throws IOException {
    	DeleteIndexRequest request = new DeleteIndexRequest(indexName);
    	// 可选参数
    	request.timeout(TimeValue.timeValueMillis(5000));
    	// 方式一：同步删除
    	DeleteIndexResponse response = this.client.indices().delete(request);
    	// 方式二：异步删除
    	/*ActionListener<DeleteIndexResponse> listener =
    	        new ActionListener<DeleteIndexResponse>() {
    	    @Override
    	    public void onResponse(DeleteIndexResponse deleteIndexResponse) {
    	        
    	    }

    	    @Override
    	    public void onFailure(Exception e) {
    	        
    	    }
    	};
    	this.client.indices().deleteAsync(request, listener);*/
    	return response;
    }
    
    /**
     * @Title: addNewBook
     * @Description: 新增doc
     * @param book
     * @return
     * @throws IOException
     */
    public IndexResponse addNewBook(Book book) throws IOException {
    	// 方式一
		XContentBuilder builder = XContentFactory.jsonBuilder()
				.startObject()
				.field("title",book.getTitle())
	            .field("author",book.getAuthor())
	            .field("word_count",book.getWordCount())
	            .field("publish_date",book.getPublishDate())
				.endObject();
		
		IndexRequest request = new IndexRequest(esconfig.getIndex(),esconfig.getType(),book.getId())
				.source(builder).opType(DocWriteRequest.OpType.CREATE);
		// 方式二 json格式		
		/*Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("user", "kimchy");
		jsonMap.put("postDate", new Date());
		jsonMap.put("message", "trying out Elasticsearch");
		IndexRequest indexRequest = new IndexRequest("posts", "doc", "1")
		        .source(jsonMap);*/		
		IndexResponse response = this.client.index(request);
		return response;
    }
    
    /**
     * @Title: getDocById
     * @Description: 通过ID获取文档
     * @param docId
     * @return
     * @throws IOException
     */
    public GetResponse getDocById(String docId) throws IOException {
    	GetRequest request = new GetRequest(esconfig.getIndex(),esconfig.getType(),docId);
    	/**
    	 * FetchSourceContext 顾名思义，就是fetch source的上下文环境，提供更加完善的过滤逻辑，主要特性为支持include、exclude和支持通配符过滤。
    	 * 
    	 */
    	//1 
//    	request.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE);
    	//2 指定那些属性返回，那些属性过滤掉
//    	String[] includes = new String[] {"author","*e"};
//    	String[] excludes = new String[] {"publish_date"};
//    	FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
//    	request.fetchSourceContext(fetchSourceContext);
    	
    	GetResponse response = this.client.get(request);
    	return response;
    } 

    /**
     * @Title: checkDocExists
     * @Description: 验证文档是否存在
     * @param docId
     * @return
     * @throws IOException 
     */
    public boolean checkDocExists(String docId) throws IOException {
    	GetRequest request = new GetRequest(esconfig.getIndex(),esconfig.getType(),docId);
    	request.fetchSourceContext(new FetchSourceContext(false));
    	
    	return this.client.exists(request);
    }
	
    /**
     * @Title: delDocById
     * @Description: 删除文档
     * @param docId
     * @return
     * @throws IOException
     */
    public DeleteResponse delDocById(String docId) throws IOException {
    	DeleteRequest request = new DeleteRequest(esconfig.getIndex(),esconfig.getType(),docId);
    	DeleteResponse response = this.client.delete(request);
    	return response;
    }
	
    /**
     * @Title: updateDocById
     * @Description: 根据ID更新文档
     * @param book
     * @return
     * @throws IOException
     */
	public UpdateResponse updateDocById(Book book) throws IOException {
		XContentBuilder builder = new XContentFactory().jsonBuilder()
				.startObject();
				if(StringUtils.isEmpty(book.getTitle())){
		            builder.field("title",book.getTitle());
		        }
		        if(StringUtils.isEmpty(book.getAuthor())){
		            builder.field("author",book.getAuthor());
		        }
		        if(StringUtils.isEmpty(book.getPublishDate())){
		            builder.field("publish_date",book.getPublishDate());
		        }
		        if(book.getWordCount()!=null) {
		        	 builder.field("word_count",book.getWordCount());
		        }
		        builder.endObject();
		        
		        UpdateRequest request = new UpdateRequest(esconfig.getIndex(),esconfig.getType(),book.getId());
		        request.doc(builder);
		        UpdateResponse response = this.client.update(request);
				return response;
	}
	
	/**
	 * @Title: bulkIndex
	 * @Description: 创建多个文档
	 * @param ids
	 * @return
	 * @throws IOException
	 */
	public BulkResponse bulkIndex(String ids) throws IOException {
		String[] idsarr = ids.split(",");
		BulkRequest request = new BulkRequest();
		for (int i = 0; i < idsarr.length; i++) {
			/*Book book = booklist.get(i);
			XContentBuilder builder = new XContentFactory().jsonBuilder()
					.startObject()
					.field("title",book.getTitle())
		            .field("author",book.getAuthor())
		            .field("word_count",book.getWordCount())
		            .field("publish_date",book.getPublishDate())
			        .endObject();*/
			request.add(new IndexRequest(esconfig.getIndex(),esconfig.getType(),idsarr[i])
					.source(XContentType.JSON,"titile", "SpringBoot 2.0"));
		}
		BulkResponse response = this.client.bulk(request);
		return response;
	}
	
	/**
	 * @Title: bulkDel
	 * @Description: 删除多个文档
	 * @param ids
	 * @return
	 * @throws IOException
	 */
	public BulkResponse bulkDel(String ids) throws IOException {
		String[] idarr = ids.split(",");
		BulkRequest request = new BulkRequest();
		for (int i = 0; i < idarr.length; i++) {
			request.add(new DeleteRequest(esconfig.getIndex(),esconfig.getType(),idarr[i]));
		}
		BulkResponse response = this.client.bulk(request);
		return response;
	}
	
	
}
