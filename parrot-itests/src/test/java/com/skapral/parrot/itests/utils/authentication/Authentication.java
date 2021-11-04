package com.skapral.parrot.itests.utils.authentication;

import java.net.http.HttpRequest;

public interface Authentication {
    void authenticate();
    HttpRequest.Builder authenticate(HttpRequest.Builder originalRequests);
}
