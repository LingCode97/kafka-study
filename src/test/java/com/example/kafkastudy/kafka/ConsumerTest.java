package com.example.kafkastudy.kafka;

import com.example.kafkastudy.kafka.consumer.Consumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author LING lbh19970425@gmail.com
 * @date 2022/1/30 14:09
 */
@SpringBootTest
public class ConsumerTest {
    @Autowired
    Consumer consumer;

    @Test
    public void receiveMsgTest(){
        consumer.consumer();
    }
}
