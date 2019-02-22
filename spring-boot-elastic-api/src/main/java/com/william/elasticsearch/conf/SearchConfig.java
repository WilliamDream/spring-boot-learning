package com.william.elasticsearch.conf;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired 
    EsConfig esConfig;

    @Bean
    public TransportClient transclient() throws UnknownHostException{
    	String[] ips = esConfig.getIp().split(",");
    	TransportAddress[] noedes = new TransportAddress[ips.length];
    	for (int i = 0; i < ips.length; i++) {
    		noedes[i] = new TransportAddress(InetAddress.getByName(ips[i]), esConfig.getPort());
		}
//        TransportAddress node = new TransportAddress(
//                InetAddress.getByName(esConfig.getIp()),
//                esConfig.getPort()
//        );

        Settings settings = Settings.builder()
                .put("cluster.name",esConfig.getClusterName())
                .build();

        TransportClient client = new PreBuiltTransportClient(settings);
        client.addTransportAddresses(noedes);
        return client;
    }
    
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
