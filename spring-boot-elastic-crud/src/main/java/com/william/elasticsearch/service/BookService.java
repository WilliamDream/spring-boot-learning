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
import com.william.elasticsearch.model.Book;

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

    @Autowired
    private TransportClient client;

    public GetResponse getBookById(String id){
        return this.client.prepareGet(esconfig.getIndex(),esconfig.getType(),id).get();
    }

    
    public IndexResponse addBook(Book book) throws Exception {
        XContentBuilder content = XContentFactory.jsonBuilder()
                .startObject()
                .field("title",book.getTitle())
                .field("author",book.getAuthor())
                .field("publish_date",book.getPublishDate())
                .field("word_count",book.getWordCount())
                .endObject();

        IndexResponse response = this.client.prepareIndex(esconfig.getIndex(),esconfig.getType(),book.getId())
                .setSource(content)
                .get();

        return response;
    }
    

    public DeleteResponse delBook(String id) {
       return this.client.prepareDelete(esconfig.getIndex(),esconfig.getType(),id).get();
    }

    public UpdateResponse editBook(Book book) throws Exception {
        UpdateRequest request = new UpdateRequest(esconfig.getIndex(),esconfig.getType(),book.getId());

        XContentBuilder builder = XContentFactory.jsonBuilder().startObject();

        if(StringUtils.isEmpty(book.getTitle())){
            builder.field("title",book.getTitle());
        }
        if(StringUtils.isEmpty(book.getAuthor())){
            builder.field("author",book.getAuthor());
        }
        if(StringUtils.isEmpty(book.getPublishDate())){
            builder.field("publish_date",book.getPublishDate());
        }
        /*if(book.getWordCount()!=null) {
        	 builder.field("word_count",book.getWordCount());
        }*/
        builder.endObject();

        request.doc(builder);
        return this.client.update(request).get();
    }
	
	
	
}
