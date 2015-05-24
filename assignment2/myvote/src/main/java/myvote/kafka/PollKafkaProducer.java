package myvote.kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

public class PollKafkaProducer {
	final static String TOPIC = "cmpe273-new-topic";


    public void produceKafkaStream(String pollResult){
    	
        Properties properties = new Properties();
        properties.put("metadata.broker.list","54.68.83.161:9092");
        properties.put("serializer.class","kafka.serializer.StringEncoder");
        properties.put("request.required.acks", "1");
        ProducerConfig producerConfig = new ProducerConfig(properties);
        kafka.javaapi.producer.Producer<String,String> producer = new kafka.javaapi.producer.Producer<String, String>(producerConfig);
        KeyedMessage<String, String> message =new KeyedMessage<String, String>(TOPIC, pollResult);
        producer.send(message);
        producer.close();
    }
}
