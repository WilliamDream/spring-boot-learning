/**
 * 
 */
package com.william.springbootkafka.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * @author: chaiz
 * @Description: TODO
 * @date: 2018年9月11日 下午4:02:19
 * @version: v1.0.0
 */
public class Listener {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    @KafkaListener(topics = {"kafkatopic"})
    public void listen(ConsumerRecord<?, ?> record) {
        logger.info("kafka的key: " + record.key());
        logger.info("kafka的value: " + record.value().toString());
    }
	
}
