package com.skapral.parrot.auth.rest;

import com.pragmaticobjects.oo.equivalence.base.EObjectHint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EObjectHint(enabled = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private UUID id;
    private String login;
    private String role;
}
