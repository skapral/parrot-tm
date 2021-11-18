package com.skapral.parrot.itests.assertions;

import com.pragmaticobjects.oo.tests.Assertion;
import com.skapral.parrot.itests.assertions.amqp.expectations.Expectation;
import org.awaitility.Awaitility;

import java.time.Duration;

public class AssertExpecting implements Assertion {
    private final Assertion assertion;
    private final Expectation expectation;

    public AssertExpecting(Assertion assertion, Expectation expectation) {
        this.assertion = assertion;
        this.expectation = expectation;
    }

    @Override
    public final void check() throws Exception {
        assertion.check();
        Awaitility.waitAtMost(Duration.ofMinutes(1))
            .pollDelay(Duration.ofSeconds(0))
            .pollInterval(Duration.ofSeconds(5))
            .await()
            .until(expectation::check);
    }
}
