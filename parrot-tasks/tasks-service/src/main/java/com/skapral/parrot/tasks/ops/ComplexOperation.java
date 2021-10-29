package com.skapral.parrot.tasks.ops;

import io.vavr.collection.List;

public class ComplexOperation implements Operation {
    private final List<Operation> operations;

    public ComplexOperation(List<Operation> operations) {
        this.operations = operations;
    }

    public ComplexOperation(Operation... operations) {
        this(List.of(operations));
    }

    @Override
    public final void execute() {
        operations.forEach(Operation::execute);
    }
}
