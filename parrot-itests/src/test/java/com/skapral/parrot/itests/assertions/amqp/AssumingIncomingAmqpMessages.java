package com.skapral.parrot.itests.assertions.amqp;

import com.pragmaticobjects.oo.tests.Assertion;
import com.rabbitmq.client.Connection;
import io.vavr.collection.List;
import org.assertj.core.api.Assertions;

public class AssumingIncomingAmqpMessages implements Assertion {
    private final Connection connection;
    private final String exchange;
    private final String routingKey;
    private final List<AmqpMessage> messages;

    public AssumingIncomingAmqpMessages(Connection connection, String exchange, String routingKey, List<AmqpMessage> messages) {
        this.connection = connection;
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.messages = messages;
    }

    public AssumingIncomingAmqpMessages(Connection connection, String exchange, String routingKey, AmqpMessage... messages) {
        this(
            connection,
            exchange,
            routingKey,
            List.of(messages)
        );
    }

    @Override
    public final void check() throws Exception {
        try(var channel = connection.createChannel()) {
            for(var message : messages) {
                channel.basicPublish(exchange, routingKey, message.props, message.payload.getBytes());
            }
        } catch(Exception ex) {
            Assertions.fail("Failed to send AMQP messages", ex);
        }
    }
}
