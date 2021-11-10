package com.skapral.parrot.accounting.rest;

import com.pragmaticobjects.oo.equivalence.base.EObjectHint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EObjectHint(enabled = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private UUID id;
    private Integer money;
}
