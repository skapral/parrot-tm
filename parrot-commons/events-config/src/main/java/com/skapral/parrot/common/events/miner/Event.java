package com.skapral.parrot.common.events.miner;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Jacksonized
@Builder
@AllArgsConstructor
public class Event {
    private final UUID uuid;
    private final String type;
    private final String outbox;
    private final String routingKey;
    private final String payload;
}
