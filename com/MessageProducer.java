package com.sample;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class MessageProducer {

    private final Producer<String, String> producer;
    private String topic = "match";
	//Kafka地址
    private String brokers = "172.18.54.143:9092,172.18.54.143:9093,172.18.54.143:9094";
    private String message = "";
    private String key = "";
    /**
     * 
     * @param topic
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * 
     * @param brokers
     */
    public void setBrokers(String brokers) {
        this.brokers = brokers;
    }
    
    /**
     * 
     * @param key
     * @param message
     */
    public void setMessage(String key, String message) {
        this.message = message;
        this.key = key;
    }

    public MessageProducer() {
        Properties props = new Properties();
        props.put("metadata.broker.list", this.brokers);
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("key.serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks","-1");
        producer = new Producer<String, String>(new ProducerConfig(props));
    }
    
    public void produce() {
        producer.send(new KeyedMessage<String, String>(this.topic, this.key ,this.message));
        System.out.println(this.message); 
    }
    
    /**
     * 
     * @param key
     * @param message
     */
    public void produce(String key, String message) {
        this.message = message;
        this.key = key;
        this.produce();
    }
    
    public void close() {
        this.producer.close();
    }
}
