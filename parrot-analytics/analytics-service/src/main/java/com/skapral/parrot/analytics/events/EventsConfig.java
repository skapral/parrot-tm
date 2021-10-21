package com.skapral.parrot.analytics.events;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;

public class EventsConfig {
    @Bean
    public Queue queue() {
        return new Queue("accounting-service", true);
    }

    @RabbitListener(queues = "accounting-service")
    public void listen(String in) {
        System.out.println("Message read from myQueue : " + in);
    }
}
