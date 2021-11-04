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
        for(var message : messages) {
            if(expectationFn.apply(message).check()) {
                return true;
            }
        }
        return false;
    }
}
