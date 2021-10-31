package com.skapral.parrot.auth.common;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class UserInfo {
    String login, role;
}
