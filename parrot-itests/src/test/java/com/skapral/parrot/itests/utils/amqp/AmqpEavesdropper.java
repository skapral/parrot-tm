package com.skapral.parrot.itests.utils.amqp;

import com.rabbitmq.client.*;

import java.util.Collections;
import java.util.UUID;


public class AmqpEavesdropper {
    private final Connection connection;

    public AmqpEavesdropper(Connection connection) {
        this.connection = connection;
    }

    public AmqpEavesdroppedState startEavesdropping(String exchangeName, String routingKey) {
        try {
            var channel = connection.createChannel();
            var state = new AmqpEavesdroppedState(channel);
            var eavesdropQueue = "eavesdrop." + UUID.randomUUID().toString();
            channel.queueDeclare(eavesdropQueue, false, true, true, Collections.emptyMap());
            channel.queueBind(eavesdropQueue, exchangeName, routingKey);
            channel.basicConsume(eavesdropQueue, false, (consumerTag, message) -> state.deliveries.add(message), consumerTag -> {
            });
            return state;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
