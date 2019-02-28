package com.william.elasticsearchaggs.service;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.william.elasticsearchaggs.conf.EsConfig;

/**
 * @author: william
 * @Description: TODO
 * @date: 2019年2月28日 下午2:32:44
 * @version: v1.0.0
 */
@Service
public class BookGenerateDateService {

	@Autowired
	private EsConfig esconfig;
	
	@Autowired
	private RestHighLevelClient client;
	
	
    public AcknowledgedResponse createMapping() throws IOException {
    	CreateIndexRequest request = new CreateIndexRequest();
    	request.settings(Settings.builder().put("number_of_shards", 3)	// 分片数
    		.put("number_of_replicas", 1));			// 副本数
    	// 3、设置索引的mappings
    	XContentBuilder  builder = XContentFactory.jsonBuilder();
        builder
        .startObject()
    		.startObject(esconfig.getType())
	            .startObject("properties")
	                .startObject("title").field("type", "text").endObject()
	                .startObject("author").field("type", "text").endObject()
	                .startObject("type").field("type","keyword").endObject()
	                .startObject("edition").field("type", "integer").endObject()
	                .startObject("publishDate").field("type", "date").endObject()
	                .startObject("price").field("type", "double").endObject()
	                .startObject("sales").field("type", "integer").endObject()
	                .startObject("desc").field("type", "text").endObject()
		        .endObject()
	        .endObject()
	    .endObject();
    	request.mapping(esconfig.getIndex(), builder);
    	PutMappingRequest putMapping = Requests.putMappingRequest(esconfig.getIndex()).type(esconfig.getType()).source(builder);
    	AcknowledgedResponse response = this.client.indices().putMapping(putMapping);
    	return response;
    }
	
}
