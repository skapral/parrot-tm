package com.skapral.parrot.accounting.events;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;

public class EventsConfig {
    @Bean
    public Queue queue() {
        return new Queue("analytics-service", true);
    }

    @RabbitListener(queues = "analytics-service")
    public void listen(String in) {
        System.out.println("Message read from myQueue : " + in);
    }
}
