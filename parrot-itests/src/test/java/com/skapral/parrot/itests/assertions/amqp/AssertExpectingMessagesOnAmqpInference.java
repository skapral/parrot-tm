package com.skapral.parrot.itests.assertions.amqp;

import com.pragmaticobjects.oo.inference.api.Inference;
import com.pragmaticobjects.oo.inference.api.Infers;
import com.pragmaticobjects.oo.tests.Assertion;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Delivery;
import com.skapral.parrot.itests.assertions.AssertExpecting;
import com.skapral.parrot.itests.assertions.AssertionInferred;
import com.skapral.parrot.itests.assertions.amqp.expectations.Expectation;
import io.vavr.collection.List;

import java.io.IOException;
import java.util.Collections;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

@Infers(value = "AssertExpectingMessagesOnAmqp", using = AssertionInferred.class)
public class AssertExpectingMessagesOnAmqpInference implements Inference<Assertion> {
    private final Assertion assertion;
    private final Connection connection;
    private final List<AmqpSource> eavesdropSources;
    private final Function<List<Delivery>, Expectation> assertionOnEavesdroppedState;

    public AssertExpectingMessagesOnAmqpInference(Assertion assertion, Connection connection, List<AmqpSource> eavesdropSources, Function<List<Delivery>, Expectation> assertionOnEavesdroppedState) {
        this.assertion = assertion;
        this.connection = connection;
        this.eavesdropSources = eavesdropSources;
        this.assertionOnEavesdroppedState = assertionOnEavesdroppedState;
    }

    @Override
    public final Assertion inferredInstance() {
        Queue<Delivery> deliveries = new ConcurrentLinkedDeque<>();
        return eavesdropSources.<Assertion>foldLeft(
            () -> new AssertExpecting(
                assertion,
                () -> assertionOnEavesdroppedState.apply(List.ofAll(deliveries)).check()
            ),
            (Assertion r, AmqpSource s) -> () -> {
                try (var channel = connection.createChannel()) {
                    var eavesdropQueue = "eavesdrop." + UUID.randomUUID().toString();
                    channel.queueDeclare(eavesdropQueue, false, true, true, Collections.emptyMap());
                    channel.queueBind(eavesdropQueue, s.exchange, s.routingKey);
                    channel.basicConsume(eavesdropQueue, false, (consumerTag, message) -> deliveries.add(message), consumerTag -> {
                    });
                    r.check();
                } catch (IOException | TimeoutException ex) {
                    throw new RuntimeException(ex);
                }
            }
        );
    }
}
