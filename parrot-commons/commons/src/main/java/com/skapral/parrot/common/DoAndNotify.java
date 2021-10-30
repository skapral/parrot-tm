package com.skapral.parrot.common;

public class DoAndNotify implements Operation {
    private final Operation operation;
    private final Event event;

    public DoAndNotify(Operation operation, Event event) {
        this.operation = operation;
        this.event = event;
    }

    @Override
    public final void execute() {
        operation.execute();
        event.send();
    }
}
