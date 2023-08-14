package com.example.atm.config;

import com.example.atm.constants.KafkaContants;
import com.example.atm.event.AmountWasDepositedEvent;
import com.example.atm.event.Event;
import com.example.atm.event.HighValueDepositWasDetectedEvent;
import com.example.atm.serdes.AmountWasDepositedEventDeserializer;
import com.example.atm.serdes.AmountWasDepositedEventSerializer;
import com.example.atm.serdes.HighValueDepositWasDetectedEventSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public ProducerFactory<String, AmountWasDepositedEvent> amountWasDepositedEventProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaContants.BOOTSTRAP_SERVERS_CONFIG);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AmountWasDepositedEventSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, AmountWasDepositedEvent> amountWasDepositedEventProducerFactoryKafkaTemplate() {
        return new KafkaTemplate<>(amountWasDepositedEventProducerFactory());
    }

    @Bean
    public ProducerFactory<String, HighValueDepositWasDetectedEvent> highValueDepositWasDetectedEventProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaContants.BOOTSTRAP_SERVERS_CONFIG);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, HighValueDepositWasDetectedEventSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, HighValueDepositWasDetectedEvent> highValueDepositWasDetectedEventKafkaTemplate() {
        return new KafkaTemplate<>(highValueDepositWasDetectedEventProducerFactory());
    }

    @Bean
    public ConsumerFactory<String, Event> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaContants.BOOTSTRAP_SERVERS_CONFIG);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, AmountWasDepositedEventDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Event> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Event> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
