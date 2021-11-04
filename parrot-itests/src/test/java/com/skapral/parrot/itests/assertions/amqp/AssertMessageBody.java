package com.skapral.parrot.itests.assertions.amqp;

import com.pragmaticobjects.oo.tests.Assertion;
import com.rabbitmq.client.Delivery;

import java.util.function.Function;

public class AssertMessageBody implements Assertion {
    private final Delivery deliveryUnderTest;
    private final Function<String, Assertion> assertionOnBody;

    public AssertMessageBody(Delivery deliveryUnderTest, Function<String, Assertion> assertionOnBody) {
        this.deliveryUnderTest = deliveryUnderTest;
        this.assertionOnBody = assertionOnBody;
    }

    @Override
    public final void check() throws Exception {
        assertionOnBody.apply(new String(deliveryUnderTest.getBody())).check();
    }
}
