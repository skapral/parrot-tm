package com.skapral.parrot.auth.rest;

import com.pragmaticobjects.oo.equivalence.base.EObjectHint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@EObjectHint(enabled = false)
@Data
@Builder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
public class UserCreation {
    String login;
    String role;
}
