package com.example.kafkastudy.kafka.consumer;

import com.example.kafkastudy.constant.KafkaBrokerProperties;
import com.example.kafkastudy.constant.KafkaConsumerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author LING lbh19970425@gmail.com
 * @date 2022/1/26 10:29
 */
@Component
@Slf4j
public class Consumer {
    KafkaConsumer<String,String> consumer;
    private static final AtomicBoolean isRunning = new AtomicBoolean(true);

    @PostConstruct
    public void init(){
        Properties props = new Properties();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaBrokerProperties.KAFKA_BROKER_URL);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConsumerProperties.KAFKA_GROUP_ID);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG,KafkaConsumerProperties.KAFKA_CLIENT_ID);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");//关闭自动提交
        consumer = new KafkaConsumer<>(props);
    }

    public void consumer(){
        consumer.subscribe(Collections.singletonList(KafkaBrokerProperties.KAFKA_DEMO_TOPIC));
        try{
            while (isRunning.get()){
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for(ConsumerRecord<String,String> record:records){
                    log.info("[消费者]topic={},partition={},offset={},key={},value={}",
                            record.topic(),record.partition(),record.offset(),record.key(),record.value());
                }
                //异步提交，提高消费者的吞吐量
                consumer.commitAsync();
            }
        }catch (Exception e){
            log.error("consumer had exception:{}", ExceptionUtils.getStackTrace(e));
        }finally {
            try {
                //最后同步提交，确保提交最终的消费进度
                consumer.commitSync();
            }finally {
                consumer.close();
            }
        }

    }

    public void stop(){
        isRunning.set(false);
    }

    public void start(){
        isRunning.set(true);
        consumer();
    }
}
