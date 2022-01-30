package com.example.kafkastudy.kafka;

import com.example.kafkastudy.constant.KafkaBrokerProperties;
import com.example.kafkastudy.kafka.consumer.Consumer;
import com.example.kafkastudy.kafka.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author LING lbh19970425@gmail.com
 * @date 2022/1/30 13:56
 */
@SpringBootTest
public class ProducerTest {
    @Autowired
    Producer producer;

    @Test
    public void sendMsgTest(){
        ProducerRecord<String,String> record = new ProducerRecord<>(KafkaBrokerProperties.KAFKA_DEMO_TOPIC, "hello kafka!");
        producer.send(record);
    }

    @Test
    public void sendMsgTest2() throws InterruptedException {
        for (int i =0;i<10000;i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>(KafkaBrokerProperties.KAFKA_DEMO_TOPIC, String.valueOf(System.currentTimeMillis()));
            producer.send(record);
        }
    }


}
