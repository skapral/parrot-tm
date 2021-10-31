package com.skapral.parrot.tasks.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
@AllArgsConstructor
public class TaskAssignment {
    UUID taskId, assigneeId;
}
