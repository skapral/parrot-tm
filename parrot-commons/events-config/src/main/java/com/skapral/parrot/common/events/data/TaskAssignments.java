package com.skapral.parrot.common.events.data;

import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
@AllArgsConstructor
public class TaskAssignments {
    private List<TaskAssignment> list;
}
