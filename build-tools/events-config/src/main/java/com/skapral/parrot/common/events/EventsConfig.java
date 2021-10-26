package com.skapral.parrot.common.events;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.time.Instant;

@Configuration
public class EventsConfig {
    private final String inboxQueue;

    @Autowired
    public EventsConfig(
            @Value("${amqp.inbox}") String inboxQueue
    ) {
        this.inboxQueue = inboxQueue;
    }

    @Bean
    public Queue inbox() {
        return new Queue(inboxQueue, true);
    }

    @Bean
    public Exchange handshake() {
        return new FanoutExchange("handshake", true, false);
    }

    @Bean
    public Binding handshakeBinding() {
        return new Binding(inboxQueue, Binding.DestinationType.QUEUE, "handshake", "", null);
    }

    @RabbitListener(queues = "${amqp.inbox}")
    public void listen(String in) {
        System.out.println("Message read from " + inboxQueue + " : " + in);
    }

    /*@PostConstruct
    public void initHandshake() {
        var props = new MessageProperties();
        props.setCorrelationId("handshake");
        props.setAppId(inboxQueue);
        var msg = new Message("handshake".getBytes(), props);
        template.send(msg);
    }*/
}
