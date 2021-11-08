package com.skapral.parrot.common.events.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Jacksonized
@Builder
@AllArgsConstructor
public class User {
    UUID id;
    String login;

}
