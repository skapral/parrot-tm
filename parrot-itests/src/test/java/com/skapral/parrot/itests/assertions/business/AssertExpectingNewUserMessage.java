package com.skapral.parrot.itests.assertions.business;

import com.pragmaticobjects.oo.tests.AssertCombined;
import com.pragmaticobjects.oo.tests.Assertion;
import com.rabbitmq.client.Connection;
import com.skapral.parrot.itests.assertions.amqp.AmqpSource;
import com.skapral.parrot.itests.assertions.amqp.AssertExpectingMessagesOnAmqp;
import com.skapral.parrot.itests.assertions.amqp.AssertMessageBody;
import com.skapral.parrot.itests.assertions.amqp.AssertMessageType;
import com.skapral.parrot.itests.assertions.amqp.expectations.AssertionBasedExpectation;
import com.skapral.parrot.itests.assertions.amqp.expectations.ExpectMessage;
import com.skapral.parrot.itests.assertions.json.AssertJsonHas;
import io.vavr.collection.List;
import org.skyscreamer.jsonassert.JSONCompareMode;

public class AssertExpectingNewUserMessage extends AssertExpectingMessagesOnAmqp {
    public AssertExpectingNewUserMessage(Assertion assertion, Connection connection, String login) {
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
                        new AssertMessageType(msg, "USER_NEW"),
                        new AssertMessageBody(
                            msg,
                            body -> new AssertJsonHas(
                                body,
                                String.format("{\"login\": \"%s\"}", login),
                                JSONCompareMode.LENIENT
                            )
                        )
                    )
                )
            )
        );
    }
}
