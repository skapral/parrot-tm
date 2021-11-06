package com.skapral.parrot.auth.common.jwt;

import com.skapral.parrot.auth.common.UserInfo;
import com.skapral.parrot.auth.common.UserInfoExtractor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
public class JwtUserInfoExtractor implements UserInfoExtractor {
    private final JwtUtils jwtUtils;

    public JwtUserInfoExtractor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public final Optional<UserInfo> userInfo(HttpServletRequest request) {
        var jwtOpt = jwtUtils.parseJwt(request);
        return jwtOpt
                .filter(jwtUtils::validateJwtToken)
                .map(jwt -> {
                    log.info("JWT {}", jwt);
                    String user = jwtUtils.getSubjectFromJwtToken(jwt);
                    String role = jwtUtils.getClaimFromJwtToken(jwt, "role").asString();
                    return new UserInfo(user, role);
                });
    }
}
