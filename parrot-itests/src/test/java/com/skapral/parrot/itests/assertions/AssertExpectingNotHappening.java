package com.skapral.parrot.itests.assertions;

import com.pragmaticobjects.oo.tests.Assertion;
import com.skapral.parrot.itests.assertions.amqp.expectations.Expectation;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;

import java.time.Duration;
import java.util.function.Predicate;

public class AssertExpectingNotHappening implements Assertion {
    private final Assertion assertion;
    private final Expectation expectation;

    public AssertExpectingNotHappening(Assertion assertion, Expectation expectation) {
        this.assertion = assertion;
        this.expectation = expectation;
    }

    @Override
    public final void check() throws Exception {
        assertion.check();
        Awaitility.waitAtMost(Duration.ofMinutes(15))
            .pollDelay(Duration.ofSeconds(15))
            .pollInterval(Duration.ofSeconds(15))
            .untilAsserted(() -> {
                var expectationMet = expectation.check();
                Assertions.assertThat(expectationMet).isFalse();
            });
    }
}
