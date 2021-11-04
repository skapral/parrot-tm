package com.skapral.parrot.tasks.rest;

import com.pragmaticobjects.oo.equivalence.base.EObjectHint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EObjectHint(enabled = false)
public class Assignee {
    private UUID id;
    private String name;
}
