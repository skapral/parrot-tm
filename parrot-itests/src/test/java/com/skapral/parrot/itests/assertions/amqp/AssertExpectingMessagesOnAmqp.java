package com.skapral.parrot.itests.assertions.amqp;

import com.pragmaticobjects.oo.tests.Assertion;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Delivery;
import com.skapral.parrot.itests.assertions.amqp.expectations.Expectation;
import io.vavr.collection.List;
import org.awaitility.Awaitility;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

public class AssertExpectingMessagesOnAmqp implements Assertion {
    private final Assertion assertion;
    private final Connection connection;
    private final List<AmqpSource> eavesdropSources;
    private final Function<List<Delivery>, Expectation> assertionOnEavesdroppedState;

    public AssertExpectingMessagesOnAmqp(Assertion assertion, Connection connection, List<AmqpSource> eavesdropSources, Function<List<Delivery>, Expectation> assertionOnEavesdroppedState) {
        this.assertion = assertion;
        this.connection = connection;
        this.eavesdropSources = eavesdropSources;
        this.assertionOnEavesdroppedState = assertionOnEavesdroppedState;
    }

    @Override
    public final void check() throws Exception {
        Queue<Delivery> deliveries = new ConcurrentLinkedDeque<>();
        var callForDeliveries = eavesdropSources.<Assertion>foldLeft(
            () -> {
                assertion.check();
                Awaitility.waitAtMost(Duration.ofMinutes(1))
                    .pollDelay(Duration.ofSeconds(0))
                    .pollInterval(Duration.ofSeconds(5))
                    .await()
                    .until(() -> assertionOnEavesdroppedState.apply(List.ofAll(deliveries)).check());
            },
            (Assertion r, AmqpSource s) -> () -> {
                try(var channel = connection.createChannel()) {
                    var eavesdropQueue = "eavesdrop." + UUID.randomUUID().toString();
                    channel.queueDeclare(eavesdropQueue, false, true, true, Collections.emptyMap());
                    channel.queueBind(eavesdropQueue, s.exchange, s.routingKey);
                    channel.basicConsume(eavesdropQueue, false, (consumerTag, message) -> deliveries.add(message), consumerTag -> {
                    });
                    r.check();
                } catch(IOException | TimeoutException ex) {
                    throw new RuntimeException(ex);
                }
            }
        );
        callForDeliveries.check();
    }
}
