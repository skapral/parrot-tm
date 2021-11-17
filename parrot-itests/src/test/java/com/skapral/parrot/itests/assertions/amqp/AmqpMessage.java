package com.skapral.parrot.itests.assertions.amqp;


import com.rabbitmq.client.AMQP;

public class AmqpMessage {
    public final AMQP.BasicProperties props;
    public final String payload;

    public AmqpMessage(AMQP.BasicProperties props, String payload) {
        this.props = props;
        this.payload = payload;
    }
}
