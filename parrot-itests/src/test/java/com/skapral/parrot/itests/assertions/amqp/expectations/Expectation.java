package com.skapral.parrot.itests.assertions.amqp.expectations;

import java.util.function.Predicate;

public interface Expectation {
    boolean check();
}
