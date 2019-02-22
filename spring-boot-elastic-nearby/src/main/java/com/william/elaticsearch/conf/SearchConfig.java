package com.william.elaticsearch.conf;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchConfig {

    @Autowired 
    EsConfig esConfig;

    @Bean
    public RestHighLevelClient client() {
    	String[] ips = esConfig.getIp().split(",");
    	List<HttpHost> hostlist = new ArrayList<>();
    	for (int i=0;i<ips.length;i++) {
    		hostlist.add(new HttpHost(ips[i], 9200, esConfig.getScheme()));
		}
    	RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(hostlist.toArray(new HttpHost[hostlist.size()])));
		return client;
    }
    
}
