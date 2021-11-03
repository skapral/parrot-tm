package com.skapral.parrot.tasks.data;

import com.pragmaticobjects.oo.equivalence.base.EObjectHint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder
@Jacksonized
@AllArgsConstructor
@EObjectHint(enabled = false)
public class TaskAssignment {
    UUID taskId, assigneeId;
}
