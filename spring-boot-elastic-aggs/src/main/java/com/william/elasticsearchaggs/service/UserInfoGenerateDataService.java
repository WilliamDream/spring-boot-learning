package com.william.elasticsearchaggs.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.william.elasticsearchaggs.conf.EsConfig;
import com.william.elasticsearchaggs.model.ResponseVo;
import com.william.elasticsearchaggs.model.UserInfo;
import com.william.elasticsearchaggs.utils.RandomUtil;


/**
 * @author: william
 * @Description: TODO
 * @date: 2019年2月26日 上午10:04:17
 * @version: v1.0.0
 */
@Service
public class UserInfoGenerateDataService {
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
	                //微信号（唯一的索引）  keyword  text
	                .startObject("wechatno").field("type", "keyword").endObject()
	                //昵称
	                .startObject("nickname").field("type", "text").endObject()
	                //性别
	                .startObject("gender").field("type","keyword").endObject()
	                //位置，专门用来存储地理坐标的类型，包含了经度和纬度
	                .startObject("location").field("type", "geo_point").endObject()
	                .startObject("birthday").field("type", "date").endObject()
	                .startObject("age").field("type", "integer").endObject()
	                .startObject("province").field("type", "text").endObject()
		        .endObject()
	        .endObject()
	    .endObject();
    	request.mapping(esconfig.getIndex(), builder);
    	PutMappingRequest putMapping = Requests.putMappingRequest(esconfig.getIndex()).type(esconfig.getType()).source(builder);
    	AcknowledgedResponse response = this.client.indices().putMapping(putMapping);
    	return response;
    }
	
	public int createBulkData(double yourLat,double yourLon,long total) throws IOException {
		RandomUtil.openCache();
		
		List<XContentBuilder> contents = new ArrayList<XContentBuilder>();
		for (int i = 0; i < total; i++) {
			UserInfo userInfo = generateWechatUser(yourLat, yourLon);
			contents.add(obj2XContent(userInfo));
		}
		RandomUtil.clearCache();
		int succ = 0;
		int count = 0;
		for (XContentBuilder content : contents) {
			BulkRequest bulkRequest = new BulkRequest();
			IndexRequest indexRequest = new IndexRequest(esconfig.getIndex(), esconfig.getType());
			indexRequest.source(content);
			bulkRequest.add(indexRequest);
			bulkRequest.numberOfActions();
			if(count%100!=0) {
				count++;
			}else {
				this.client.bulk(bulkRequest);
				succ = succ + bulkRequest.numberOfActions();
			}
		}
		
		return succ;
	}
	
    public ResponseVo searchNearby(double lat, double lon, int radius, int size, String sex) throws IOException{
    	SearchRequest searchRequest = new SearchRequest();
		// 设置索引，索引可以为多个
		searchRequest.indices(esconfig.getIndex());
		// 设置类型，类型也可以为多个
		searchRequest.types(esconfig.getType());
		SearchSourceBuilder build = new SearchSourceBuilder();
        //同一单位为米
        String unit = DistanceUnit.METERS.toString();//坐标范围计量单位

        //实现分页操作
//		int  index = (bookRequest.getPageindex()-1)*bookRequest.getPagesize();
		build.from(0);	// 查询起始  (pageIndex-1)*pageSize
		build.size(100);	// 每页大小
        //构建查询条件
		BoolQueryBuilder boolbuild = new BoolQueryBuilder();
        //地理坐标，方圆多少米以内都要给找出来
		GeoDistanceQueryBuilder qb = QueryBuilders.geoDistanceQuery("location")
                .point(lat, lon)
                .distance(radius,DistanceUnit.METERS)
//                .optimizeBbox("memory")
                .geoDistance(GeoDistance.PLANE); //设置计算规则，是平面还是立体 (方圆多少米)

//        //相对于 where location > 0 and location < radius
		boolbuild.filter(qb);
		
        //拼接查询条件
        //性别、昵称，坐标

        //继续拼接where条件
        //and sex = ?
        if(!(sex == null || "".equals(sex.trim()))){
        	boolbuild.must(QueryBuilders.matchQuery("gender", sex));
        }

        //设置排序规则
        GeoDistanceSortBuilder geoSort = SortBuilders.geoDistanceSort("location",lat, lon);
        geoSort.unit(DistanceUnit.METERS);
        geoSort.order(SortOrder.ASC);//按距离升序排序，最近的要排在最前面

        //order by location asc 升序排序
        build.sort(geoSort);

        //到此为止，就相当于SQL语句构建完毕


        //开始执行查询
        //调用  execute()方法
        //Response
        build.query(boolbuild);
        searchRequest.source(build);
        System.out.println(searchRequest.source().toString());
        SearchResponse response = this.client.search(searchRequest);

        //高亮分词
        SearchHits hits = response.getHits();
        SearchHit[] searchHists = hits.getHits();

        //搜索的耗时
        Float consumetime = response.getTook().getMillis() / 1000f;
        List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
        
        for (SearchHit hit : searchHists) {
            // 获取距离值，并保留两位小数点
            BigDecimal geoDis = new BigDecimal((Double) hit.getSortValues()[0]);
            Map<String, Object> hitMap = hit.getSourceAsMap();
            // 在创建MAPPING的时候，属性名的不可为geoDistance。
            hitMap.put("geoDistance", geoDis.setScale(0, BigDecimal.ROUND_HALF_DOWN));
            data.add(hitMap);
        }

        return ResponseVo.success(data, hits.getTotalHits(),consumetime);
    }
	
	private UserInfo generateWechatUser(double yourLat,double yourLon) {
		String wechatno = RandomUtil.randomWechatNo();
		String gender = RandomUtil.randomSex();
		String nickname = RandomUtil.randomNickName(gender);
		double[] point = RandomUtil.randomPoint(yourLat, yourLon);
		String birthday = RandomUtil.randomBirthday();
		String year = birthday.substring(0, 5);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowyear = sdf.format(new Date()).substring(0, 5);
		int age = Integer.parseInt(nowyear)-Integer.parseInt(year);
		String privince = RandomUtil.randomProvince();
		return new UserInfo(wechatno, nickname, gender, point[0], point[1],birthday,age,privince);
	}
	
	private XContentBuilder obj2XContent(UserInfo userInfo) {
		XContentBuilder jsonBuild = null;
		try {
			jsonBuild = XContentFactory.jsonBuilder();
			jsonBuild.startObject()
			.field("wechatno", userInfo.getWechatno())
			.field("nickname", userInfo.getNickname())
			.field("gender", userInfo.getGender())
			.startObject("location")
			.field("lat", userInfo.getLat())
			.field("lon", userInfo.getLon())
			.endObject()
			.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonBuild;
	}
}
