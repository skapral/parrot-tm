package com.skapral.parrot.itests.assertions.amqp.expectations;

import com.rabbitmq.client.Delivery;
import io.vavr.collection.List;

import java.util.function.Function;

public class ExpectMessage implements Expectation {
    private final List<Delivery> messages;
    private final Function<Delivery, Expectation> expectationFn;

    public ExpectMessage(List<Delivery> messages, Function<Delivery, Expectation> expectationFn) {
        this.messages = messages;
        this.expectationFn = expectationFn;
    }

    @Override
    public final boolean check() {
        return messages
            .map(message -> expectationFn.apply(message).check())
            .foldLeft(false, (b, m) -> b || m);
    }
}
