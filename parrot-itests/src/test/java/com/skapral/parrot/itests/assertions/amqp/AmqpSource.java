package com.skapral.parrot.itests.assertions.amqp;

public class AmqpSource {
    public final String exchange;
    public final String routingKey;

    public AmqpSource(String exchange, String routingKey) {
        this.exchange = exchange;
        this.routingKey = routingKey;
    }
}
