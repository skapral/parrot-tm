package com.skapral.parrot.itests.assertions.amqp.expectations;

import io.vavr.collection.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ExpectAll implements Expectation {
    private static final Logger log = LoggerFactory.getLogger(ExpectAll.class);
    private final List<Expectation> expectations;

    public ExpectAll(List<Expectation> expectations) {
        this.expectations = expectations;
    }

    public ExpectAll(Expectation... expectations) {
        this(List.of(expectations));
    }

    @Override
    public final boolean check() {
        var results = expectations.map(Expectation::check);
        log.info(results.map(Object::toString).reduce((e1, e2) -> e1 + " && " + e2));
        return results.foldLeft(true, (e1, e2) -> e1 && e2);
    }
}
