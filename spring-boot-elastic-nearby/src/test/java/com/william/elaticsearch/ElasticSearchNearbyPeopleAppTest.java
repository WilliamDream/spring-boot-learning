package com.william.elaticsearch;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import com.william.elaticsearch.model.ResponseVo;
import com.william.elaticsearch.service.SearchNearbyPeopleService;


/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticSearchNearbyPeopleAppTest {

	
	@Autowired
	private SearchNearbyPeopleService service;
	
	@Test
//	@Ignore
	public void searchNearby() throws IOException{

		int size = 10,radius = 5000;

		System.out.println("开始获取距离" + radius + "米以内人");
		ResponseVo result = service.searchNearby(22.548732,113.941607, radius, size, null);

		System.out.println("共找到" + result.getCount() + "个人,优先显示" + size + "人，查询耗时" + result.getConsumetime() + "秒");
		for (Map<String,Object> taxi : result.getData()) {

			String nickName = taxi.get("nickName").toString();

			String location = taxi.get("location").toString();
			Object geo = taxi.get("geoDistance");

			System.out.println(nickName + "，" +
					"微信号:" + taxi.get("wxNo") +
					"，性别:" + taxi.get("sex") +
					",距离"  + geo + "米" +
					"(坐标：" + location + ")");
		}

		System.out.println("以上" + size + "人显示在列表中......");

	}
	
}
