package com.skapral.parrot.itests.assertions.amqp;

import com.pragmaticobjects.oo.tests.Assertion;
import com.rabbitmq.client.Delivery;
import org.assertj.core.api.Assertions;

public class AssertMessageType implements Assertion {
    private final Delivery deliveryUnderTest;
    private final String type;

    public AssertMessageType(Delivery deliveryUnderTest, String type) {
        this.deliveryUnderTest = deliveryUnderTest;
        this.type = type;
    }

    @Override
    public final void check() throws Exception {
        Assertions.assertThat(deliveryUnderTest.getProperties().getType()).isEqualTo(type);
    }
}
