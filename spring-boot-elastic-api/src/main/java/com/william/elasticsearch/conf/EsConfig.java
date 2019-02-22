package com.william.elasticsearch.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @author: william
 * @Description: TODO
 * @date: 2018年8月30日 下午3:24:10
 * @version: v1.0.0
 */

@Component
@Data
public class EsConfig {

    @Value("${es.cluster.name}")
    private String clusterName;
    @Value("${es.host.ip}")
    private String ip;
    @Value("${es.host.port}")
    private int port;
    @Value("${es.index}")
    private String index;
    @Value("${es.type}")
    private String type;
    @Value("${es.scheme}")
    private String scheme;
}

