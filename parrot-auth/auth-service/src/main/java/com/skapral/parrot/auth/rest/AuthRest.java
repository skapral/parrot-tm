package com.skapral.parrot.auth.rest;


import com.skapral.parrot.auth.common.jwt.JwtClaim;
import com.skapral.parrot.auth.common.jwt.JwtUtils;
import com.skapral.parrot.auth.data.Role;
import com.skapral.parrot.auth.security.RoleAuthority;
import io.vavr.collection.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.stream.Collectors;

class RolesClaim implements JwtClaim {
    private final List<Role> roles;

    public RolesClaim(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public final String name() {
        return "roles";
    }

    @Override
    public final String value() {
        return roles.map(Role::toString).collect(Collectors.joining(","));
    }
}


@RestController
public class AuthRest {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthRest(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("login")
    public ResponseEntity<Void> login(@RequestParam("login") String login) {
        var authToken = new UsernamePasswordAuthenticationToken(
                login,
                ""
        );
        var auth = authenticationManager.authenticate(authToken);

        var roles = auth.getAuthorities().stream()
                .filter(a -> a instanceof RoleAuthority)
                .map(a -> (RoleAuthority) a)
                .map(a -> a.role)
                .collect(List.collector());

        var jwtToken = jwtUtils.generateJwtToken(
                login, new RolesClaim(roles)
        );
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        jwtUtils.getUserNameFromJwtToken(jwtToken),
                        null,
                        Collections.emptyList()
                )
        );
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/");
        headers.add("Authorization", "Bearer " + jwtToken);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
