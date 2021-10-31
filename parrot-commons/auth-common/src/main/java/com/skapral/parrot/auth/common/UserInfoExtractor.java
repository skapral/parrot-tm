package com.skapral.parrot.auth.common;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface UserInfoExtractor {
    Optional<UserInfo> userInfo(HttpServletRequest request);
}
