package com.skapral.parrot.auth.common;

import org.springframework.security.core.GrantedAuthority;

public class RoleAuthority implements GrantedAuthority {
    static final long serialVersionUID = -344489682320634968L;
    public final String role;

    public RoleAuthority(String role) {
        this.role = role;
    }

    @Override
    public final String getAuthority() {
        return role;
    }
}
