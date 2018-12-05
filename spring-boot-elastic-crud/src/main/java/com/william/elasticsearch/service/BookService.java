package com.william.elasticsearch.service;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.william.elasticsearch.conf.EsConfig;

/**
 * @author: william
 * @Description: TODO
 * @date: 2018年8月30日 下午4:11:36
 * @version: v1.0.0
 */

@Service
public class BookService {

	@Autowired
	private EsConfig esconfig;
//    private String indexName = "book"; //相当于数据库名称
//    private String indexType = "novel";	//相当于数据表名称

    @Autowired
    private TransportClient client;

    public GetResponse getBookById(String id){
        return this.client.prepareGet(esconfig.getIndex(),esconfig.getType(),id).get();
    }

    public IndexResponse add(String title, String author, int wordCount, String publishDate) throws Exception {

        XContentBuilder content = XContentFactory.jsonBuilder()
                .startObject()
                .field("title",title)
                .field("author",author)
                .field("publish_date",publishDate)
                .field("word_count",wordCount)
                .endObject();

        IndexResponse response = this.client.prepareIndex(esconfig.getIndex(),esconfig.getType())
                .setSource(content)
                .get();

        return response;

    }

    public DeleteResponse remove(String id) {
       return this.client.prepareDelete(esconfig.getIndex(),esconfig.getType(),id).get();
    }

    public UpdateResponse modify(String id, String title, String author) throws Exception {
        UpdateRequest request = new UpdateRequest(esconfig.getIndex(),esconfig.getType(),id);

        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject();

        if(StringUtils.isEmpty(title)){
            builder.field("title",title);
        }
        if(StringUtils.isEmpty(author)){
            builder.field("author",author);
        }
        builder.endObject();

        request.doc(builder);
        return this.client.update(request).get();
    }
	
	
	
}
