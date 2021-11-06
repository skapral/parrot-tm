package com.skapral.parrot.auth.rest;


import com.skapral.parrot.auth.common.RoleAuthority;
import com.skapral.parrot.auth.common.jwt.JwtClaim;
import com.skapral.parrot.auth.common.jwt.JwtUtils;
import com.skapral.parrot.auth.queries.UserById;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

class RolesClaim implements JwtClaim {
    private final List<Role> roles;

    public RolesClaim(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public final String name() {
        return "role";
    }

    @Override
    public final String value() {
        return roles.map(Role::toString).collect(Collectors.joining(","));
    }
}


@RestController
@Slf4j
public class AuthRest {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final JdbcTemplate template;

    @Autowired
    public AuthRest(AuthenticationManager authenticationManager, JwtUtils jwtUtils, JdbcTemplate template) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.template = template;
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
                .map(Role::valueOf)
                .collect(List.collector());

        var jwtToken = jwtUtils.generateJwtToken(
                auth.getName(), new RolesClaim(roles)
        );
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        jwtUtils.getSubjectFromJwtToken(jwtToken),
                        null,
                        Collections.emptyList()
                )
        );
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtToken);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping("current")
    public User current() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        log.info(auth.getName());
        return new UserById(
                template,
                UUID.fromString(auth.getName())
        ).get().get();
    }
}
