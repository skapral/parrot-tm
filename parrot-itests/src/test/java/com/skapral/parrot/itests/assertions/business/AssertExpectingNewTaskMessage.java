package com.skapral.parrot.itests.assertions.business;

import com.pragmaticobjects.oo.tests.AssertCombined;
import com.pragmaticobjects.oo.tests.Assertion;
import com.rabbitmq.client.Connection;
import com.skapral.parrot.itests.assertions.amqp.AmqpSource;
import com.skapral.parrot.itests.assertions.amqp.AssertExpectingMessagesOnAmqp;
import com.skapral.parrot.itests.assertions.amqp.AssertMessageType;
import com.skapral.parrot.itests.assertions.amqp.expectations.AssertionBasedExpectation;
import com.skapral.parrot.itests.assertions.amqp.expectations.ExpectMessage;
import io.vavr.collection.List;

public class AssertExpectingNewTaskMessage extends AssertExpectingMessagesOnAmqp {
    public AssertExpectingNewTaskMessage(Assertion assertion, Connection connection) {
        super(
            assertion,
            connection,
            List.of(
                new AmqpSource("outbox", "")
            ),
            deliveries -> new ExpectMessage(
                deliveries,
                msg -> new AssertionBasedExpectation(
                    new AssertCombined(
                        new AssertMessageType(msg, "TASK_NEW")
                    )
                )
            )
        );
    }
}
