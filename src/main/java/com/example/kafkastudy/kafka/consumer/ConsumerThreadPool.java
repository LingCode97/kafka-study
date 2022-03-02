package com.example.kafkastudy.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author LING lbh19970425@gmail.com
 * @date 2022/1/30 14:15
 */
@Slf4j
public class ConsumerThreadPool extends Thread{
    private final KafkaConsumer<String,String> kafkaConsumer;
    private final ExecutorService executorService;

    public ConsumerThreadPool(Properties properties, List<String> topics, int threadNumber){
        kafkaConsumer= new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(topics);
        executorService = new ThreadPoolExecutor(threadNumber, threadNumber, 0L, TimeUnit.MILLISECONDS
                , new ArrayBlockingQueue<>(1000), new ThreadFactory() {
            final AtomicInteger id = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"KafkaConsumer-"+id.getAndIncrement());
            }
        },new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Override
    public void run() {
        try{
            while (true){
                ConsumerRecords<String,String> records = kafkaConsumer.poll(Duration.ofMillis(100));
                if(!records.isEmpty()){
                    executorService.submit(new RecordsHandler(records));
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            kafkaConsumer.close();
        }
    }

    public static class RecordsHandler extends Thread{
        public ConsumerRecords<String,String> records;
        

        public RecordsHandler(ConsumerRecords<String,String> records){
            this.records = records;
           // offsets = new HashMap<>();
        }

        @Override
        public void run() {
            for(TopicPartition tp:records.partitions()){
                List<ConsumerRecord<String,String>> tpRecords = records.records(tp);
                //处理消息
                for(ConsumerRecord<String,String> record:tpRecords){
                    log.info("[消费者]topic={},partition={},offset={},key={},value={}",
                            record.topic(),record.partition(),record.offset(),record.key(),record.value());
                }
//                long lastConsumerOffset =tpRecords.get(tpRecords.size()-1).offset();
//                synchronized (offsets){
//
//                }
            }

        }
    }
}
