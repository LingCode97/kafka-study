package com.example.kafkastudy.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * @author LING lbh19970425@gmail.com
 * @date 2022/1/26 10:13
 * 生产者拦截器，非必需
 */
@Slf4j
public class ProducerIntercept implements ProducerInterceptor<String,String> {
    private volatile long sendSuccess = 0;
    private volatile long sendFail = 0;

    @Override
    public ProducerRecord<String,String> onSend(ProducerRecord<String,String> producerRecord) {
        //在序列化消息和计算分区之前调用，可以用于统一过滤、修改消息内容
        String modifiedValue = "[KafkaIntercept]"+producerRecord.value();
        return new ProducerRecord<>(producerRecord.topic(), producerRecord.partition()
                , producerRecord.timestamp(), producerRecord.key()
                , modifiedValue, producerRecord.headers());
    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        //在消息被应答之前或消息发送失败时调用
        if(e==null){
            sendSuccess++;
        }else{
            sendFail++;
        }
    }

    @Override
    public void close() {
        //拦截器关闭时调用
        double successRatio = (double) sendSuccess/(sendFail+sendSuccess);
        log.info("发送成功率:{}",String.format("%f",successRatio*100)+"%");
    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
