package com.william.elasticsearch.conf;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchConfig {

    @Autowired EsConfig esConfig;

    /*@Bean
    public TransportClient client() throws UnknownHostException{


        TransportAddress node = new TransportAddress(
                InetAddress.getByName(esConfig.getIp()),
                esConfig.getPort()
        );

        Settings settings = Settings.builder()
                .put("cluster.name",esConfig.getClusterName())
                .build();

        TransportClient client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(node);
        return client;
    }*/
    
    @Bean
    public RestHighLevelClient client() {
    	
    	RestHighLevelClient client = new RestHighLevelClient(
    			RestClient.builder(
    					new HttpHost("192.169.2.98", 9200, "http"),
    					new HttpHost("192.169.2.188", 9200, "http"),
    					new HttpHost("192.169.2.156", 9200, "http")));
		return client;
    }
    
}
