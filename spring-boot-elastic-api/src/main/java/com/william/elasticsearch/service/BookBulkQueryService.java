package com.william.elasticsearch.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.william.elasticsearch.conf.EsConfig;
import com.william.elasticsearch.model.ResponseVo;

/**
 * @author: chaiz
 * @Description: TODO
 * @date: 2019年2月21日 下午5:27:02
 * @version: v1.0.0
 */
@Service
public class BookBulkQueryService {

	@Autowired
	private EsConfig esconfig;

    @Autowired
    private RestHighLevelClient client;
    
	/***
	 * bulk api
	 * @Title: bulkOperate
	 * @Description: 批量操作接口，可是多个新增、修改、删除同时操作
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public BulkResponse bulkOperate() throws IOException {
		BulkRequest bulkRequest = new BulkRequest();
		
		IndexRequest indexRequest = new IndexRequest(esconfig.getIndex(), esconfig.getType(), "12");
		Map<String,String> adddoc = new HashMap<>();
		adddoc.put("id", "12");
		adddoc.put("title", "微服务实践之Dubbo");
		adddoc.put("edition", "2");
		adddoc.put("author", "alibaba");
		adddoc.put("type", "java");
		adddoc.put("wordCount", "500000");
		adddoc.put("publishDate", "2015-01-01");
		adddoc.put("desc", "dubbo study");
		indexRequest.source(adddoc, XContentType.JSON);
		
		UpdateRequest updateRequest = new UpdateRequest(esconfig.getIndex(), esconfig.getType(), "9");
		Map<String,String> editdoc = new HashMap<>();
		editdoc.put("title","python开发入门");
		updateRequest.doc(editdoc,XContentType.JSON);
		
		bulkRequest.add(indexRequest);
		bulkRequest.add(updateRequest);
		BulkResponse blukresponse = this.client.bulk(bulkRequest);
		return blukresponse;
	}
	

	
}
