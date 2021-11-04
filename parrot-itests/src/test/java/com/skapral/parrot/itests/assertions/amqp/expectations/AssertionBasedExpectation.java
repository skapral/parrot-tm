package com.skapral.parrot.itests.assertions.amqp.expectations;

import com.pragmaticobjects.oo.tests.Assertion;

public class AssertionBasedExpectation implements Expectation {
    private final Assertion assertion;

    public AssertionBasedExpectation(Assertion assertion) {
        this.assertion = assertion;
    }

    @Override
    public final boolean check() {
        try {
            assertion.check();
            return true;
        } catch(AssertionError err) {
            return false;
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
