package com.skapral.parrot.common.events.impl;

import com.skapral.parrot.common.Event;
import io.vavr.collection.List;

public class MultipleEvents implements Event {
    private final List<Event> events;

    public MultipleEvents(List<Event> events) {
        this.events = events;
    }

    public MultipleEvents(Event... events) {
        this(List.of(events));
    }

    @Override
    public final void send() {
        events.forEach(Event::send);
    }
}
