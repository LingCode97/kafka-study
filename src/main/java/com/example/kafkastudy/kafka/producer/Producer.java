package com.example.kafkastudy.kafka.producer;

import com.example.kafkastudy.constant.KafkaProducerProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;
import java.util.concurrent.Future;

/**
 * @author LING lbh19970425@gmail.com
 * @date 2022/1/26 09:47
 */
@Component
public class Producer {
    private KafkaProducer<String,String> producer;

    @PostConstruct
    public void init(){
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProducerProperties.KAFKA_BROKER_URL);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaProducerProperties.KAFKA_CLIENT_ID);
        //同步发送时，可选配置重试次数，重试指定次数后还失败才会抛出异常
        //props.put(ProducerConfig.RETRIES_CONFIG,3);
        producer = new KafkaProducer<>(props);
    }

    public void send(ProducerRecord<String,String> record){
        try{
            //异步发送，发后即忘
            producer.send(record);
            /*
             *同步发送，可靠性更高
             * Future<RecordMetadata> future = producer.send(record);
             * RecordMetadata recordMetadata = future.get();
             * */
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
