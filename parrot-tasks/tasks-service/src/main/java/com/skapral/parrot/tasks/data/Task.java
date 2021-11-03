package com.skapral.parrot.tasks.data;

import com.pragmaticobjects.oo.equivalence.base.EObjectHint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EObjectHint(enabled = false)
public class Task {
    private UUID id;
    private String description;
    private Status status;
    private UUID assigneeId;
    private String assigneeName;
}
