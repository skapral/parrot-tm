package com.skapral.parrot.auth.common.mock;

import com.skapral.parrot.auth.common.UserInfo;
import com.skapral.parrot.auth.common.UserInfoExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class MockUserInfoExtractor implements UserInfoExtractor {
    @Override
    public final Optional<UserInfo> userInfo(HttpServletRequest request) {
        var userInfo = getUserInfo(request);
        var creds = userInfo.map(i -> i.split(":"));
        return creds.map(info -> {
            var user = Objects.requireNonNull(info[0]);
            var role = Objects.requireNonNull(info[1]);
            log.info("USER {} {}", user, role);
            return new UserInfo(user, role);
        });
    }

    private final Optional<String> getUserInfo(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Mock ")) {
            return Optional.of(headerAuth.substring(5));
        }
        return Optional.empty();
    }
}
