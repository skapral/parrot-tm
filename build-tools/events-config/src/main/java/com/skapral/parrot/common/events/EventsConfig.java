package com.skapral.parrot.common.events;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
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

}
