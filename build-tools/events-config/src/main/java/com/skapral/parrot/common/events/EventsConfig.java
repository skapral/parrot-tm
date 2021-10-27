package com.skapral.parrot.common.events;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @RabbitListener(queues = "${amqp.inbox}")
    public void listen(String in) {
        System.out.println("Message read from " + inboxQueue + " : " + in);
    }

}
