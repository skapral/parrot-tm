package com.skapral.parrot.tasks.events;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Amqp {
    @Bean
    public Queue queue() {
        return new Queue("tasks-service", true);
    }

    @RabbitListener(queues = "tasks-service")
    public void listen(Message message) {
        System.out.println("Message read from myQueue : " + new String(message.getBody()));
    }
}
