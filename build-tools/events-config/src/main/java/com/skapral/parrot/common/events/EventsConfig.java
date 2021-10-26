package com.skapral.parrot.common.events;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class EventsConfig {
    private final String inboxQueue;

    @Autowired
    public EventsConfig(@Value("${amqp.inbox}") String inboxQueue) {
        this.inboxQueue = inboxQueue;
    }

    @Bean
    public Queue queue() {
        return new Queue(inboxQueue, true);
    }

    @RabbitListener(queues = "${amqp.inbox}")
    public void listen(String in) {
        System.out.println("Message read from myQueue : " + in);
    }
}
