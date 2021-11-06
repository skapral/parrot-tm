package com.skapral.parrot.itests.assertions.http.endpoints;

import java.net.http.HttpRequest;

public interface Endpoint {
    HttpRequest request();
}
