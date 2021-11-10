package com.skapral.parrot.common.events.impl;

import com.skapral.parrot.common.Event;

public class NoEvent implements Event {
    @Override
    public final void send() {
        // do nothing
    }
}
