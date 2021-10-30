package com.skapral.parrot.common;

import io.vavr.collection.List;

public class SequentialOperation implements Operation {
    private final List<Operation> sequence;

    public SequentialOperation(List<Operation> sequence) {
        this.sequence = sequence;
    }

    public SequentialOperation(Operation... sequence) {
        this(List.of(sequence));
    }

    @Override
    public final void execute() {
        sequence.forEach(Operation::execute);
    }
}
