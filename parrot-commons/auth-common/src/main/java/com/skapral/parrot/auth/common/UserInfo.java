package com.skapral.parrot.auth.common;

import com.pragmaticobjects.oo.equivalence.base.EObjectHint;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
@EObjectHint(enabled = false)
public class UserInfo {
    String login, role;
}
