package com.skapral.parrot.auth.ops;

import com.skapral.parrot.common.Operation;
import io.vavr.collection.List;

public class SequentialOperation implements Operation {
    private final List<Operation> operations;

    public SequentialOperation(List<Operation> operations) {
        this.operations = operations;
    }

    public SequentialOperation(Operation... operations) {
        this(List.of(operations));
    }

    @Override
    public final void execute() {
        operations.forEach(Operation::execute);
    }
}
