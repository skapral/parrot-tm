package com.skapral.parrot.auth.security;

import com.skapral.parrot.auth.data.Role;
import org.springframework.security.core.GrantedAuthority;

public class RoleAuthority implements GrantedAuthority {
    static final long serialVersionUID = -344489682320634968L;
    public final Role role;

    public RoleAuthority(Role role) {
        this.role = role;
    }

    @Override
    public final String getAuthority() {
        return role.name();
    }
}
