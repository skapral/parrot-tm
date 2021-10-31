package com.skapral.parrot.itests.utils.amqp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;
import io.vavr.collection.List;
import org.awaitility.Awaitility;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class AmqpEavesdroppedState implements AutoCloseable {
    private final Channel channel;
    final Set<Delivery> deliveries = Collections.newSetFromMap(new ConcurrentHashMap<>());

    AmqpEavesdroppedState(Channel channel) {
        this.channel = channel;
    }

    public void assertOnMessages(Consumer<List<Delivery>> assertion, Duration timeout) {
        Awaitility.waitAtMost(timeout).await().untilAsserted(() -> assertion.accept(List.ofAll(deliveries)));
    }

    @Override
    public void close() {
        try {
            channel.close();
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
